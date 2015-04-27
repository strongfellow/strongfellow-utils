package com.strongfellow.utils.data;

import lombok.Data;

@Data
public class TXout {

	private long value; // 4 bytes
	private String lockingScript; // varies
}
