package br.com.lucianoyamane.example.entity;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.transaction.TransactionInput;
import br.com.lucianoyamane.example.transaction.TransactionOutput;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain {

    private String fingerPrint;

    private byte[] signature;

    private Transaction transaction;

    private TransactionBlockChain(Transaction transaction) {
        this.transaction = transaction;
        this.setFingerPrint(this.createFingerPrint());
    }

    public static TransactionBlockChain create(Transaction transaction) {
        return new TransactionBlockChain(transaction);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    private void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    private String createFingerPrint() {
        return StringUtil.encode(this.getData());
    }

    private String getData() {
        return new StringBuilder(UUID.randomUUID().toString())
                .append(this.getTransaction().getSenderPublicKeyDecorator().toString())
                .append(this.getTransaction().getReceiverPublickeyDecorator().toString())
                .append(this.getTransaction().getValue())
                .toString();
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    private byte[] getSignature() {
        return this.signature;
    }

    public boolean processTransaction() {
        Boolean signatureVerified = this.verifiySignature();
        if (signatureVerified) {
            this.removeCurrentOutput();
            this.addCurrentTransactionOutput();
            this.addLeftOverTransactionOutput();
        }
        return signatureVerified;
    }

    private Boolean verifiySignature() {
        return StringUtil.verifyECDSASig(this.transaction.getSenderPublicKeyDecorator().getPublicKey(), this.getFingerPrint(), this.getSignature());
    }

    private void removeCurrentOutput() {
        for(TransactionInput transactionInput : this.transaction.getInputs()) {
            UnspentTransactions.getInstance().remove(transactionInput.getUnspentTransaction());
        }
    }

    private void addCurrentTransactionOutput() {
        TransactionOutput current = TransactionOutput.current( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.transaction.setSenderTransactionOutput(current);
        this.addUnspentTransaction(current);
    }

    private void addLeftOverTransactionOutput() {
        TransactionOutput leftover = TransactionOutput.leftover( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.transaction.setReceiverTransactionOutput(leftover);
        this.addUnspentTransaction(leftover);
    }

    private void addUnspentTransaction(TransactionOutput transactionOutput) {
        UnspentTransactions.getInstance().add(transactionOutput);
    }

    public Integer getLeftOverValue() {
        return this.getInputValue() - this.getTransaction().getValue();
    }

    public Integer getInputValue() {
        List<TransactionInput> inputs = this.getTransaction().getInputs();
        return inputs.stream().mapToInt(input -> input.getUnspentTransaction().getValue()).sum();
    }

    public Boolean isInputEqualOutputValue() {
        return this.getInputValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getTransaction().getSenderTransactionOutput().getValue() + this.getTransaction().getReceiverTransactionOutput().getValue();
    }

    public void isConsistent() {
        if (!this.verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }

        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getFingerPrint() + ")");
        }

        TransactionOutput senderTransactionOutput = this.getTransaction().getSenderTransactionOutput();
        if (!senderTransactionOutput.isMine(this.getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + senderTransactionOutput + ") is not who it should be");
        }

        TransactionOutput receiverTransactionOutput = this.getTransaction().getReceiverTransactionOutput();
        if (!receiverTransactionOutput.isMine(this.getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + receiverTransactionOutput + ") is not who it should be");
        }
    }

}
