package com.strongfellow.utils;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
	public static String hex(byte[] buffer, int start, int length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			str.append(String.format("%02x", (0xFF & buffer[start + i])));
		}
		return str.toString();
	}
	public static String hex(byte[] buffer) {
		return hex(buffer, 0, buffer.length);
	}
		
	public static long uint(byte[] bytes, int offset, int length) {
		long result = 0;
		for (int i = 0; i < length; i++) {
			result += (((long)(bytes[offset + i])) & 0xffL) << (8 * i);
		}
		return result;
	}
	public static long uint16(byte[] bytes, int offset) {
		return uint(bytes, offset, 2);
	}
	public static long uint32(byte[] bytes, int offset) {
		return uint(bytes, offset, 4);
	}
	public static long uint64(byte[] bytes, int offset) {
		return uint(bytes, offset, 8);
	}

	public static byte howManyVarIntBytes(long b) {
		if (b < 0xfd) {
			return 0;
		} else if (b == 0xfd) {
			return 2;
		} else if (b == 0xfe) {
			return 4;
		} else if (b == 0xff) {
			return 8;
		} else { 
			throw new RuntimeException("bad byte: " + b);
		}
	}

	public static String hex(ByteBuffer buffer, long length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			str.append(String.format("%02x", buffer.get() & 0xff));
		}
		return str.toString();
	}
	
	public static String hex(byte[] bytes, int offset, long length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			str.append(String.format("%02x", bytes[offset + i] & 0xff));
		}
		return str.toString();
	}

	public static String reverseHex(byte[] bytes, int offset, long length) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < length; i++) {
			str.append(String.format("%02x", bytes[(int) (offset + length - (i + 1))] & 0xff));
		}
		return str.toString();
	}
	
	public static String doubleSha(byte[] buffer, int offset, int len) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(buffer, offset, len);
		byte[] bytes = digest.digest();
		digest.update(bytes);
		bytes = digest.digest();
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			result.append(String.format("%02x", bytes[bytes.length - (i + 1)]));
		}
		return result.toString();
	}

	public static byte[] unhex(String s) {
		byte[] result = new byte[s.length() / 2];
		for (int i = 0; i < result.length; i++) {
			result[i] = (byte) Integer.parseInt(s.substring(2*i, 2*i + 2), 16);
		}
		return result;
	}

}
