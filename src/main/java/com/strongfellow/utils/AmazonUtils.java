package com.strongfellow.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class AmazonUtils {

	private static final Logger logger = LoggerFactory.getLogger(AmazonUtils.class);

	public static class CacheResult {	
		public CacheResult(boolean hit, String md5) {
			this.cacheHit = hit;
			this.md5 = md5;
		}
		
		public final boolean cacheHit;
		public final String md5;
	}
	
	public static class CacheException extends Exception {
		private static final long serialVersionUID = 1L;

		public CacheException(Exception e) {
			super(e);
		}
	}
	
	private static CacheResult cache(AmazonS3 s3, String bucket, String key, InputStream in) throws CacheException {
		byte[] bytes;
		try {
			bytes = IOUtils.toByteArray(in);
			in.close();
		} catch (IOException e) {
			throw new CacheException(e);
		}
		return cache(s3, bucket, key, bytes);
	}
	public static CacheResult cache(AmazonS3 s3, String bucket, String key, String localFile) throws FileNotFoundException, CacheException {
		return cache(s3, bucket, key, new File(localFile));
	}
	
	public static CacheResult cache(AmazonS3 s3, String bucket, String key, File localFile) throws CacheException {
		logger.info("begin caching from {}", localFile);
		CacheResult result;
		try {
			result = cache(s3, bucket, key, new FileInputStream(localFile));
		} catch (FileNotFoundException e) {
			throw new CacheException(e);
		}
		logger.info("begin caching from {}", localFile);
		return result;
	}
	
	public static CacheResult cache(AmazonS3 s3,
				String bucket, String key, byte[] payload) throws CacheException {
		return cache(s3, bucket, key, payload, 0, payload.length);
	}
	
    public static CacheResult cache(AmazonS3 s3, String bucketName, String key, byte[] payload, int offset, int length) throws CacheException {
    	logger.info("begin caching s3://{}/{}", bucketName, key);
    	
    	MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new CacheException(e);
		}
		digest.update(payload, offset, length);
		String md5 = Utils.hex(digest.digest());
		boolean cacheHit = false;

    	try {
    		ObjectMetadata meta = s3.getObjectMetadata(bucketName, key);
    		String etag = meta.getETag();
    		logger.info("etag in s3: {}", etag);
    		logger.info("md5 locally: {}", md5);
    		if (md5 !=  null && md5.equals(etag)) {
    			cacheHit = true;
    		}
    	} catch(Throwable t) {
    		logger.error("couldn't find " + key, t);
    	}

    	if (cacheHit) {
        	logger.info("cache hit, skipping.");
        } else {
        	logger.info("cache miss, uploading...");
        	InputStream input = new ByteArrayInputStream(payload, offset, length);
        	s3.putObject(bucketName, key, input, null);
        	try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    	logger.info("finished caching s3://{}/{}", bucketName, key);
    	return new CacheResult(cacheHit, md5);
    }

	
}
