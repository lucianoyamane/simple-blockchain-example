package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.entity.TransactionBlockChain;

public class GenesisWallet extends Wallet {

    private GenesisWallet(String name) {
        super(name);
    }

    public static GenesisWallet novo() {
        return new GenesisWallet("Genesis");
    }

    @Override
    public TransactionBlockChain sendFunds(PublicData receiverPublicData, Integer value) {
        TransactionBlockChain transactionBlockChain = TransactionBlockChain.create(this.toPublicData().getPublicKeyDecorator(), receiverPublicData.getPublicKeyDecorator(), value);
        createSignatureTransaction(transactionBlockChain);
        return transactionBlockChain;
    }

    @Override
    public Integer getBalance() {
        return Integer.valueOf(999999999);
    }
}
