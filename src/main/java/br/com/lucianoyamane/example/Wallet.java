package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Wallet {

	private KeyPair keyPair;
    
    public Wallet(){
		this.keyPair = BouncyCastleKeyPair.init().getKeyPair();
	}

	public PublicKey getPublicKey() {
		return this.keyPair.getPublic();
	}

	public PrivateKey getPrivateKey() {
		return this.keyPair.getPrivate();
	}

    public float getBalance() {
		float total = 0;
        for (Map.Entry<String, TransactionOutput> item: NoobChain.UTXOs.entrySet()){
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(this.getPublicKey())) {
            	total += UTXO.value ;
            }
        }
		return total;
	}

	public float getBalance(Map<String, TransactionOutput> unspentUTXOs) {
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: unspentUTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(this.getPublicKey())) {
				total += UTXO.value ;
			}
		}
		return total;
	}

	public Map<String,TransactionOutput> getUnspentUTXOs(Map<String, TransactionOutput> UTXOs) {
		Map<String, TransactionOutput> unspentUTXOs = new HashMap<>();
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(this.getPublicKey())) {
				unspentUTXOs.put(UTXO.id, UTXO);
			}
		}
		return unspentUTXOs;
	}

	//Generates and returns a new transaction from this wallet.
	public Transaction sendFunds(PublicKey _recipient, float value ) {
		Map<String, TransactionOutput> UTXOs = this.getUnspentUTXOs(NoobChain.UTXOs);

		if(getBalance(UTXOs) < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
    //create array list of inputs
		List<TransactionInput> inputs = new ArrayList();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}

		Transaction newTransaction = new Transaction(this.getPublicKey(), _recipient , value, inputs);
		newTransaction.generateSignature(this.getPrivateKey());

		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
}
