package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.Block;
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

	public String genesisBlock(TransactionBlockChain transaction) {
		BlockBlockChain genesis = BlockBlockChain.init(Block.init())
								.addTransaction(transaction)
								.processGenesis()
								.mine(Difficulty.getInstance().getDifficulty());
		addGenesis(genesis);
		return genesis.getHash();
	}

	public String transactionBlock(String previousHash, TransactionBlockChain transaction) {
		BlockBlockChain blockBlockChain = BlockBlockChain.init(Block.init())
									.addTransaction(transaction)
									.process(previousHash)
									.mine(Difficulty.getInstance().getDifficulty());
		addBlock(blockBlockChain);
		return blockBlockChain.getHash();
	}

	private void addBlock(BlockBlockChain newBlockBlockChain) {
		blockchain.add(newBlockBlockChain);
	}

	private void addGenesis(BlockBlockChain newBlockBlockChain) {
		this.genesis = newBlockBlockChain;
	}

	private BlockBlockChain getGenesis() {
		return this.genesis;
	}

	
	public void isChainValid() {
		List<TransactionOperationBlockChain> tempTransactionsOutputs = new ArrayList<>();

		BlockBlockChain blockBlockChainGenesis = this.getGenesis();

		tempTransactionsOutputs.addAll(blockBlockChainGenesis.getTransactionOutputs());
		String previousHash = blockBlockChainGenesis.getHash();

		for(BlockBlockChain currentBlockBlockChain : this.blockchain) {

			currentBlockBlockChain.isConsistent(previousHash, Difficulty.getInstance().getDifficulty());

			List<TransactionBlockChain> currentBlockTransactions = currentBlockBlockChain.getTransactionBlockChains();

			for(TransactionBlockChain transactionBlockChain : currentBlockTransactions) {
				transactionBlockChain.isConsistent();

				List<TransactionOperationBlockChain> transactionInputs = transactionBlockChain.getUnspentTransactionsOperationBlockChain();

				for(TransactionOperationBlockChain output : transactionInputs) {
					TransactionOperationBlockChain transactionOperationBlockChainFromOutside = tempTransactionsOutputs.stream().filter(outputTemp -> outputTemp.equals(output)).findFirst().orElse(null);
					output.isConsistent(transactionOperationBlockChainFromOutside);
				}

				for(TransactionOperationBlockChain output : transactionInputs) {
					tempTransactionsOutputs.remove(output);
				}
			}
			for(TransactionOperationBlockChain output: currentBlockBlockChain.getTransactionOutputs()) {
				tempTransactionsOutputs.add(output);
			}
			previousHash = currentBlockBlockChain.getHash();
		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}

}
