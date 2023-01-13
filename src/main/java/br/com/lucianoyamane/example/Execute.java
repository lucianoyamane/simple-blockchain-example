package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

public class Execute {

    public static void main(String[] args) {


        Wallet walletA = Wallet.novo("A");
        Wallet walletB = Wallet.novo("B");
        GenesisWallet genesisWallet = GenesisWallet.novo();

        BlockChainApp blockChainApp = BlockChainApp.create();

        String genesisHash = blockChainApp.genesisBlock(genesisWallet, walletA, 10000);
        blockChainApp.isChainValid();

        String block1Hash = blockChainApp.transactionBlock(genesisHash, walletA, walletB, 4000);
        blockChainApp.isChainValid();

        String block2Hash = blockChainApp.transactionBlock(block1Hash, walletA, walletB, 100000);
        blockChainApp.isChainValid();

        String block3Hash = blockChainApp.transactionBlock(block2Hash,walletB, walletA, 2000);
        blockChainApp.isChainValid();

        String block4Hash = blockChainApp.transactionBlock(block3Hash,walletA, walletB, 1000);
        blockChainApp.isChainValid();

        String block5Hash = blockChainApp.transactionBlock(block4Hash,walletA, walletB, 7000);
        blockChainApp.isChainValid();

    }
}
