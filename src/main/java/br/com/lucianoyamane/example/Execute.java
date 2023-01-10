package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

public class Execute {

    public static void main(String[] args) {


        Wallet walletA = Wallet.create("A");
        Wallet walletB = Wallet.create("B");
        GenesisWallet genesisWallet = GenesisWallet.create();

        BlockChain blockChain = BlockChain.create();

        String genesisHash = blockChain.genesisBlock(genesisWallet, walletA, 10000);
        blockChain.isChainValid();

        String block1Hash = blockChain.transactionBlock(genesisHash, walletA, walletB, 4000);
        blockChain.isChainValid();

        String block2Hash = blockChain.transactionBlock(block1Hash, walletA, walletB, 100000);
        blockChain.isChainValid();

        String block3Hash = blockChain.transactionBlock(block2Hash,walletB, walletA, 2000);
        blockChain.isChainValid();

        String block4Hash = blockChain.transactionBlock(block3Hash,walletA, walletB, 1000);
        blockChain.isChainValid();

        String block5Hash = blockChain.transactionBlock(block4Hash,walletA, walletB, 7000);
        blockChain.isChainValid();

    }
}
