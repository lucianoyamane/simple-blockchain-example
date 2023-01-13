package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.*;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.transaction.TransactionInput;
import br.com.lucianoyamane.example.transaction.TransactionOutput;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Wallet {

	private KeyPair keyPair;
	private PublicKeyDecorator publicKeyDecorator;
	private String name;

	public static Wallet novo(String name) {
		return new Wallet(name);
	}

    protected Wallet(String name){
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

    public Integer getBalance() {
		return UnspentTransactions.getInstance().getWalletBalance(this.getPublicKeyDecorator());
	}

	public byte[] createSignatureTransaction(String hash) {
		return StringUtil.applyECDSASig(this.getPrivateKey(), hash);
	}

	public Boolean hasFunds(Integer value) {
		return this.getBalance() >= value;
	}
	public TransactionBlockChain sendFunds(PublicData receiverPublicData, Integer value ) {
		SystemOutPrintlnDecorator.verde("\nWallet " + this.getName() + " is Attempting to send funds (" + value + ") to Wallet " + receiverPublicData.getName());
		if (!this.hasFunds(value)) {
			SystemOutPrintlnDecorator.vermelho("Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		List<TransactionOutput> unspentTransactionOutputs = UnspentTransactions.getInstance().loadUnspentUTXO(this.getPublicKeyDecorator());
		List<TransactionInput> inputs = unspentTransactionOutputs.stream().map(TransactionInput::create).toList();
		Transaction newTransaction = Transaction.create(this.toPublicData().getPublicKeyDecorator(), receiverPublicData.getPublicKeyDecorator(), value, inputs);
		TransactionBlockChain transactionBlockChain = TransactionBlockChain.create(newTransaction);
		transactionBlockChain.setSignature(createSignatureTransaction(transactionBlockChain.getFingerPrint()));
		return transactionBlockChain;
	}

	public PublicData toPublicData() {
		return PublicData.create(this.getPublicKeyDecorator(), this.getName());
	}
}
