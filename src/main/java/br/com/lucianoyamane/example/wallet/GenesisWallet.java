package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.transaction.Transaction;

public class GenesisWallet extends Wallet {

    private GenesisWallet(String name) {
        super(name);
    }

    public static GenesisWallet novo() {
        return new GenesisWallet("Genesis");
    }

    @Override
    public Transaction sendFunds(PublicData receiverPublicData, Integer value) {
        Transaction newTransaction = Transaction.genesis(this.toPublicData(), receiverPublicData, value);
        newTransaction.setSignature(createSignatureTransaction(newTransaction.getHash()));
        return newTransaction;
    }

    @Override
    public Integer getBalance() {
        return Integer.valueOf(999999999);
    }
}
