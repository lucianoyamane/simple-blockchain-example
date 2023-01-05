package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.*;
import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transactions.UnspentTransactions;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public class Wallet {

	private KeyPair keyPair;
	private PublicKeyDecorator publicKeyDecorator;

	private String name;

	public static Wallet create(String name) {
		return new Wallet(name);
	}

    private Wallet(String name){
		this.setName(name);
		this.setKeyPair(BouncyCastleKeyPair.init().getKeyPair());
	}

	private void setKeyPair(KeyPair keyPair) {
		this.keyPair = keyPair;
		this.setPublicKeyDecorator(this.keyPair.getPublic());
	}

	private void setPublicKeyDecorator(PublicKey publicKey) {
		this.publicKeyDecorator = PublicKeyDecorator.inicia(publicKey);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
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

	public List<TransactionOutput> getUnspentUTXO(List<TransactionOutput> UTXOs) {
		return UTXOs.stream()
				.filter(output -> output.isMine(this.getPublicKeyDecorator())).toList();
	}

	public void createSignatureTransaction(Transaction transaction) {
		transaction.setSignature(StringUtil.applyECDSASig(this.getPrivateKey(), transaction.getData()));
	}
	public Transaction sendFunds(String nameReceiver, PublicKeyDecorator receiverPublicKeyDecorator, Integer value ) {
		List<TransactionOutput> unspentTransactionOutputs = this.getUnspentUTXO(UnspentTransactions.getInstance().get());
		List<TransactionInput> inputs = unspentTransactionOutputs.stream().map(TransactionInput::create).toList();

		Transaction newTransaction = Transaction.create(this.getPublicKeyDecorator(), receiverPublicKeyDecorator , value, inputs, this.getName(), nameReceiver);
		if (this.getBalance(unspentTransactionOutputs) < value) {
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}

		this.createSignatureTransaction(newTransaction);

		return newTransaction;
	}
}
