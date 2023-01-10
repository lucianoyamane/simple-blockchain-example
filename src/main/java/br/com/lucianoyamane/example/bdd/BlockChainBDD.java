package br.com.lucianoyamane.example.bdd;

import br.com.lucianoyamane.example.BlockChain;
import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.RegisteredWallets;
import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

public class BlockChainBDD implements BDD {

    private List<BlockBDD> blockBDDS;

    private BlockBDD genesisBlockBDD;

    private BlockChain blockChain;

    private Wallet genesisWallet;

    public Wallet getGenesisWallet() {
        return genesisWallet;
    }

    public void setGenesisWallet(Wallet genesisWallet) {
        this.genesisWallet = genesisWallet;
    }

    private BlockChainBDD() {
        this.blockChain = BlockChain.create();
        this.blockBDDS = new ArrayList<>();
        this.genesisWallet = GenesisWallet.create();
    }

    public static BlockChainBDD init() {
        return new BlockChainBDD();
    }




    public BlockBDD newBlock() {
        BlockBDD blockBDD = BlockBDD.init(this);
        this.blockBDDS.add(blockBDD);
        return blockBDD;
    }

    public BlockBDD genesis() {
        this.genesisBlockBDD = BlockBDD.init(this);
        return this.genesisBlockBDD;
    }

    public BlockChainBDD end() {
        return this;
    }

    public void execute() {
        System.out.println("******************************************************");
        System.out.println("Creating and Mining Genesis block... ");
        Block genesis = this.genesisBlockBDD.execute();
        genesis.processGenesis();
        this.blockChain.mine(genesis);
        this.blockChain.addBlock(genesis);
        String previousHash = genesis.getHash();
        this.blockChain.isChainValid();
        RegisteredWallets.getInstance().getFinalBalances();
        for(BlockBDD blockBDD : this.blockBDDS) {
            System.out.println("******************************************************");
            Block block = blockBDD.execute();
            block.process(previousHash);
            this.blockChain.mine(block);
            this.blockChain.addBlock(block);
            previousHash = block.getHash();
            this.blockChain.isChainValid();
            RegisteredWallets.getInstance().getFinalBalances();
        }
    }
}
