package br.com.lucianoyamane.example;


import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
//import java.util.Base64;
import java.util.HashMap;
//import com.google.gson.GsonBuilder;


public class NoobChain {
	
	public static ArrayList<Block> blockchain = new ArrayList();
	public static HashMap<String,TransactionOutput> UTXOs = new HashMap();
	
	public static int difficulty = 5;

	public static Wallet walletA;
	public static Wallet walletB;
	public static Transaction genesisTransaction;

	public static void main(String[] args) {	
		//add our blocks to the blockchain ArrayList:
//		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider
		
		//Create wallets:
		walletA = new Wallet();
		walletB = new Wallet();		
		Wallet coinbase = new Wallet();

//		genesisTransaction = coinbase.sendFunds(walletA.getPublicKey(), 100f);
		
		//create genesis transaction, which sends 100 NoobCoin to walletA:
		genesisTransaction = Transaction.genesis(coinbase.getPublicKeyDecorator(), walletA.getPublicKeyDecorator(), 100f);
		walletA.createSignatureTransaction(genesisTransaction);
		
		System.out.println("Creating and Mining Genesis block... ");
		Block genesis = Block.genesis();
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);
		
		//testing
		Block block1 = Block.init(genesis.getHash());
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(NoobChain.UTXOs));
		System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.getPublicKeyDecorator(), 40f));
		addBlock(block1);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(NoobChain.UTXOs));
		System.out.println("WalletB's balance is: " + walletB.getBalance(NoobChain.UTXOs));
		
		Block block2 = Block.init(block1.getHash());
		System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.getPublicKeyDecorator(), 1000f));
		addBlock(block2);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(NoobChain.UTXOs));
		System.out.println("WalletB's balance is: " + walletB.getBalance(NoobChain.UTXOs));
		
		Block block3 = Block.init(block2.getHash());
		System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds( walletA.getPublicKeyDecorator(), 20));
		addBlock(block3);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(NoobChain.UTXOs));
		System.out.println("WalletB's balance is: " + walletB.getBalance(NoobChain.UTXOs));

		Block block4 = Block.init(block3.getHash());
		System.out.println("\nWalletB is Attempting to send funds (10) to WalletA...");
		block4.addTransaction(walletB.sendFunds( walletA.getPublicKeyDecorator(), 10));
		addBlock(block4);
		System.out.println("\nWalletA's balance is: " + walletA.getBalance(NoobChain.UTXOs));
		System.out.println("WalletB's balance is: " + walletB.getBalance(NoobChain.UTXOs));
		
		isChainValid();
		
	}
	
	public static Boolean isChainValid() {
		Block currentBlock; 
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).getId(), genesisTransaction.outputs.get(0));
		
		//loop through blockchain to check hashes:
		for(int i = 1; i < blockchain.size(); i++) {
			
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i-1);
			//compare registered hash and calculated hash:
			if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
				System.out.println("#Current Hashes not equal");
				return false;
			}
			//compare previous hash and registered previous hash
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
				System.out.println("#Previous Hashes not equal");
				return false;
			}
			//check if hash is solved
			if(!currentBlock.getHash().substring( 0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}
			
			//loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for(int t=0; t <currentBlock.getTransactions().size(); t++) {
				Transaction currentTransaction = currentBlock.getTransactions().get(t);
				
				currentTransaction.verifiySignature();

				if(currentTransaction.getInputValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false; 
				}

				tempOutput = tempUTXOs.get(currentTransaction.input);

				if(tempOutput == null) {
					System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
					return false;
				}

				if(currentTransaction.input.getUTXO().getValue() != tempOutput.getValue()) {
					System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
					return false;
				}

				tempUTXOs.remove(currentTransaction.input.getTransactionOutputId());

				
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
