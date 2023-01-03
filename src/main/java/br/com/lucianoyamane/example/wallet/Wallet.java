package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.*;
import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
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

    public Integer getBalance(List<TransactionOutput> UTXOs) {
		return UTXOs.stream()
				.filter(output -> output.isMine(this.getPublicKeyDecorator()))
				.mapToInt(output -> output.getValue()).sum();
	}

	public TransactionOutput getUnspentUTXO(List<TransactionOutput> UTXOs) {
		return UTXOs.stream()
				.filter(output -> output.isMine(this.getPublicKeyDecorator())).findFirst().orElse(null);
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
