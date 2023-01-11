package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.bdd.BlockChainBDD;
import br.com.lucianoyamane.example.configurations.RegisteredWallets;
import br.com.lucianoyamane.example.wallet.Wallet;

public class ExecuteBlockChain {

    public static void main(String[] args) {

        RegisteredWallets registeredWallets = RegisteredWallets.getInstance();
        registeredWallets.register(Wallet.create("Luciano"));
        registeredWallets.register(Wallet.create("Augusto"));

        BlockChainBDD blockChainBDD = BlockChainBDD.init();
        blockChainBDD
                .genesis()
                    .addTransaction()
                        .sender(blockChainBDD.getGenesisWallet())
                        .receiver(registeredWallets.getWallet("Luciano"))
                        .sendFunds(100)
                    .end()
                .end()
                .newBlock()
                    .addTransaction()
                        .sender(registeredWallets.getWallet("Luciano"))
                        .receiver(registeredWallets.getWallet("Augusto"))
                        .sendFunds(40)
                    .end()
                .end()
                .newBlock()
                    .addTransaction()
                        .sender(registeredWallets.getWallet("Luciano"))
                        .receiver(registeredWallets.getWallet("Augusto"))
                        .sendFunds(1000)
                    .end()
                .end()
                .newBlock()
                    .addTransaction()
                        .sender(registeredWallets.getWallet("Augusto"))
                        .receiver(registeredWallets.getWallet("Luciano"))
                        .sendFunds(20)
                    .end()
                .end()
                .newBlock()
                    .addTransaction()
                        .sender(registeredWallets.getWallet("Luciano"))
                        .receiver(registeredWallets.getWallet("Augusto"))
                        .sendFunds(10)
                    .end()
                .end()
                .newBlock()
                    .addTransaction()
                        .sender(registeredWallets.getWallet("Luciano"))
                        .receiver(registeredWallets.getWallet("Augusto"))
                        .sendFunds(70)
                    .end()
                .end()
            .end();
        blockChainBDD.execute();

    }
}
