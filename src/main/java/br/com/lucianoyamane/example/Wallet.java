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
            	total += UTXO.getValue() ;
            }
        }
		return total;
	}

	public TransactionOutput getUnspentUTXO(Map<String, TransactionOutput> UTXOs) {
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(this.getPublicKey())) {
				return UTXO;
			}
		}
		return null;
	}
	public Transaction sendFunds(PublicKey _recipient, float value ) {
		TransactionOutput UTXO = this.getUnspentUTXO(NoobChain.UTXOs);

		if(UTXO.getValue() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		TransactionInput input = TransactionInput.create(UTXO.getId(), UTXO);

		Transaction newTransaction = Transaction.create(this.getPublicKey(), _recipient , value, input);

		String data = newTransaction.getData();
		newTransaction.setSignature(StringUtil.applyECDSASig(this.getPrivateKey(), data));


		return newTransaction;
	}
}
