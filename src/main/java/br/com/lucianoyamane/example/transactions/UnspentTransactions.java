package br.com.lucianoyamane.example.transactions;

import br.com.lucianoyamane.example.TransactionOutput;
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
    private List<TransactionOutput> transactionOutputs;

    private UnspentTransactions() {
        this.transactionOutputs = new ArrayList<>();
    }

    public List<TransactionOutput> get() {
        return transactionOutputs;
    }

    public void add(TransactionOutput transactionOutput) {
        this.transactionOutputs.add(transactionOutput);
    }

    public void remove(TransactionOutput transactionOutput) {
        this.transactionOutputs.remove(transactionOutput);
    }

    public Integer getWalletBalance(PublicKeyDecorator publicKeyDecorator) {
        return this.transactionOutputs.stream()
                .filter(output -> output.isMine(publicKeyDecorator))
                .mapToInt(TransactionOutput::getValue).sum();
    }

}
