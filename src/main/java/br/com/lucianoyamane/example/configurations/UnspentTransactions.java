package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.blockchain.OperationExecutor;
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
    private List<OperationExecutor> operationExecutors;

    private UnspentTransactions() {
        this.operationExecutors = new ArrayList<>();
    }

    public void add(OperationExecutor operationExecutor) {
        this.operationExecutors.add(operationExecutor);
    }

    public void remove(OperationExecutor operationExecutor) {
        this.operationExecutors.remove(operationExecutor);
    }

    public Integer getWalletBalance(PublicKeyDecorator publicKeyDecorator) {
        return this.operationExecutors.stream()
                .filter(output -> publicKeyDecorator.mePertence(output))
                .mapToInt(OperationExecutor::getValue).sum();
    }

    public List<OperationExecutor> loadUnspentUTXO(PublicKeyDecorator publicKeyDecorator) {
        return this.operationExecutors.stream()
                .filter(output -> publicKeyDecorator.mePertence(output)).toList();
    }

}
