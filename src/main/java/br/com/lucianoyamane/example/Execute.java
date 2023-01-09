package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

public class Execute {

    public static void main(String[] args) {

        Wallet walletA = Wallet.create("A");
        Wallet walletB = Wallet.create("B");
        GenesisWallet genesisWallet = GenesisWallet.create();

        String genesisHash = BlockChain.genesisBlock(genesisWallet, walletA, 10000);
        BlockChain.isChainValid();

        String block1Hash = BlockChain.transactionBlock(genesisHash, walletA, walletB, 4000);
        BlockChain.isChainValid();

        String block2Hash = BlockChain.transactionBlock(block1Hash, walletA, walletB, 100000);
        BlockChain.isChainValid();

        String block3Hash = BlockChain.transactionBlock(block2Hash,walletB, walletA, 2000);
        BlockChain.isChainValid();

        String block4Hash = BlockChain.transactionBlock(block3Hash,walletA, walletB, 1000);
        BlockChain.isChainValid();

        String block5Hash = BlockChain.transactionBlock(block4Hash,walletA, walletB, 7000);
        BlockChain.isChainValid();

    }
}
