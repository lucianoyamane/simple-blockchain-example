package br.com.lucianoyamane.example.blockchain;

import br.com.lucianoyamane.example.models.Block;
import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;

import java.util.Objects;

public class BlockExecutor implements Executor {
	
	private Block block;

	private TransactionExecutor transactionExecutor;

	public static BlockExecutor genesis(TransactionExecutor transactionExecutor) {
		return new BlockExecutor(Block.init(), transactionExecutor).processAsGenesis();
	}

    public static BlockExecutor init(TransactionExecutor transactionExecutor, String previousHash) {
        return new BlockExecutor(Block.init(), transactionExecutor).process(previousHash);
    }

	private BlockExecutor(Block block, TransactionExecutor transactionExecutor) {
		this.setBlock(block);
		this.setTransactionBlockChain(transactionExecutor);
	}

	private BlockExecutor processAsGenesis() {
		this.process("0");
		return this;
	}

	private BlockExecutor process(String previousHash) {
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

	public BlockExecutor mine(int difficulty) {
		String zeros = StringUtil.getCharsZeroByDifficuty(difficulty);
		while(!this.testHashCondition(zeros)) {
			this.getBlock().increaseNonce();
			this.getBlock().setHash(calculateHash());

//			SystemOutPrintlnDecorator.azul("Genereted hash : " + this.getBlock().getHash());
		}
		SystemOutPrintlnDecorator.azul("Previous Hash : " + this.getBlock().getPreviousHash());
		SystemOutPrintlnDecorator.azul("Nonce : " + this.getBlock().getNonce());
		SystemOutPrintlnDecorator.azul("Merkle : " + this.getBlock().getMerkleRoot());
		SystemOutPrintlnDecorator.ciano("Block Mined!!! : " + this.getBlock().getHash());
		return this;
	}

	private Boolean testHashCondition(String target) {
		return this.getBlock().getHash().startsWith(target);
	}

	private BlockExecutor setTransactionBlockChain(TransactionExecutor transactionExecutor) {
		if (Objects.nonNull(transactionExecutor) && transactionExecutor.processTransaction()) {
			this.transactionExecutor = transactionExecutor;
			SystemOutPrintlnDecorator.ciano("Transaction Successfully added to Block");
		} else {
			SystemOutPrintlnDecorator.vermelho("Transaction failed to process. Discarded.");
		}
		return this;
	}
	public String getHash() {
		return this.getBlock().getHash();
	}

	public String getMerklet() {
		return this.getBlock().getMerkleRoot();
	}

	public TransactionExecutor getTransactionBlockChain() {
		return transactionExecutor;
	}

	private Block getBlock() {
		return block;
	}

	private void setBlock(Block block) {
		this.block = block;
	}

	public String getPreviousHash() {
		return this.getBlock().getPreviousHash();
	}

}
