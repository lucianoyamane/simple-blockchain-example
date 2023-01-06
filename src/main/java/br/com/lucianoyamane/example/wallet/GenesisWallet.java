package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.transaction.GenesisTransaction;
import br.com.lucianoyamane.example.transaction.Transaction;

public class GenesisWallet extends Wallet{

    private GenesisWallet(String name) {
        super(name);
    }

    public static GenesisWallet create(String name) {
        return new GenesisWallet(name);
    }

    @Override
    public Transaction sendFunds(Operator receiverOperator, Integer value) {
        Transaction newTransaction = GenesisTransaction.create(this.toOperator(), receiverOperator, value);
        newTransaction.setSignature(createSignatureTransaction(newTransaction.getHash()));
        return newTransaction;

    }
}
