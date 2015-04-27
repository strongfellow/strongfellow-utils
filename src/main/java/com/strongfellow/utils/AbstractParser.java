package com.strongfellow.utils;

import java.security.NoSuchAlgorithmException;

public class AbstractParser {
	
	private byte[] buffer;
	private int offset;

	protected long get() {
		long result = buffer[offset] & 0xff;
		offset++;
		return result;
	}

	protected int getOffset() {
		// TODO Auto-generated method stub
		return this.offset;
	}
	
	protected byte[] getBuffer() {
		return this.buffer;
	}
	
	protected void setBuffer(byte[] buffer, int offset) {
		this.buffer = buffer;
		this.offset = offset;
	}
	
	protected String hex(long n) {
		String result = Utils.hex(buffer, this.offset, n);
		this.offset += n;
		return result;
	}
	
	protected String reverseHex(long n) {
		String result = Utils.reverseHex(buffer, this.offset, n);
		this.offset += n;
		return result;
	}
	
	protected String hashHex() {
		return reverseHex(32);
	}
	
	protected long uint(int n) {
		long result = Utils.uint(this.buffer, this.offset, n);
		this.offset += n;
		return result;
	}

	protected long uint16() {
		return this.uint(2);
	}
		
	protected long uint32() {
		return this.uint(4);
	}
	
	protected long uint64() {
		return this.uint(8);
	}

	protected long varInt() {
		long b = (long)(this.buffer[this.offset] & 0xff);
		this.offset++;
		if (b < 0xfd) {
			return b;
		} else if (b == 0xfd) {
			return uint16();
		} else if (b == 0xfe) {
			return uint32();
		} else {
			return uint64();
		}
	}

	protected String varString() {
		long length = this.varInt();
		return hex(length);
	}

	protected String doubleSha(int offset, int length) throws ParseException {
		try {
			return Utils.doubleSha(this.buffer, offset, length);
		} catch (NoSuchAlgorithmException e) {
			throw new ParseException(e);
		}
	}

}
