package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.TransactionInput;
import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.ArrayList;
import java.util.List;

public class TransactionBuilder {

    private String transactionId; // this is also the hash of the transaction.
    private PublicKeyDecorator senderPublicKey; // senders address/public key.
    private PublicKeyDecorator receiverPublicKey; // Recipients address/public key.
    private Integer value;
    private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
    private List<TransactionInput> inputsFromUnspentOutputs;
    private TransactionOutput currentTransactionOutput;
    private TransactionOutput leftOverTransactionOutput;
    private String owner;
    private String receiver;

    public TransactionBuilder(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value) {
        this.setSenderPublicKey(senderPublicKey);
        this.setReceiverPublicKey(receiverPublicKey);
        this.setValue(value);
        this.setInputsFromUnspentOutputs(new ArrayList<>());
    }

    public TransactionBuilder(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value, List<TransactionInput> inputsFromUnspentOutputs, String owner, String receiver) {
        this.setSenderPublicKey(senderPublicKey);
        this.setReceiverPublicKey(receiverPublicKey);
        this.setValue(value);
        this.setInputsFromUnspentOutputs(inputsFromUnspentOutputs);
        this.setOwner(owner);
        this.setReceiver(receiver);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKeyDecorator getSenderPublicKey() {
        return senderPublicKey;
    }

    public void setSenderPublicKey(PublicKeyDecorator senderPublicKey) {
        this.senderPublicKey = senderPublicKey;
    }

    public PublicKeyDecorator getReceiverPublicKey() {
        return receiverPublicKey;
    }

    public void setReceiverPublicKey(PublicKeyDecorator receiverPublicKey) {
        this.receiverPublicKey = receiverPublicKey;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public List<TransactionInput> getInputsFromUnspentOutputs() {
        return inputsFromUnspentOutputs;
    }

    public void setInputsFromUnspentOutputs(List<TransactionInput> inputsFromUnspentOutputs) {
        this.inputsFromUnspentOutputs = inputsFromUnspentOutputs;
    }

    public TransactionOutput getCurrentTransactionOutput() {
        return currentTransactionOutput;
    }

    public void setCurrentTransactionOutput(TransactionOutput currentTransactionOutput) {
        this.currentTransactionOutput = currentTransactionOutput;
    }

    public TransactionOutput getLeftOverTransactionOutput() {
        return leftOverTransactionOutput;
    }

    public void setLeftOverTransactionOutput(TransactionOutput leftOverTransactionOutput) {
        this.leftOverTransactionOutput = leftOverTransactionOutput;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
