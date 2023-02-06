package br.com.lucianoyamane.example.block;

import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;

import java.util.Objects;

public class BlockBlockChain implements BlockChainObject {
	
	private Block block;

	private TransactionBlockChain transactionBlockChain;

	public static BlockBlockChain genesis(TransactionBlockChain transactionBlockChain) {
		return new BlockBlockChain(Block.init(), transactionBlockChain).processAsGenesis();
	}

    public static BlockBlockChain init(TransactionBlockChain transactionBlockChain, String previousHash) {
        return new BlockBlockChain(Block.init(), transactionBlockChain).process(previousHash);
    }

	private BlockBlockChain(Block block, TransactionBlockChain transactionBlockChain) {
		this.setBlock(block);
		this.setTransactionBlockChain(transactionBlockChain);
	}

	public BlockBlockChain processAsGenesis() {
		this.process("0");
		return this;
	}

	public BlockBlockChain process(String previousHash) {
		this.getBlock().setPreviousHash(previousHash);
		this.getBlock().setHash(calculateHash());
		this.getBlock().setMerkleRoot(StringUtil.getMerkleRoot(this.getTransactionId()));
		return this;
	}
	public String calculateHash() {
		return StringUtil.encode(this.createCompositionToHash());
	}

	private String createCompositionToHash() {
		StringBuilder composition = new StringBuilder();
		composition
				.append(this.getBlock().getPreviousHash())
				.append(this.getBlock().getTimeStamp())
				.append(this.getBlock().getNonce())
				.append(this.getBlock().getMerkleRoot());
		return composition.toString();
	}

	private String getTransactionId() {
		if (this.getTransactionBlockChain() != null) {
			return this.getTransactionBlockChain().getHash();
		}
		return null;
	}

	public BlockBlockChain mine(int difficulty) {
		String zeros = StringUtil.getCharsZeroByDifficuty(difficulty);
		while(!this.testHashCondition(zeros)) {
			this.getBlock().increaseNonce();
			this.getBlock().setHash(calculateHash());
		}
		SystemOutPrintlnDecorator.ciano("Block Mined!!! : " + this.getBlock().getHash());
		return this;
	}

	private Boolean testHashCondition(String target) {
		return this.getBlock().getHash().startsWith(target);
	}

	private BlockBlockChain setTransactionBlockChain(TransactionBlockChain transactionBlockChain) {
		if (Objects.nonNull(transactionBlockChain) && transactionBlockChain.processTransaction()) {
			this.transactionBlockChain = transactionBlockChain;
			SystemOutPrintlnDecorator.ciano("Transaction Successfully added to Block");
		} else {
			SystemOutPrintlnDecorator.vermelho("Transaction failed to process. Discarded.");
		}
		return this;
	}
	public String getHash() {
		return this.getBlock().getHash();
	}

	public TransactionBlockChain getTransactionBlockChain() {
		return transactionBlockChain;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public String getPreviousHash() {
		return this.getBlock().getPreviousHash();
	}

}
