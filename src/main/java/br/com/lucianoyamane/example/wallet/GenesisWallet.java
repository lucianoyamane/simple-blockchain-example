package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.entity.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.Transaction;

public class GenesisWallet extends Wallet {

    private GenesisWallet(String name) {
        super(name);
    }

    public static GenesisWallet novo() {
        return new GenesisWallet("Genesis");
    }

    @Override
    public TransactionBlockChain sendFunds(PublicData receiverPublicData, Integer value) {
        Transaction newTransaction = Transaction.genesis(this.toPublicData(), receiverPublicData, value);
        TransactionBlockChain transactionBlockChain = TransactionBlockChain.create(newTransaction);
        transactionBlockChain.setSignature(createSignatureTransaction(transactionBlockChain.getFingerPrint()));
        return transactionBlockChain;
    }

    @Override
    public Integer getBalance() {
        return Integer.valueOf(999999999);
    }
}
