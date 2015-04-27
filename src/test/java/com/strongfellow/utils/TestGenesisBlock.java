package com.strongfellow.utils;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.commons.codec.binary.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.strongfellow.utils.data.Block;
import com.strongfellow.utils.data.TXIn;
import com.strongfellow.utils.data.TXout;
import com.strongfellow.utils.data.Transaction;

public class TestGenesisBlock {

	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	private static final String genesisHex = "f9beb4d91d0100000100000000000000000000000000000000000000000000000000000000000000000000003ba3edfd7a7b12b27ac72c3e67768f617fc81bc3888a51323a9fb8aa4b1e5e4a29ab5f49ffff001d1dac2b7c0101000000010000000000000000000000000000000000000000000000000000000000000000ffffffff4d04ffff001d0104455468652054696d65732030332f4a616e2f32303039204368616e63656c6c6f72206f6e206272696e6b206f66207365636f6e64206261696c6f757420666f722062616e6b73ffffffff0100f2052a01000000434104678afdb0fe5548271967f1a67130b7105cd6a828e03909a67962e0ea1f61deb649f6bc3f4cef38c4f35504e51ec112de5c384df7ba0b8d578a4c702b6bf11d5fac00000000";
	
	@Test
	public void testGenesis() throws ParseException {
		BlockParser parser = new BlockParser();
		byte[] genesisBytes = hexStringToByteArray(genesisHex);
		Block block = parser.parse(genesisBytes);

		Assert.assertEquals(block.getBlockHash(),
				"000000000019d6689c085ae165831e934ff763ae46a2a6c172b3f1b60a8ce26f");
		Assert.assertEquals(block.getBlockLength(), 285);
		
		Assert.assertEquals(block.getHeader().getVersion(), 1);
		Assert.assertEquals(block.getHeader().getPreviousBlock(),
				"0000000000000000000000000000000000000000000000000000000000000000");
		Assert.assertEquals(block.getHeader().getMerkleRoot(),
				"4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b");
		Assert.assertEquals(block.getHeader().getTime(), 1231006505);
		Assert.assertEquals(block.getHeader().getBits(), "ffff001d");
		Assert.assertEquals(block.getHeader().getNonce(), 2083236893l);
		
		List<Transaction> transactions = block.getTransactions();
		Assert.assertEquals(transactions.size(), 1);
		
		Transaction t = transactions.get(0);
		Assert.assertEquals(t.getTxVersion(), 1L);

		List<TXIn> inputs = t.getTxIns();
		Assert.assertEquals(inputs.size(), 1);

		TXIn input = inputs.get(0);
		Assert.assertEquals(input.getOutputHash(), "0000000000000000000000000000000000000000000000000000000000000000");
		Assert.assertEquals(input.getOutputIndex(), 0xFFFFFFFFL);
		Assert.assertEquals(input.getUnlockingScript(), "04FFFF001D0104455468652054696D65732030332F4A616E2F32303039204368616E63656C6C6F72206F6E206272696E6B206F66207365636F6E64206261696C6F757420666F722062616E6B73".toLowerCase());
		Assert.assertEquals((int)input.getSequenceNo(), -1);
		
		List<TXout> outputs = t.getTxOuts();
		Assert.assertEquals(outputs.size(), 1);
		TXout output = outputs.get(0);
		Assert.assertEquals(output.getValue(), 5000000000L);
		Assert.assertEquals(output.getLockingScript(), "4104678afdb0fe5548271967f1a67130b7105cd6a828e03909a67962e0ea1f61deb649f6bc3f4cef38c4f35504e51ec112de5c384df7ba0b8d578a4c702b6bf11d5fac");

		Assert.assertEquals(t.getLockTime(), "00000000");

		Assert.assertEquals(t.getTxLength(), 204);
		Assert.assertEquals(t.getTxHash(), "4a5e1e4baab89f3a32518a88c31bc87f618f76673e2cc77ab2127b7afdeda33b");

	}
}
