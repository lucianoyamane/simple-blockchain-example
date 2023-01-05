package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.transactions.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class BlockChain {
	
	public static List<Block> blockchain = new ArrayList();
	public static int difficulty = 5;

	private static String bootstrapBlock(Wallet baseWallet, Wallet receiverWallet) {
		Transaction transaction = Transaction.genesis(baseWallet.getPublicKeyDecorator(), receiverWallet.getPublicKeyDecorator(), 10000);
		receiverWallet.createSignatureTransaction(transaction);
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = Block.genesis();
		genesis.addTransaction(transaction);
		addBlock(genesis);
		return genesis.getHash();
	}

	private static String transactionBlock(String previousHash, Wallet senderWallet, Wallet receiverWallet, Integer value) {
		Block block = Block.init(previousHash);
		System.out.println("\nWallet's " + senderWallet.getName() + " balance is: " + senderWallet.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("\nWallet " + senderWallet.getName() + " is Attempting to send funds (" + value + ") to Wallet " + receiverWallet.getName());
		block.addTransaction(senderWallet.sendFunds(receiverWallet.getPublicKeyDecorator(), value));
		mine(block);
		addBlock(block);
		System.out.println("\nWallet's " + senderWallet.getName() + " balance is: " + senderWallet.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("Wallet's " + receiverWallet.getName() + " balance is: " + receiverWallet.getBalance(UnspentTransactions.getInstance().get()));
		return block.getHash();
	}

	public static void main(String[] args) {	

		Wallet walletA = Wallet.create("A");
		Wallet walletB = Wallet.create("B");
		Wallet coinbase = Wallet.create("Genesis");

		String genesisHash = bootstrapBlock(coinbase, walletA);

		String block1Hash = transactionBlock(genesisHash, walletA, walletB, 4000);
//		isChainValid();

//		String block2Hash = transactionBlock(block1Hash, walletA, walletB, 100000);
//		isChainValid();
//
		String block3Hash = transactionBlock(block1Hash,walletB, walletA, 2000);
		isChainValid();
//
//		transactionBlock(block3Hash,walletB, walletA, 1000);
//		isChainValid();
		
	}

	public static void addBlock(Block newBlock) {
		blockchain.add(newBlock);
	}

	public static void mine(Block newBlock) {
		newBlock.mineBlock(difficulty);
	}
	
	public static void isChainValid() {
		List<TransactionOutput> tempTransactionsOutputs = new ArrayList<>();

		for(int i = 1; i < blockchain.size(); i++) {
			
			Block currentBlock = blockchain.get(i);
			Block previousBlock = blockchain.get(i - 1);

			if (previousBlock.isGenesis()) {
				tempTransactionsOutputs.addAll(previousBlock.getTransactionOutputs());
			}
			currentBlock.isConsistent(previousBlock.getHash(), difficulty);

			List<Transaction> currentBlockTransactions = currentBlock.getTransactions();

			for(Transaction transaction : currentBlockTransactions) {
				transaction.isConsistent();

				List<TransactionInput> transactionInputs = transaction.getInputs();

				for(TransactionInput input : transactionInputs) {
					TransactionOutput transactionOutputFromOutside = tempTransactionsOutputs.stream().filter(output -> output.equals(input.getUnspentTransaction())).findFirst().orElse(null);
					input.isConsistent(transactionOutputFromOutside);
				}

				for(TransactionInput input : transactionInputs) {
					tempTransactionsOutputs.remove(input.getUnspentTransaction());
				}
			}

			for(TransactionOutput output: currentBlock.getTransactionOutputs()) {
				tempTransactionsOutputs.add(output);
			}
		}
		System.out.println("Blockchain is valid");
	}
	

}
