package br.com.lucianoyamane.example.blockchain;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.models.Transaction;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain implements BlockChainObject {

    private Transaction transaction;
    private OperationBlockChain currentOperationBlockChain;
    private OperationBlockChain leftOverOperationBlockChain;
    private List<OperationBlockChain> unspentTransactionsOperationBlockChain;

    private TransactionBlockChain(Transaction transaction) {
        this.setTransaction(transaction);
        transaction.setHash(this.createHash());
        this.createInputs(transaction.getSenderPublicKeyDecorator());
    }

    public static TransactionBlockChain create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
        return new TransactionBlockChain(Transaction.create(senderPublicKeyDecorator, receiverPublickeyDecorator, value));
    }

    private void createInputs(PublicKeyDecorator senderPublicKeyDecorator) {
        List<OperationBlockChain> operationBlockChains = UnspentTransactions.getInstance().loadUnspentUTXO(senderPublicKeyDecorator);
        this.setUnspentTransactionsOperationBlockChain(operationBlockChains);
    }

    public OperationBlockChain getCurrentTransactionOperationBlockChain() {
        return currentOperationBlockChain;
    }

    public void setCurrentTransactionOperationBlockChain(OperationBlockChain currentOperationBlockChain) {
        this.currentOperationBlockChain = currentOperationBlockChain;
    }

    public OperationBlockChain getLeftOverTransactionOperationBlockChain() {
        return leftOverOperationBlockChain;
    }

    public void setLeftOverTransactionOperationBlockChain(OperationBlockChain leftOverOperationBlockChain) {
        this.leftOverOperationBlockChain = leftOverOperationBlockChain;
    }

    public List<OperationBlockChain> getUnspentTransactionsOperationBlockChain() {
        return unspentTransactionsOperationBlockChain;
    }

    public void setUnspentTransactionsOperationBlockChain(List<OperationBlockChain> unspentTransactionsOperationBlockChain) {
        this.unspentTransactionsOperationBlockChain = unspentTransactionsOperationBlockChain;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public PublicKeyDecorator getTransactionReceiverPublicKeyDecorator() {
        return this.getTransaction().getReceiverPublickeyDecorator();
    }

    public PublicKeyDecorator getTransactionSenderPublicKeyDecorator() {
        return this.getTransaction().getSenderPublicKeyDecorator();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    private String createHash() {
        return StringUtil.encode(this.getData());
    }

    public String getHash() {
        return this.getTransaction().getHash();
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
        return StringUtil.verifyECDSASig(this.getTransaction().getSenderPublicKeyDecorator().getPublicKey(), this.getTransaction().getHash(), this.getTransaction().getSignature());
    }

    private void removePreviousTransactions() {
        for(OperationBlockChain operationBlockChain : this.getUnspentTransactionsOperationBlockChain()) {
            UnspentTransactions.getInstance().remove(operationBlockChain);
        }
    }

    private void addCurrentTransactionOutput() {
        OperationBlockChain current = OperationBlockChain.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.setCurrentTransactionOperationBlockChain(current);
        UnspentTransactions.getInstance().add(current);
    }

    private void addLeftOverTransactionOutput() {
        OperationBlockChain leftover = OperationBlockChain.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.setLeftOverTransactionOperationBlockChain(leftover);
        UnspentTransactions.getInstance().add(leftover);
    }

    private Integer getLeftOverValue() {
        return this.getUnspentValue() - this.getTransaction().getValue();
    }

    public Integer getUnspentValue() {
        List<OperationBlockChain> outputs = this.getUnspentTransactionsOperationBlockChain();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
    }

}
