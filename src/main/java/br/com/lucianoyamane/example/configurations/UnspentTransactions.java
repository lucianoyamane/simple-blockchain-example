package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
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
    private List<TransactionOperationBlockChain> transactionOperationBlockChains;

    private UnspentTransactions() {
        this.transactionOperationBlockChains = new ArrayList<>();
    }

    public void add(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.transactionOperationBlockChains.add(transactionOperationBlockChain);
    }

    public void remove(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.transactionOperationBlockChains.remove(transactionOperationBlockChain);
    }

    public Integer getWalletBalance(PublicKeyDecorator publicKeyDecorator) {
        return this.transactionOperationBlockChains.stream()
                .filter(output -> publicKeyDecorator.mePertence(output))
                .mapToInt(TransactionOperationBlockChain::getValue).sum();
    }

    public List<TransactionOperationBlockChain> loadUnspentUTXO(PublicKeyDecorator publicKeyDecorator) {
        return this.transactionOperationBlockChains.stream()
                .filter(output -> publicKeyDecorator.mePertence(output)).toList();
    }

}
