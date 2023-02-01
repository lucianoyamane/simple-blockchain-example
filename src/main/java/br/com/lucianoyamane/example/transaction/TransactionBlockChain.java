package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain implements BlockChainObject {

    private Transaction transaction;
    private TransactionOperationBlockChain currentTransactionOperationBlockChain;
    private TransactionOperationBlockChain leftOverTransactionOperationBlockChain;
    private List<TransactionOperationBlockChain> unspentTransactionsOperationBlockChain;

    private TransactionBlockChain(Transaction transaction) {
        this.setTransaction(transaction);
        transaction.setFingerPrint(this.createFingerPrint());
        this.createInputs(transaction.getSenderPublicKeyDecorator());
    }

    public static TransactionBlockChain create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
        return new TransactionBlockChain(Transaction.create(senderPublicKeyDecorator, receiverPublickeyDecorator, value));
    }

    private void createInputs(PublicKeyDecorator senderPublicKeyDecorator) {
        List<TransactionOperationBlockChain> transactionOperationBlockChains = UnspentTransactions.getInstance().loadUnspentUTXO(senderPublicKeyDecorator);
        this.setUnspentTransactionsOperationBlockChain(transactionOperationBlockChains);
    }

    public TransactionOperationBlockChain getCurrentTransactionOperationBlockChain() {
        return currentTransactionOperationBlockChain;
    }

    public void setCurrentTransactionOperationBlockChain(TransactionOperationBlockChain currentTransactionOperationBlockChain) {
        this.currentTransactionOperationBlockChain = currentTransactionOperationBlockChain;
    }

    public TransactionOperationBlockChain getLeftOverTransactionOperationBlockChain() {
        return leftOverTransactionOperationBlockChain;
    }

    public void setLeftOverTransactionOperationBlockChain(TransactionOperationBlockChain leftOverTransactionOperationBlockChain) {
        this.leftOverTransactionOperationBlockChain = leftOverTransactionOperationBlockChain;
    }

    public List<TransactionOperationBlockChain> getUnspentTransactionsOperationBlockChain() {
        return unspentTransactionsOperationBlockChain;
    }

    public void setUnspentTransactionsOperationBlockChain(List<TransactionOperationBlockChain> unspentTransactionsOperationBlockChain) {
        this.unspentTransactionsOperationBlockChain = unspentTransactionsOperationBlockChain;
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
            this.removePreviousTransactions();
            this.addCurrentTransactionOutput();
            this.addLeftOverTransactionOutput();
        }
        return signatureVerified;
    }

    public Boolean verifiySignature() {
        return StringUtil.verifyECDSASig(this.getTransaction().getSenderPublicKeyDecorator().getPublicKey(), this.getTransaction().getFingerPrint(), this.getTransaction().getSignature());
    }

    private void removePreviousTransactions() {
        for(TransactionOperationBlockChain transactionOperationBlockChain : this.getUnspentTransactionsOperationBlockChain()) {
            UnspentTransactions.getInstance().remove(transactionOperationBlockChain);
        }
    }

    private void addCurrentTransactionOutput() {
        TransactionOperationBlockChain current = TransactionOperationBlockChain.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.setCurrentTransactionOperationBlockChain(current);
        this.addUnspentTransaction(current);
    }

    private void addLeftOverTransactionOutput() {
        TransactionOperationBlockChain leftover = TransactionOperationBlockChain.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.setLeftOverTransactionOperationBlockChain(leftover);
        this.addUnspentTransaction(leftover);
    }

    private void addUnspentTransaction(TransactionOperationBlockChain transactionOperationBlockChain) {
        UnspentTransactions.getInstance().add(transactionOperationBlockChain);
    }

    private Integer getLeftOverValue() {
        return this.getUnspentValue() - this.getTransaction().getValue();
    }

    public Integer getUnspentValue() {
        List<TransactionOperationBlockChain> outputs = this.getUnspentTransactionsOperationBlockChain();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
    }

}
