package com.strongfellow.utils;

import java.util.ArrayList;
import java.util.List;

import com.strongfellow.utils.data.Block;
import com.strongfellow.utils.data.BlockHeader;
import com.strongfellow.utils.data.TXIn;
import com.strongfellow.utils.data.TXout;
import com.strongfellow.utils.data.Transaction;

public class BlockParser extends AbstractParser {

	public Transaction parseTransaction(byte[] bytes, int offset) throws ParseException {
		this.setBuffer(bytes, offset);
		Transaction transaction = new Transaction();
		int start = this.getOffset();
		transaction.setTxVersion(this.uint32());
		{
			int inputCount = (int) varInt();
			List<TXIn> inputs = new ArrayList<TXIn>(inputCount);
			transaction.setTxIns(inputs);
			for (int i = 0; i < inputCount; i++) {
				TXIn input = new TXIn();
				input.setOutputHash(this.hashHex());
				input.setOutputIndex(this.uint32());
				input.setUnlockingScript(this.varString());
				input.setSequenceNo(this.uint32());
				inputs.add(input);
			}
		}
		{
			int outputCount = (int) varInt();
			List<TXout> outputs = new ArrayList<TXout>(outputCount);
			transaction.setTxOuts(outputs);
			for (int i = 0; i < outputCount; i++) {
				TXout output = new TXout();
				long n = 0;
				for (int j = 0; j < 8; j++) {
					n += (this.get() << (8 * j));
				}
				output.setValue(n);
				output.setLockingScript(this.varString());
				outputs.add(output);
			}
		}

		transaction.setLockTime(this.hex(4));
		int length = this.getOffset() - start;
		transaction.setTxHash(this.doubleSha(start, length));
		transaction.setTxLength(length);
		return transaction;
	}
	
	public Block parse(byte[] bytes) throws ParseException {
		Block block = new Block();
		this.setBuffer(bytes, 0);
		block.setMagicNumber(this.hex(4));
		block.setBlockLength(this.uint32());
		block.setBlockHash(this.doubleSha(this.getOffset(), 80));
		block.setHeader(parseBlockHeader());
		int txCount = (int)varInt();

		List<Transaction> transactions = new ArrayList<Transaction>(txCount);
		block.setTransactions(transactions);
		for (int i = 0; i < txCount; i++) {
			Transaction t = this.parseTransaction(this.getBuffer(), this.getOffset());
			transactions.add(t);
		}
		return block;
	}

	private BlockHeader parseBlockHeader() {
		BlockHeader h = new BlockHeader();
		h.setVersion(this.uint32());
		h.setPreviousBlock(this.hashHex());
		h.setMerkleRoot(this.hashHex());
		h.setTime(this.uint32());
		h.setBits(this.hex(4));
		h.setNonce(this.uint32());
		return h;
	}
	
}
