package com.strongfellow.utils.data;

import java.util.List;

import lombok.Data;

@Data
public class Block {

	private String magicNumber; //  4 bytes
	private long blockLength; // 4 bytes
	private BlockHeader header; // 80 bytes
	private List<Transaction> transactions; // varies
	
	private String blockHash;

}
