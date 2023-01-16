package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperation;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class BlockChainApp {

	private List<Block> blockchain;

	private BlockChainApp() {
		this.blockchain = new ArrayList();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String genesisBlock(TransactionBlockChain transaction) {
		Block genesis = Block.genesis()
								.addTransaction(transaction)
								.processGenesis()
								.mine(Difficulty.getInstance().getDifficulty());
		addBlock(genesis);
		return genesis.getHash();
	}

	public String transactionBlock(String previousHash, TransactionBlockChain transaction) {
		Block block = Block.init();
		block.addTransaction(transaction);
		block.process(previousHash);
		block.mine(Difficulty.getInstance().getDifficulty());
		addBlock(block);
		return block.getHash();
	}

	public void addBlock(Block newBlock) {
		blockchain.add(newBlock);
	}

	
	public void isChainValid() {
		List<TransactionOperation> tempTransactionsOutputs = new ArrayList<>();

		for(int i = 1; i < blockchain.size(); i++) {
			
			Block currentBlock = blockchain.get(i);
			Block previousBlock = blockchain.get(i - 1);

			if (previousBlock.isGenesis()) {
				tempTransactionsOutputs.addAll(previousBlock.getTransactionOutputs());
			}
			isConsistent(currentBlock, previousBlock.getHash(), Difficulty.getInstance().getDifficulty());

			List<TransactionBlockChain> currentBlockTransactions = currentBlock.getTransactions();

			for(TransactionBlockChain transaction : currentBlockTransactions) {
				transaction.isConsistent();

				List<TransactionOperation> transactionInputs = transaction.getTransaction().getUnspentTransactions();

				for(TransactionOperation output : transactionInputs) {
					TransactionOperation transactionOperationFromOutside = tempTransactionsOutputs.stream().filter(outputTemp -> outputTemp.equals(output)).findFirst().orElse(null);
					isConsistent(output, transactionOperationFromOutside);
				}

				for(TransactionOperation output : transactionInputs) {
					tempTransactionsOutputs.remove(output);
				}
			}
			for(TransactionOperation output: currentBlock.getTransactionOutputs()) {
				tempTransactionsOutputs.add(output);
			}
		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}

	private static void isConsistent(Block block, String previousHash, int difficulty) {
		if (!block.compareRegisteredAndCalculatedHash()) {
			throw new BlockChainException("Current Hashes not equal");
		}
		if (!block.compareHash(previousHash)) {
			throw new BlockChainException("Previous Hashes not equal");
		}
		if(!block.hashIsSolved(difficulty)) {
			throw new BlockChainException("This block hasn't been mined");
		}
	}



	private static void isConsistent(TransactionOperation transactionOperation, TransactionOperation referenceTransactionOperation) {
		if(referenceTransactionOperation == null) {
			throw new BlockChainException("#Referenced input on Transaction(" + transactionOperation.toString() + ") is Missing");
		}
		if(transactionOperation.getValue() != referenceTransactionOperation.getValue()) {
			throw new BlockChainException("#Referenced input Transaction(" + transactionOperation + ") value is Invalid");
		}
	}

	

}
