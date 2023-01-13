package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
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

	public String genesisBlock(Transaction transaction) {
		Block genesis = Block.genesis()
								.addTransaction(transaction)
								.processGenesis()
								.mine(Difficulty.getInstance().getDifficulty());
		addBlock(genesis);
		return genesis.getHash();
	}

	public String transactionBlock(String previousHash, Transaction transaction) {
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

			List<Transaction> currentBlockTransactions = currentBlock.getTransactions();

			for(Transaction transaction : currentBlockTransactions) {
				isConsistent(transaction);

				List<TransactionInput> transactionInputs = transaction.getInputs();

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

	private static void isConsistent(Transaction transaction) {
		if (!transaction.verifiySignature()) {
			throw new BlockChainException("Transaction Signature failed to verify");
		}

		if (!transaction.isInputEqualOutputValue()) {
			throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + transaction.getHash() + ")");
		}

		TransactionOutput senderTransactionOutput = transaction.getSenderTransactionOutput();
		if (!senderTransactionOutput.isMine(transaction.getReceiverPublicKey())) {
			throw new BlockChainException("#TransactionOutput(" + senderTransactionOutput.getId() + ") is not who it should be");
		}

		TransactionOutput receiverTransactionOutput = transaction.getReceiverTransactionOutput();
		if (!receiverTransactionOutput.isMine(transaction.getSenderPublicKey())) {
			throw new BlockChainException("#TransactionOutput(" + receiverTransactionOutput.getId() + ") is not who it should be");
		}
	}

	private static void isConsistent(TransactionInput transactionInput, TransactionOutput referenceTransactionOutput) {
		if(referenceTransactionOutput == null) {
			throw new BlockChainException("#Referenced input on Transaction(" + transactionInput.getUnspentTransaction().getId() + ") is Missing");
		}
		if(transactionInput.getUnspentTransaction().getValue() != referenceTransactionOutput.getValue()) {
			throw new BlockChainException("#Referenced input Transaction(" + transactionInput.getUnspentTransaction().getId() + ") value is Invalid");
		}
	}

	

}
