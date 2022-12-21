package br.com.lucianoyamane.example;

import java.util.ArrayList;
import java.util.Date;
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
		return this.getTransactions().stream().map(transaction -> transaction.getTransactionId()).toList();
	}

	public void mineBlock(int difficulty) {
		this.setMerkleRoot(StringUtil.getMerkleRoot(this.getTransactionsId()));
		String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0" 
		while(!this.getHash().substring( 0, difficulty).equals(target)) {
			this.increaseNonce();
			this.setHash(calculateHash());
		}
		System.out.println("Block Mined!!! : " + hash);
	}

	private void increaseNonce() {
		this.nonce ++;
	}

	public boolean addTransaction(Transaction transaction) {
		if(transaction == null) {
			return Boolean.FALSE;
		}

		if((!"0".equals(previousHash))) {
			if(transaction.processTransaction() != true) {
				System.out.println("Transaction failed to process. Discarded.");
				return Boolean.FALSE;
			}
		}

		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return Boolean.TRUE;
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
