package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.transaction.Transaction;

public class GenesisWallet extends Wallet {

    private GenesisWallet(String name) {
        super(name);
    }

    public static GenesisWallet create() {
        return new GenesisWallet("Genesis");
    }

    @Override
    public Transaction sendFunds(PublicData receiverPublicData, Integer value) {
        Transaction newTransaction = Transaction.genesis(this.toPublicData(), receiverPublicData, value);
        newTransaction.setSignature(createSignatureTransaction(newTransaction.getHash()));
        return newTransaction;

    }
}
