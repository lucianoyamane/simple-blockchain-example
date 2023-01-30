package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class BlockChainApp {

	private List<BlockBlockChain> blockchain;

	private BlockBlockChain genesis;

	private BlockChainApp() {
		this.blockchain = new ArrayList();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String registraTransacaoGenesis(TransactionBlockChain transaction) {
		BlockBlockChain genesis = BlockBlockChain.genesis(transaction);
		this.setGenesis(genesis);
		return this.proofOfWork(genesis);
	}

	public String registraTransacao(TransactionBlockChain transaction, String previousHash) {
		BlockBlockChain blockBlockChain = BlockBlockChain.init(transaction, previousHash);
		this.addBlock(blockBlockChain);
		return this.proofOfWork(blockBlockChain);
	}

	private String proofOfWork(BlockBlockChain blockBlockChain) {
		return blockBlockChain
				.mine(Difficulty.getInstance().getDifficulty())
				.getHash();
	}


	private void addBlock(BlockBlockChain newBlockBlockChain) {
		blockchain.add(newBlockBlockChain);
	}

	private void setGenesis(BlockBlockChain newBlockBlockChain) {
		this.genesis = newBlockBlockChain;
	}

	private BlockBlockChain getGenesis() {
		return this.genesis;
	}


	private void bootstrapIsChainValid(PreviousBlockData previousBlockData) {
		BlockBlockChain blockBlockChainGenesis = this.getGenesis();

		TransactionBlockChain transactionBlockChain = blockBlockChainGenesis.getTransactionBlockChain();
		if (transactionBlockChain.getSenderTransactionOperationBlockChain() != null) {
			previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getSenderTransactionOperationBlockChain());
		}
		if (transactionBlockChain.getReceiverTransactionOperationBlockChain() != null) {
			previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getReceiverTransactionOperationBlockChain());
		}
		previousBlockData.setPreviousHash(blockBlockChainGenesis.getHash());
	}
	public void isValid() {
		PreviousBlockData previousBlockData = new PreviousBlockData(Difficulty.getInstance().getDifficulty());

		this.bootstrapIsChainValid(previousBlockData);

		for(BlockBlockChain currentBlockBlockChain : this.blockchain) {
			currentBlockBlockChain.isConsistent(previousBlockData);
		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}

	public class PreviousBlockData {

		private Integer difficulty;

		private String previousHash;

		private List<TransactionOperationBlockChain> transactionOperationBlockChains;

		public PreviousBlockData(Integer difficulty) {
			this.setTransactionOperationBlockChains(new ArrayList<>());
			this.setDifficulty(difficulty);
		}

		public Integer getDifficulty() {
			return difficulty;
		}

		private void setDifficulty(Integer difficulty) {
			this.difficulty = difficulty;
		}

		public String getPreviousHash() {
			return previousHash;
		}

		public void setPreviousHash(String previousHash) {
			this.previousHash = previousHash;
		}

		public List<TransactionOperationBlockChain> getTransactionOperationBlockChains() {
			return transactionOperationBlockChains;
		}

		private void setTransactionOperationBlockChains(List<TransactionOperationBlockChain> transactionOperationBlockChains) {
			this.transactionOperationBlockChains = transactionOperationBlockChains;
		}

		public void addTransactionOperationBlockChains(TransactionOperationBlockChain transactionOperationBlockChain) {
			if (transactionOperationBlockChain != null) {
				this.getTransactionOperationBlockChains().add(transactionOperationBlockChain);
			}
		}

		public void removeTransactionOperationBlockChains(TransactionOperationBlockChain transactionOperationBlockChain) {
			this.getTransactionOperationBlockChains().remove(transactionOperationBlockChain);
		}
	}

}
