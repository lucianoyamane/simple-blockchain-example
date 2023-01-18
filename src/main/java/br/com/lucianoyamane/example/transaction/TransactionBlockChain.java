package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain {

    private Transaction transaction;

    private TransactionBlockChain(Transaction transaction) {
        this.setTransaction(transaction);
        transaction.setFingerPrint(this.createFingerPrint());
    }

    public static TransactionBlockChain create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
        return new TransactionBlockChain(Transaction.create(senderPublicKeyDecorator, receiverPublickeyDecorator, value, createInputs(senderPublicKeyDecorator)));
    }

    private static List<TransactionOperationBlockChain> createInputs(PublicKeyDecorator senderPublicKeyDecorator) {
        return UnspentTransactions.getInstance().loadUnspentUTXO(senderPublicKeyDecorator);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    private String createFingerPrint() {
        return StringUtil.encode(this.getData());
    }

    public String getFingerPrint() {
        return this.getTransaction().getFingerPrint();
    }

    private String getData() {
        return new StringBuilder(UUID.randomUUID().toString())
                .append(this.getTransaction().getSenderPublicKeyDecorator().toString())
                .append(this.getTransaction().getReceiverPublickeyDecorator().toString())
                .append(this.getTransaction().getValue())
                .toString();
    }

    public void setSignature(byte[] signature) {
        this.getTransaction().setSignature(signature);
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
        return StringUtil.verifyECDSASig(this.getTransaction().getSenderPublicKeyDecorator().getPublicKey(), this.getTransaction().getFingerPrint(), this.getTransaction().getSignature());
    }

    private void removeCurrentOutput() {
        for(TransactionOperationBlockChain transactionOperationBlockChain : this.getTransaction().getUnspentTransactions()) {
            UnspentTransactions.getInstance().remove(transactionOperationBlockChain);
        }
    }

    private void addCurrentTransactionOutput() {
        TransactionOperationBlockChain current = TransactionOperationBlockChain.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.getTransaction().setSenderTransactionOutput(current);
        this.addUnspentTransaction(current);
    }

    private void addLeftOverTransactionOutput() {
        TransactionOperationBlockChain leftover = TransactionOperationBlockChain.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.getTransaction().setReceiverTransactionOutput(leftover);
        this.addUnspentTransaction(leftover);
    }

    private void addUnspentTransaction(TransactionOperationBlockChain transactionOperationBlockChain) {
        UnspentTransactions.getInstance().add(transactionOperationBlockChain);
    }

    private Integer getLeftOverValue() {
        return this.getInputValue() - this.getTransaction().getValue();
    }

    private Integer getInputValue() {
        List<TransactionOperationBlockChain> outputs = this.getTransaction().getUnspentTransactions();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
    }

    private Boolean isInputEqualOutputValue() {
        return this.getInputValue().equals(this.getOutputsValue());
    }

    private Integer getOutputsValue() {
        return this.getTransaction().getSenderTransactionOutput().getValue() + this.getTransaction().getReceiverTransactionOutput().getValue();
    }

    public void isConsistent() {
        if (!this.verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }

        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getTransaction().getFingerPrint() + ")");
        }

        TransactionOperationBlockChain senderTransactionOperationBlockChain = this.getTransaction().getSenderTransactionOutput();
        if (!senderTransactionOperationBlockChain.isMine(this.getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + senderTransactionOperationBlockChain + ") is not who it should be");
        }

        TransactionOperationBlockChain receiverTransactionOperationBlockChain = this.getTransaction().getReceiverTransactionOutput();
        if (!receiverTransactionOperationBlockChain.isMine(this.getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + receiverTransactionOperationBlockChain + ") is not who it should be");
        }
    }
}
