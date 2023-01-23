package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperation;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class BlockChainApp {

	private List<BlockBlockChain> blockchain;

	private BlockChainApp() {
		this.blockchain = new ArrayList();
	}

	public static BlockChainApp create() {
		return new BlockChainApp();
	}

	public String genesisBlock(TransactionBlockChain transaction) {
		SystemOutPrintlnDecorator.ciano("******************************************************");
		SystemOutPrintlnDecorator.ciano("Creating and Mining Genesis block... ");
		BlockBlockChain genesis = BlockBlockChain.init(Block.init())
								.addTransaction(transaction)
								.processGenesis()
								.mine(Difficulty.getInstance().getDifficulty());
		addBlock(genesis);
		return genesis.getHash();
	}

	public String transactionBlock(String previousHash, TransactionBlockChain transaction) {
		SystemOutPrintlnDecorator.ciano("******************************************************");
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

	
	public void isChainValid() {
		List<TransactionOperationBlockChain> tempTransactionsOutputs = new ArrayList<>();

		for(int i = 1; i < blockchain.size(); i++) {
			
			BlockBlockChain currentBlockBlockChain = blockchain.get(i);
			BlockBlockChain previousBlockBlockChain = blockchain.get(i - 1);

			if (previousBlockBlockChain.isGenesis()) {
				tempTransactionsOutputs.addAll(previousBlockBlockChain.getTransactionOutputs());
			}
			currentBlockBlockChain.isConsistent(previousBlockBlockChain.getHash(), Difficulty.getInstance().getDifficulty());

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
		}
		SystemOutPrintlnDecorator.roxo("Blockchain is valid");
	}







	

}
