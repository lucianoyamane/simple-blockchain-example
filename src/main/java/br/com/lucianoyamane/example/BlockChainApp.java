package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.transaction.TransactionInput;
import br.com.lucianoyamane.example.transaction.TransactionOutput;

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
		List<TransactionOutput> tempTransactionsOutputs = new ArrayList<>();

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

				List<TransactionInput> transactionInputs = transaction.getTransaction().getInputs();

				for(TransactionInput input : transactionInputs) {
					TransactionOutput transactionOutputFromOutside = tempTransactionsOutputs.stream().filter(output -> output.equals(input.getUnspentTransaction())).findFirst().orElse(null);
					isConsistent(input, transactionOutputFromOutside);
				}

				for(TransactionInput input : transactionInputs) {
					tempTransactionsOutputs.remove(input.getUnspentTransaction());
				}
			}
			for(TransactionOutput output: currentBlock.getTransactionOutputs()) {
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



	private static void isConsistent(TransactionInput transactionInput, TransactionOutput referenceTransactionOutput) {
		if(referenceTransactionOutput == null) {
			throw new BlockChainException("#Referenced input on Transaction(" + transactionInput.getUnspentTransaction().toString() + ") is Missing");
		}
		if(transactionInput.getUnspentTransaction().getValue() != referenceTransactionOutput.getValue()) {
			throw new BlockChainException("#Referenced input Transaction(" + transactionInput.getUnspentTransaction().toString() + ") value is Invalid");
		}
	}

	

}
