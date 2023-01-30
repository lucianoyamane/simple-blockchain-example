package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.List;
import java.util.UUID;

public class TransactionBlockChain implements BlockChainObject {

    private Transaction transaction;

    private TransactionOperationBlockChain senderTransactionOperationBlockChain;
    private TransactionOperationBlockChain receiverTransactionOperationBlockChain;
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

    public TransactionOperationBlockChain getSenderTransactionOperationBlockChain() {
        return senderTransactionOperationBlockChain;
    }

    public void setSenderTransactionOperationBlockChain(TransactionOperationBlockChain senderTransactionOperationBlockChain) {
        this.senderTransactionOperationBlockChain = senderTransactionOperationBlockChain;
    }

    public TransactionOperationBlockChain getReceiverTransactionOperationBlockChain() {
        return receiverTransactionOperationBlockChain;
    }

    public void setReceiverTransactionOperationBlockChain(TransactionOperationBlockChain receiverTransactionOperationBlockChain) {
        this.receiverTransactionOperationBlockChain = receiverTransactionOperationBlockChain;
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
        for(TransactionOperationBlockChain transactionOperationBlockChain : this.getUnspentTransactionsOperationBlockChain()) {
            UnspentTransactions.getInstance().remove(transactionOperationBlockChain);
        }
    }

    private void addCurrentTransactionOutput() {
        TransactionOperationBlockChain current = TransactionOperationBlockChain.create( this.getTransaction().getReceiverPublickeyDecorator(), this.getTransaction().getValue());
        this.setSenderTransactionOperationBlockChain(current);
        this.addUnspentTransaction(current);
    }

    private void addLeftOverTransactionOutput() {
        TransactionOperationBlockChain leftover = TransactionOperationBlockChain.create( this.getTransaction().getSenderPublicKeyDecorator(), this.getLeftOverValue());
        this.setReceiverTransactionOperationBlockChain(leftover);
        this.addUnspentTransaction(leftover);
    }

    private void addUnspentTransaction(TransactionOperationBlockChain transactionOperationBlockChain) {
        UnspentTransactions.getInstance().add(transactionOperationBlockChain);
    }

    private Integer getLeftOverValue() {
        return this.getInputValue() - this.getTransaction().getValue();
    }

    private Integer getInputValue() {
        List<TransactionOperationBlockChain> outputs = this.getUnspentTransactionsOperationBlockChain();
        return outputs.stream().mapToInt(output -> output.getValue()).sum();
    }

    private Boolean isInputEqualOutputValue() {
        return this.getInputValue().equals(this.getOutputsValue());
    }

    private Integer getOutputsValue() {
        return this.getSenderTransactionOperationBlockChain().getValue() + this.getReceiverTransactionOperationBlockChain().getValue();
    }

    @Override
    public void isConsistent(BlockChainApp.PreviousBlockData previousBlockData) {
        if (!this.verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }

        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getTransaction().getFingerPrint() + ")");
        }

        TransactionOperationBlockChain senderTransactionOperationBlockChain = this.getSenderTransactionOperationBlockChain();
        if (!senderTransactionOperationBlockChain.isMine(this.getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + senderTransactionOperationBlockChain + ") is not who it should be");
        }

        TransactionOperationBlockChain receiverTransactionOperationBlockChain = this.getReceiverTransactionOperationBlockChain();
        if (!receiverTransactionOperationBlockChain.isMine(this.getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + receiverTransactionOperationBlockChain + ") is not who it should be");
        }

        List<TransactionOperationBlockChain> transactionsOperationBlockChain = this.getUnspentTransactionsOperationBlockChain();

        for(TransactionOperationBlockChain output : transactionsOperationBlockChain) {
            output.isConsistent(previousBlockData);
        }

        previousBlockData.addTransactionOperationBlockChains(this.getSenderTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getReceiverTransactionOperationBlockChain());
    }
}
