package com.strongfellow.utils.data;

import lombok.Data;

@Data
public class TXIn {

	private String outputHash; // 32 bytes
	private long outputIndex; // 4 bytes
	private String unlockingScript; // varies
	private long sequenceNo; // 4 bytes
}
