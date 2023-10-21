package br.com.lucianoyamane.example.blockchain;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.models.Transaction;

import java.util.List;
import java.util.UUID;

public class TransactionExecutor implements Executor {

    private Transaction transaction;
    private OperationExecutor currentOperationExecutor;
    private OperationExecutor leftOverOperationExecutor;
    private List<OperationExecutor> unspentTransactionsOperationExecutor;

    private TransactionExecutor(Transaction transaction) {
        this.setTransaction(transaction);
        transaction.setHash(this.createHash());
        this.createInputs(transaction.getSenderPublicKeyDecorator());
    }

    public static TransactionExecutor create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
        return new TransactionExecutor(Transaction.create(senderPublicKeyDecorator, receiverPublickeyDecorator, value));
    }

    private void createInputs(PublicKeyDecorator senderPublicKeyDecorator) {
        List<OperationExecutor> operationExecutors = UnspentTransactions.getInstance().loadUnspentUTXO(senderPublicKeyDecorator);
        this.setUnspentTransactionsOperationBlockChain(operationExecutors);
    }

    public OperationExecutor getCurrentTransactionOperationBlockChain() {
        return currentOperationExecutor;
    }

    public void setCurrentTransactionOperationBlockChain(OperationExecutor currentOperationExecutor) {
        this.currentOperationExecutor = currentOperationExecutor;
    }

    public OperationExecutor getLeftOverTransactionOperationBlockChain() {
        return leftOverOperationExecutor;
    }

    public void setLeftOverTransactionOperationBlockChain(OperationExecutor leftOverOperationExecutor) {
        this.leftOverOperationExecutor = leftOverOperationExecutor;
    }

    public List<OperationExecutor> getUnspentTransactionsOperationBlockChain() {
        return unspentTransactionsOperationExecutor;
    }

    public void setUnspentTransactionsOperationBlockChain(List<OperationExecutor> unspentTransactionsOperationExecutor) {
        this.unspentTransactionsOperationExecutor = unspentTransactionsOperationExecutor;
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
        for(OperationExecutor operationExecutor : this.getUnspentTransactionsOperationBlockChain()) {
            UnspentTransactions.getInstance().remove(operationExecutor);
        }
    }

    private void addCurrentTransactionOutput() {
        OperationExecutor current = OperationExecutor.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.setCurrentTransactionOperationBlockChain(current);
        UnspentTransactions.getInstance().add(current);
    }

    private void addLeftOverTransactionOutput() {
        OperationExecutor leftover = OperationExecutor.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.setLeftOverTransactionOperationBlockChain(leftover);
        UnspentTransactions.getInstance().add(leftover);
    }

    private Integer getLeftOverValue() {
        return this.getUnspentValue() - this.getTransaction().getValue();
    }

    public Integer getUnspentValue() {
        List<OperationExecutor> outputs = this.getUnspentTransactionsOperationBlockChain();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
    }

}
