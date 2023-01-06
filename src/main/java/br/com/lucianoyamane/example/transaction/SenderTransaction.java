package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class SenderTransaction {

    private PublicKeyDecorator publicKey; // senders address/public key.

    private TransactionOutput transactionOutput;

    private String name;

    public static SenderTransaction create(PublicKeyDecorator publicKeyDecorator, String name) {
        return new SenderTransaction(publicKeyDecorator, name);
    }

    private SenderTransaction(PublicKeyDecorator publicKey, String name) {
        this.setPublicKey(publicKey);
        this.setName(name);
    }

    public PublicKeyDecorator getPublicKey() {
        return publicKey;
    }

    public String getPublicKeyString() {
        return publicKey.toString();
    }

    private void setPublicKey(PublicKeyDecorator publicKey) {
        this.publicKey = publicKey;
    }

    public TransactionOutput getTransactionOutput() {
        return transactionOutput;
    }

    public void setTransactionOutput(TransactionOutput transactionOutput) {
        this.transactionOutput = transactionOutput;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

}
