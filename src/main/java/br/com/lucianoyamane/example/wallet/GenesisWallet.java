package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class GenesisWallet extends Wallet {

    private GenesisWallet() {
        super();
    }

    public static GenesisWallet create() {
        return new GenesisWallet();
    }

    @Override
    public TransactionExecutor sendFunds(PublicKeyDecorator receiverPublicKeyDecorator, Integer value) {
        TransactionExecutor transactionExecutor = TransactionExecutor.create(this.getPublicKeyDecorator(), receiverPublicKeyDecorator, value);
        createSignatureTransaction(transactionExecutor);
        return transactionExecutor;
    }

    @Override
    public Integer getBalance() {
        return Integer.valueOf(999999999);
    }
}
