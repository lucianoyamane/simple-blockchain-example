package br.com.lucianoyamane.example.block;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BlockBlockChain implements BlockChainObject {
	
	private Block block;

	private TransactionBlockChain transactionBlockChain;

    public static BlockBlockChain init(Block block) {
        return new BlockBlockChain(block);
    }

	private BlockBlockChain(Block block) {
		this.block = block;
	}

	public BlockBlockChain processGenesis() {
		this.process("0");
		return this;
	}

	public BlockBlockChain process(String previousHash) {
		this.block.setPreviousHash(previousHash);
		this.block.setHash(calculateHash());
		this.block.setMerkleRoot(StringUtil.getMerkleRoot(this.getTransactionId()));
		return this;
	}
	private String calculateHash() {
		return StringUtil.encode(this.createCompositionToHash());
	}

	private String createCompositionToHash() {
		StringBuilder composition = new StringBuilder();
		composition
				.append(this.block.getPreviousHash())
				.append(this.block.getTimeStamp())
				.append(this.block.getNonce())
				.append(this.block.getMerkleRoot());
		return composition.toString();
	}

	private String getTransactionId() {
		if (this.getTransactionBlockChain() != null) {
			return this.getTransactionBlockChain().getFingerPrint();
		}
		return null;
	}

	public BlockBlockChain mine(int difficulty) {
		String zeros = StringUtil.getCharsZeroByDifficuty(difficulty);
		while(!this.testHashCondition(zeros)) {
			this.block.increaseNonce();
			this.block.setHash(calculateHash());
		}
		SystemOutPrintlnDecorator.ciano("Block Mined!!! : " + this.block.getHash());
		return this;
	}

	private Boolean testHashCondition(String target) {
		return this.block.getHash().startsWith(target);
	}

	public BlockBlockChain addTransaction(TransactionBlockChain transaction) {
		if (Objects.nonNull(transaction) && transaction.processTransaction()) {
			this.setTransactionBlockChain(transaction);
			SystemOutPrintlnDecorator.ciano("Transaction Successfully added to Block");
		} else {
			SystemOutPrintlnDecorator.vermelho("Transaction failed to process. Discarded.");
		}
		return this;
	}

	public Boolean compareRegisteredAndCalculatedHash() {
		return this.block.getHash().equals(this.calculateHash());
	}

	public Boolean compareHash(String previousHash) {
		return previousHash.equals(this.block.getPreviousHash());
	}

	public Boolean hashIsSolved(int difficulty) {
		return this.block.getHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
	}

	public String getHash() {
		return this.block.getHash();
	}

	public TransactionBlockChain getTransactionBlockChain() {
		return transactionBlockChain;
	}

	public void setTransactionBlockChain(TransactionBlockChain transactionBlockChain) {
		this.transactionBlockChain = transactionBlockChain;
	}

	@Override
	public void isConsistent(BlockChainApp.PreviousBlockData previousBlockData) {
		if (!this.compareRegisteredAndCalculatedHash()) {
			throw new BlockChainException("Current Hashes not equal");
		}
		if (!this.compareHash(previousBlockData.getPreviousHash())) {
			throw new BlockChainException("Previous Hashes not equal");
		}
		if(!this.hashIsSolved(previousBlockData.getDifficulty())) {
			throw new BlockChainException("This block hasn't been mined");
		}

		TransactionBlockChain currentBlockTransaction = this.getTransactionBlockChain();
		if (currentBlockTransaction != null) {
			currentBlockTransaction.isConsistent(previousBlockData);
		}
		previousBlockData.setPreviousHash(this.getHash());
	}

}
