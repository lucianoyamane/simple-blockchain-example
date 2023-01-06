package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class ReceiverTransaction {

    private PublicKeyDecorator publicKey; // senders address/public key.

    private TransactionOutput transactionOutput;

    private String name;

    public PublicKeyDecorator getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKeyDecorator publicKey) {
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

    public void setName(String name) {
        this.name = name;
    }

    public String getPublicKeyString() {
        return publicKey.toString();
    }

}
