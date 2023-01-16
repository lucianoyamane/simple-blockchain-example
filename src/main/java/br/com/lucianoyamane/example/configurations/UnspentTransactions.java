package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.transaction.TransactionOperation;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.ArrayList;
import java.util.List;

public class UnspentTransactions {

    private static UnspentTransactions instance;

    public static UnspentTransactions getInstance() {
        if (instance == null) {
            instance = new UnspentTransactions();
        }
        return instance;
    }
    private List<TransactionOperation> transactionOperations;

    private UnspentTransactions() {
        this.transactionOperations = new ArrayList<>();
    }

    public void add(TransactionOperation transactionOperation) {
        this.transactionOperations.add(transactionOperation);
    }

    public void remove(TransactionOperation transactionOperation) {
        this.transactionOperations.remove(transactionOperation);
    }

    public Integer getWalletBalance(PublicKeyDecorator publicKeyDecorator) {
        return this.transactionOperations.stream()
                .filter(output -> output.isMine(publicKeyDecorator))
                .mapToInt(TransactionOperation::getValue).sum();
    }

    public List<TransactionOperation> loadUnspentUTXO(PublicKeyDecorator publicKeyDecorator) {
        return this.transactionOperations.stream()
                .filter(output -> output.isMine(publicKeyDecorator)).toList();
    }

}
