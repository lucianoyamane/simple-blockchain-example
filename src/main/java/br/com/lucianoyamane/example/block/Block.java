package br.com.lucianoyamane.example.block;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.transaction.TransactionOutput;

import java.util.ArrayList;
import java.util.List;

public class Block {
	
	private String hash;
	private String previousHash;
	private String merkleRoot;
	private List<Transaction> transactions;
	private long timeStamp;
	private int nonce;

    public static Block genesis() {
        return init("0");
    }

    public static Block init(String previousHash) {
        return new Block(previousHash);
    }

	private Block(String previousHash) {
		this.setPreviousHash(previousHash);
		this.setTimeStamp(System.currentTimeMillis());
		this.setTransactions(new ArrayList());
		this.setHash(calculateHash());
		this.setMerkleRoot("");
	}

	public Boolean isGenesis() {
		return this.getPreviousHash().equals("0");
	}
	public String calculateHash() {
		return StringUtil.encode(this.createCompositionToHash());
	}

	private String createCompositionToHash() {
		StringBuilder composition = new StringBuilder();
		composition
				.append(this.getPreviousHash())
				.append(this.getTimeStamp())
				.append(this.getNonce())
				.append(this.getMerkleRoot());
		return composition.toString();
	}

	public List<String> getTransactionsId() {
		return this.getTransactions().stream().map(transaction -> transaction.getHash()).toList();
	}

	public List<TransactionOutput> getTransactionOutputs() {
		List<TransactionOutput> transactionOutputs = new ArrayList<>();
		for(Transaction transaction : this.transactions) {
			if (transaction.getSenderTransactionOutput() != null) {
				transactionOutputs.add(transaction.getSenderTransactionOutput());
			}
			if (transaction.getReceiverTransactionOutput() != null) {
				transactionOutputs.add(transaction.getReceiverTransactionOutput());
			}
		}
		return transactionOutputs;
	}

	public void mineBlock(int difficulty) {
		String zeros = StringUtil.getCharsZeroByDifficuty(difficulty);
		while(!this.testHashCondition(zeros)) {
			this.increaseNonce();
			this.setHash(calculateHash());
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	private Boolean testHashCondition(String target) {
		return this.getHash().startsWith(target);
	}

	private void increaseNonce() {
		this.nonce ++;
	}

	public boolean addTransaction(Transaction transaction) {
		if(transaction == null) {
			return Boolean.FALSE;
		}

		if(!transaction.processTransaction()) {
			System.out.println("Transaction failed to process. Discarded.");
			return Boolean.FALSE;
		}

		transactions.add(transaction);
		this.setMerkleRoot(StringUtil.getMerkleRoot(this.getTransactionsId()));
		System.out.println("Transaction Successfully added to Block");
		return Boolean.TRUE;
	}

	public Boolean compareRegisteredAndCalculatedHash() {
		return this.getHash().equals(this.calculateHash());
	}

	public Boolean compareHash(String previousHash) {
		return previousHash.equals(this.getPreviousHash());
	}

	public Boolean hashIsSolved(int difficulty) {
		return this.getHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
	}

	public String getHash() {
		return hash;
	}

	private void setHash(String hash) {
		this.hash = hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}

	private void setPreviousHash(String previousHash) {
		this.previousHash = previousHash;
	}

	private String getMerkleRoot() {
		return merkleRoot;
	}

	private void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	private void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	private long getTimeStamp() {
		return timeStamp;
	}

	private void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	private int getNonce() {
		return nonce;
	}

}