package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.transactions.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
import java.util.List;


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
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i = 1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);

			if (!currentBlock.isConsistent(previousBlock.getHash(), difficulty)) {
				return false;
			}

			List<Transaction> currentBlockTransactions = currentBlock.getTransactions();

			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t = 0; t < currentBlockTransactions.size(); t++) {
				Transaction currentTransaction = currentBlockTransactions.get(t);
				
				currentTransaction.verifiySignature();

				if(!currentTransaction.isConsistent()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}

				TransactionInput transactionInput = currentTransaction.input;

				tempOutput = tempUTXOs.get(transactionInput.getUnspentTransactionId());

				if(tempOutput == null) {
					System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
					return false;
				}

				if(transactionInput.getUnspentTransaction().getValue() != tempOutput.getValue()) {
					System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
					return false;
				}

				tempUTXOs.remove(transactionInput.getUnspentTransactionId());

				
				for(TransactionOutput output: currentTransaction.outputs) {
					tempUTXOs.put(output.getId(), output);
				}
				
				if( currentTransaction.outputs.get(0).getReceiverPublicKey() != currentTransaction.getReceiverPublicKey()) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if( currentTransaction.outputs.get(1).getReceiverPublicKey() != currentTransaction.getSenderPublicKey()) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}
				
			}
			
		}
		System.out.println("Blockchain is valid");
		return true;
	}
	
	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}
