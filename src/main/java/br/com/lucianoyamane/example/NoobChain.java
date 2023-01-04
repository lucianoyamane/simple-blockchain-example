package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.transactions.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.List;


public class NoobChain {
	
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

	public static void main(String[] args) {	

		//Create wallets:
		Wallet walletA = Wallet.create("A");
		Wallet walletB = Wallet.create("B");
		Wallet coinbase = Wallet.create("Genesis");
		
		//create genesis transaction, which sends 100 NoobCoin to walletA:
		String genesisHash = bootstrapBlock(coinbase, walletA);

		//testing
		Block block1 = Block.init(genesisHash);
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
		List<TransactionOutput> tempTransactionsOutputs = new ArrayList<>();

		for(int i = 1; i < blockchain.size(); i++) {
			
			Block currentBlock = blockchain.get(i);
			Block previousBlock = blockchain.get(i - 1);

			if (previousBlock.isGenesis()) {
				List<TransactionOutput> unspentTransactions = previousBlock.getTransactionOutputs();
				for(TransactionOutput transactionOutput : unspentTransactions) {
					tempTransactionsOutputs.add(transactionOutput);
				}
			}

			if (!currentBlock.isConsistent(previousBlock.getHash(), difficulty)) {
				return false;
			}

			List<Transaction> currentBlockTransactions = currentBlock.getTransactions();

			for(int t = 0; t < currentBlockTransactions.size(); t++) {
				Transaction currentTransaction = currentBlockTransactions.get(t);

				if(!currentTransaction.verifiySignature() || !currentTransaction.isConsistent() || !currentTransaction.isReceiverOutputConsistent() || !currentTransaction.isSenderOutputConsistent()) {
					return false; 
				}

				List<TransactionInput> transactionInputs = currentTransaction.getInputs();

				for(TransactionInput input : transactionInputs) {
					TransactionOutput transactionOutputFromOutside = tempTransactionsOutputs.stream().filter(output -> output.equals(input.getUnspentTransaction())).findFirst().orElse(null);
					if (!input.isConsistent(transactionOutputFromOutside)) {
						return false;
					}
				}

				for(TransactionInput input : transactionInputs) {
					tempTransactionsOutputs.remove(input.getUnspentTransaction());
				}
				for(TransactionOutput output: currentTransaction.getOutputs()) {
					tempTransactionsOutputs.add(output);
				}
			}
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	

}
