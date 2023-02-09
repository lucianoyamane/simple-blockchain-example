package br.com.lucianoyamane.example.configurations;

import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
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
    private List<OperationBlockChain> operationBlockChains;

    private UnspentTransactions() {
        this.operationBlockChains = new ArrayList<>();
    }

    public void add(OperationBlockChain operationBlockChain) {
        this.operationBlockChains.add(operationBlockChain);
    }

    public void remove(OperationBlockChain operationBlockChain) {
        this.operationBlockChains.remove(operationBlockChain);
    }

    public Integer getWalletBalance(PublicKeyDecorator publicKeyDecorator) {
        return this.operationBlockChains.stream()
                .filter(output -> publicKeyDecorator.mePertence(output))
                .mapToInt(OperationBlockChain::getValue).sum();
    }

    public List<OperationBlockChain> loadUnspentUTXO(PublicKeyDecorator publicKeyDecorator) {
        return this.operationBlockChains.stream()
                .filter(output -> publicKeyDecorator.mePertence(output)).toList();
    }

}
