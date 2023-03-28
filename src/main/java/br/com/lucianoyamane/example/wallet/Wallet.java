package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Wallet {

	private KeyPair keyPair;
	private PublicKeyDecorator publicKeyDecorator;

	public static Wallet anonymous() {
		return new Wallet();
	}

	protected Wallet() {
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

    public Integer getBalance() {
		return UnspentTransactions.getInstance().getWalletBalance(this.getPublicKeyDecorator());
	}

	protected void createSignatureTransaction(TransactionBlockChain transactionBlockChain) {
		transactionBlockChain.setSignature(StringUtil.applyECDSASig(this.getPrivateKey(), transactionBlockChain.getHash()));
	}

	public Boolean hasFunds(Integer value) {
		return this.getBalance() >= value;
	}
	public TransactionBlockChain sendFunds(PublicKeyDecorator receiverPublicKey, Integer value ) {
		if (!this.hasFunds(value)) {
			SystemOutPrintlnDecorator.vermelho("Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		TransactionBlockChain transactionBlockChain = TransactionBlockChain.create(this.getPublicKeyDecorator(), receiverPublicKey, value);
		createSignatureTransaction(transactionBlockChain);
		return transactionBlockChain;
	}

}
