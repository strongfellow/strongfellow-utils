package com.strongfellow.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockIterable implements Iterable<ByteBuffer> {
	
	private static final Logger logger = LoggerFactory.getLogger(BlockIterable.class);
	
	private final InputStream in;
	private byte[] bytes = new byte[16 * 1024];
	private int blockLength;

	public BlockIterable(InputStream in) {
		this.in = in;
	}
	
	private class BlockIterator implements Iterator<ByteBuffer> {

		@Override
		public boolean hasNext() {
			try {
				in.read(bytes, 0, 8);
				BlockIterable.this.blockLength = (int)Utils.uint32(bytes, 4);
				logger.info("blockLength: {}", BlockIterable.this.blockLength);
				while (8 + BlockIterable.this.blockLength >= BlockIterable.this.bytes.length) {
					byte[] newBytes = new byte[bytes.length * 2];
					for (int i = 0; i < 8; i++) {
						newBytes[i] = bytes[i];
					}
					BlockIterable.this.bytes = newBytes;
				}
				in.read(BlockIterable.this.bytes, 8, BlockIterable.this.blockLength);
			} catch(IOException e) {
				return false;
			}
			return true;
		}

		@Override
		public ByteBuffer next() {
			return ByteBuffer.wrap(bytes, 0, 8 + BlockIterable.this.blockLength);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}

	@Override
	public Iterator<ByteBuffer> iterator() {
		return new BlockIterator();
	}
}
