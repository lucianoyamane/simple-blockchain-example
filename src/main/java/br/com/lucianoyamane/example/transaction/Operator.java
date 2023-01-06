package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class Operator {

    private PublicKeyDecorator publicKey;

    private TransactionOutput transactionOutput;

    private String name;

    public static Operator create(PublicKeyDecorator publicKey, String name) {
        return new Operator(publicKey, name);
    }

    private Operator(PublicKeyDecorator publicKey, String name) {
        this.setPublicKey(publicKey);
        this.setName(name);
    }

    public PublicKeyDecorator getPublicKey() {
        return publicKey;
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

    public String getPublicKeyString() {
        return publicKey.toString();
    }
}
