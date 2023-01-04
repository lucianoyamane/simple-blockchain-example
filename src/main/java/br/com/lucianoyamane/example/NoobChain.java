package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.transactions.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


public class NoobChain {
	
	public static ArrayList<Block> blockchain = new ArrayList();
	public static int difficulty = 5;
	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

	public static void main(String[] args) {	

		//Create wallets:
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();
		
		//create genesis transaction, which sends 100 NoobCoin to walletA:
		genesisTransaction = Transaction.genesis(coinbase.getPublicKeyDecorator(), walletA.getPublicKeyDecorator(), 10000);
		walletA.createSignatureTransaction(genesisTransaction);
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = Block.genesis();
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		//testing
		Block block1 = Block.init(genesis.getHash());
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.getPublicKeyDecorator(), 4000));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("WalletB's balance is: " + walletB.getBalance(UnspentTransactions.getInstance().get()));
		
		Block block2 = Block.init(block1.getHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.getPublicKeyDecorator(), 100000));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("WalletB's balance is: " + walletB.getBalance(UnspentTransactions.getInstance().get()));
		
		Block block3 = Block.init(block2.getHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.getPublicKeyDecorator(), 2000));
		addBlock(block3);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("WalletB's balance is: " + walletB.getBalance(UnspentTransactions.getInstance().get()));

		Block block4 = Block.init(block3.getHash());
		System.out.println("\nWalletB is Attempting to send funds (10) to WalletA...");
		block4.addTransaction(walletB.sendFunds( walletA.getPublicKeyDecorator(), 1000));
		addBlock(block4);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(UnspentTransactions.getInstance().get()));
		System.out.println("WalletB's balance is: " + walletB.getBalance(UnspentTransactions.getInstance().get()));
		
		isChainValid();
		
	}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
	
	public static Boolean isChainValid() {
		List<TransactionOutput> tempTransactionsOutputs = new ArrayList<>(); //a temporary working list of unspent transactions at a given block state.
		tempTransactionsOutputs.add(genesisTransaction.getOutputs().get(0));
		
		//loop through blockchain to check hashes:
		for(int i = 1; i < blockchain.size(); i++) {
			
			Block currentBlock = blockchain.get(i);
			Block previousBlock = blockchain.get(i - 1);

			if (!currentBlock.isConsistent(previousBlock.getHash(), difficulty)) {
				return false;
			}

			List<Transaction> currentBlockTransactions = currentBlock.getTransactions();

			for(int t = 0; t < currentBlockTransactions.size(); t++) {
				Transaction currentTransaction = currentBlockTransactions.get(t);

				if(!currentTransaction.verifiySignature() || !currentTransaction.isConsistent() || !currentTransaction.isReceiverOutputConsistent() || !currentTransaction.isSenderOutputConsistent()) {
					return false; 
				}

				TransactionInput transactionInput = currentTransaction.getInput();
				TransactionOutput transactionOutputFromOutside = tempTransactionsOutputs.stream().filter(output -> output.equals(transactionInput.getUnspentTransaction())).findFirst().orElse(null);
				if (!transactionInput.isConsistent(transactionOutputFromOutside)) {
					return false;
				}
				tempTransactionsOutputs.remove(transactionInput.getUnspentTransaction());
				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempTransactionsOutputs.add(output);
				}
			}
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	

}
