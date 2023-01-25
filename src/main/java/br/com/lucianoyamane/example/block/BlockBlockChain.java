package br.com.lucianoyamane.example.block;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.ArrayList;
import java.util.List;

public class BlockBlockChain {
	
	private Block block;

	private List<TransactionBlockChain> transactionBlockChains;

    public static BlockBlockChain init(Block block) {
        return new BlockBlockChain(block);
    }

	private BlockBlockChain(Block block) {
		this.block = block;
		this.setTransactionBlockChains(new ArrayList());
	}

	public BlockBlockChain processGenesis() {
		this.process("0");
		return this;
	}

	public BlockBlockChain process(String previousHash) {
		this.block.setPreviousHash(previousHash);
		this.block.setHash(calculateHash());
		this.block.setMerkleRoot(StringUtil.getMerkleRoot(this.getTransactionsId()));
		return this;
	}

	public Boolean isGenesis() {
		return this.block.getPreviousHash().equals("0");
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

	private List<String> getTransactionsId() {
		return this.getTransactionBlockChains().stream().map(transaction -> transaction.getFingerPrint()).toList();
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
		if(transaction == null) {
			return this;
		}

		if(!transaction.processTransaction()) {
			SystemOutPrintlnDecorator.vermelho("Transaction failed to process. Discarded.");
			return this;
		}

		this.getTransactionBlockChains().add(transaction);
		SystemOutPrintlnDecorator.ciano("Transaction Successfully added to Block");
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

	public void setTransactionBlockChains(List<TransactionBlockChain> transactionBlockChains) {
		this.transactionBlockChains = transactionBlockChains;
	}

	public List<TransactionBlockChain> getTransactionBlockChains() {
		return this.transactionBlockChains;
	}

	public void isConsistent(String previousHash, int difficulty) {
		if (!this.compareRegisteredAndCalculatedHash()) {
			throw new BlockChainException("Current Hashes not equal");
		}
		if (!this.compareHash(previousHash)) {
			throw new BlockChainException("Previous Hashes not equal");
		}
		if(!this.hashIsSolved(difficulty)) {
			throw new BlockChainException("This block hasn't been mined");
		}
	}

}
