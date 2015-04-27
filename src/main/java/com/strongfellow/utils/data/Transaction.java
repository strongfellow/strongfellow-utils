package com.strongfellow.utils.data;

import java.util.List;

import lombok.Data;

@Data
public class Transaction {

	private long txVersion; // 4 bytes
	private List<TXIn> txIns; // varies
	private List<TXout> txOuts; // varies
	private String lockTime; // 4 bytes

	private String txHash;
	private long txLength;
}
