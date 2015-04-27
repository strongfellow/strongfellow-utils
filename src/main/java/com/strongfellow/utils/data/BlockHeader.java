package com.strongfellow.utils.data;

import lombok.Data;

@Data
public class BlockHeader {

	private long version; // 4 bytes
	private String previousBlock; // 32 bytes
	private String merkleRoot; // 32 bytes
	private long time; // 4 bytes
	private String bits; // 4 bytes
	private long nonce; // 4 bytes;
	
}
