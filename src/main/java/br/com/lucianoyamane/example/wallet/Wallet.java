package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.*;
import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class Wallet {

	private KeyPair keyPair;
	private PublicKeyDecorator publicKeyDecorator;
    
    public Wallet(){
		this.setKeyPair(BouncyCastleKeyPair.init().getKeyPair());
	}

	private void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
		this.setPublicKeyDecorator(this.keyPair.getPublic());
	}

	private void setPublicKeyDecorator(PublicKey publicKey) {
		this.publicKeyDecorator = PublicKeyDecorator.inicia(publicKey);
	}

	public PublicKeyDecorator getPublicKeyDecorator() {
		return publicKeyDecorator;
	}

	private PrivateKey getPrivateKey() {
		return this.keyPair.getPrivate();
	}

    public Integer getBalance(Map<String, TransactionOutput> UTXOs) {
		Integer total = 0;
        for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
        	TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(this.getPublicKeyDecorator())) {
            	total += UTXO.getValue() ;
            }
        }
		return total;
	}

	public TransactionOutput getUnspentUTXO(Map<String, TransactionOutput> UTXOs) {
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(this.getPublicKeyDecorator())) {
				return UTXO;
			}
		}
		return null;
	}

	public void createSignatureTransaction(Transaction transaction) {
		transaction.setSignature(StringUtil.applyECDSASig(this.getPrivateKey(), transaction.getData()));
	}
	public Transaction sendFunds(PublicKeyDecorator recipentPublicKeyDecorator, Integer value ) {
		TransactionOutput UTXO = this.getUnspentUTXO(NoobChain.UTXOs);

		if(UTXO.getValue() < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		TransactionInput input = TransactionInput.create(UTXO);
		Transaction newTransaction = Transaction.create(this.getPublicKeyDecorator(), recipentPublicKeyDecorator , value, input);
		this.createSignatureTransaction(newTransaction);

		return newTransaction;
	}
}
