package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class GenesisWallet extends Wallet {

    private GenesisWallet() {
        super();
    }

    public static GenesisWallet create() {
        return new GenesisWallet();
    }

    @Override
    public TransactionBlockChain sendFunds(PublicKeyDecorator receiverPublicKeyDecorator, Integer value) {
        TransactionBlockChain transactionBlockChain = TransactionBlockChain.create(this.getPublicKeyDecorator(), receiverPublicKeyDecorator, value);
        createSignatureTransaction(transactionBlockChain);
        return transactionBlockChain;
    }

    @Override
    public Integer getBalance() {
        return Integer.valueOf(999999999);
    }
}
