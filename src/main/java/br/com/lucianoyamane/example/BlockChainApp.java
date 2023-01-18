package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.BlockEntity;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperation;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class BlockChainApp {

	private List<BlockEntity> blockchain;

	private BlockChainApp() {
		this.blockchain = new ArrayList();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String genesisBlock(TransactionBlockChain transaction) {
		SystemOutPrintlnDecorator.ciano("******************************************************");
		SystemOutPrintlnDecorator.ciano("Creating and Mining Genesis block... ");
		BlockEntity genesis = BlockEntity.init()
								.addTransaction(transaction)
								.processGenesis()
								.mine(Difficulty.getInstance().getDifficulty());
		addBlock(genesis);
		return genesis.getHash();
	}

	public String transactionBlock(String previousHash, TransactionBlockChain transaction) {
		SystemOutPrintlnDecorator.ciano("******************************************************");
		BlockEntity blockEntity = BlockEntity.init()
									.addTransaction(transaction)
									.process(previousHash)
									.mine(Difficulty.getInstance().getDifficulty());
		addBlock(blockEntity);
		return blockEntity.getHash();
	}

	private void addBlock(BlockEntity newBlockEntity) {
		blockchain.add(newBlockEntity);
	}

	
	public void isChainValid() {
		List<TransactionOperation> tempTransactionsOutputs = new ArrayList<>();

		for(int i = 1; i < blockchain.size(); i++) {
			
			BlockEntity currentBlockEntity = blockchain.get(i);
			BlockEntity previousBlockEntity = blockchain.get(i - 1);

			if (previousBlockEntity.isGenesis()) {
				tempTransactionsOutputs.addAll(previousBlockEntity.getTransactionOutputs());
			}
			isConsistent(currentBlockEntity, previousBlockEntity.getHash(), Difficulty.getInstance().getDifficulty());

			List<TransactionBlockChain> currentBlockTransactions = currentBlockEntity.getTransactions();

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
			for(TransactionOperation output: currentBlockEntity.getTransactionOutputs()) {
				tempTransactionsOutputs.add(output);
			}
		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}

	private static void isConsistent(BlockEntity blockEntity, String previousHash, int difficulty) {
		if (!blockEntity.compareRegisteredAndCalculatedHash()) {
			throw new BlockChainException("Current Hashes not equal");
		}
		if (!blockEntity.compareHash(previousHash)) {
			throw new BlockChainException("Previous Hashes not equal");
		}
		if(!blockEntity.hashIsSolved(difficulty)) {
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
