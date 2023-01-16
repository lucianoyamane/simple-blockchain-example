package br.com.lucianoyamane.example.entity;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transaction.Transaction;
import br.com.lucianoyamane.example.transaction.TransactionOperation;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain {

    private Transaction transaction;

    private TransactionBlockChain(Transaction transaction) {
        this.transaction = transaction;
        transaction.setFingerPrint(this.createFingerPrint());
    }

    public static TransactionBlockChain create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
        return new TransactionBlockChain(Transaction.create(senderPublicKeyDecorator, receiverPublickeyDecorator, value, createInputs(senderPublicKeyDecorator)));
    }

    private static List<TransactionOperation> createInputs(PublicKeyDecorator senderPublicKeyDecorator) {
        return UnspentTransactions.getInstance().loadUnspentUTXO(senderPublicKeyDecorator);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    private String createFingerPrint() {
        return StringUtil.encode(this.getData());
    }

    public String getFingerPrint() {
        return this.transaction.getFingerPrint();
    }

    private String getData() {
        return new StringBuilder(UUID.randomUUID().toString())
                .append(this.getTransaction().getSenderPublicKeyDecorator().toString())
                .append(this.getTransaction().getReceiverPublickeyDecorator().toString())
                .append(this.getTransaction().getValue())
                .toString();
    }

    public void setSignature(byte[] signature) {
        this.transaction.setSignature(signature);
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
        return StringUtil.verifyECDSASig(this.transaction.getSenderPublicKeyDecorator().getPublicKey(), this.transaction.getFingerPrint(), this.transaction.getSignature());
    }

    private void removeCurrentOutput() {
        for(TransactionOperation transactionOperation : this.transaction.getUnspentTransactions()) {
            UnspentTransactions.getInstance().remove(transactionOperation);
        }
    }

    private void addCurrentTransactionOutput() {
        TransactionOperation current = TransactionOperation.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.transaction.setSenderTransactionOutput(current);
        this.addUnspentTransaction(current);
    }

    private void addLeftOverTransactionOutput() {
        TransactionOperation leftover = TransactionOperation.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.transaction.setReceiverTransactionOutput(leftover);
        this.addUnspentTransaction(leftover);
    }

    private void addUnspentTransaction(TransactionOperation transactionOperation) {
        UnspentTransactions.getInstance().add(transactionOperation);
    }

    public Integer getLeftOverValue() {
        return this.getInputValue() - this.getTransaction().getValue();
    }

    public Integer getInputValue() {
        List<TransactionOperation> outputs = this.getTransaction().getUnspentTransactions();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
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
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.transaction.getFingerPrint() + ")");
        }

        TransactionOperation senderTransactionOperation = this.getTransaction().getSenderTransactionOutput();
        if (!senderTransactionOperation.isMine(this.getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + senderTransactionOperation + ") is not who it should be");
        }

        TransactionOperation receiverTransactionOperation = this.getTransaction().getReceiverTransactionOutput();
        if (!receiverTransactionOperation.isMine(this.getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + receiverTransactionOperation + ") is not who it should be");
        }
    }

}
