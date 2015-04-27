package com.strongfellow.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.strongfellow.utils.data.Block;

public class Main {
		
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) throws IOException, ParseException, NoSuchAlgorithmException, InterruptedException {

		InputStream in = new BufferedInputStream(System.in);
		byte[] bytes = new byte[16 * 1024];
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		BlockParser parser = new BlockParser();
		while (true) {
			int y = in.read(bytes, 0, 8);
			if (y == -1) {
				System.exit(0);
			}
			long n = Utils.uint32(bytes,  4);
			while (8 + n >= bytes.length) {
				byte[] newBs = new byte[2 * bytes.length];
				for (int i = 0; i < 8; i++) {
					newBs[i] = bytes[i];
				}
				bytes = newBs;
			}
			int location = 8;
			for (int i = 0; n != 0; i++) {
				int x = in.read(bytes, location, (int)n);
				n -= x;
				location += x;
				if (i != 0) {
					logger.info("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
					Thread.sleep(1000 * (1 << i));
				}
			}
			Block block = parser.parse(bytes);
			String v = mapper.writeValueAsString(block);
			System.out.println(v);
		}
	}

}
