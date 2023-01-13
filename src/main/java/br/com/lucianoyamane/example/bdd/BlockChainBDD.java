package br.com.lucianoyamane.example.bdd;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.wallet.GenesisWallet;
import br.com.lucianoyamane.example.wallet.Wallet;

import java.util.ArrayList;
import java.util.List;

public class BlockChainBDD implements BDD {

    private List<BlockBDD> blockBDDS;

    private BlockBDD genesisBlockBDD;

    private BlockChainApp blockChainApp;

    private Wallet genesisWallet;

    private Wallet getGenesisWallet() {
        return genesisWallet;
    }

    public void setGenesisWallet(Wallet genesisWallet) {
        this.genesisWallet = genesisWallet;
    }

    private BlockChainBDD() {
        this.blockChainApp = BlockChainApp.create();
        this.blockBDDS = new ArrayList<>();
        this.genesisWallet = GenesisWallet.novo();
    }

    public static BlockChainBDD init() {
        return new BlockChainBDD();
    }

    public BlockChainBDD bootstrap(String receiver, Integer value) {
        return this.genesis()
                        .transacao()
                            .remetente("Genesis")
                            .destinatario(receiver)
                            .valor(value)
                        .fim()
                    .fim();
    }


    public BlockBDD bloco() {
        BlockBDD blockBDD = BlockBDD.init(this);
        this.blockBDDS.add(blockBDD);
        return blockBDD;
    }

    public BlockBDD genesis() {
        this.genesisBlockBDD = BlockBDD.init(this);
        return this.genesisBlockBDD;
    }

    public BlockChainBDD fim() {
        return this;
    }


    public void executa() {
        SystemOutPrintlnDecorator.ciano("******************************************************");
        SystemOutPrintlnDecorator.azul("Creating and Mining Genesis block... ");
        Block genesis = this.genesisBlockBDD.execute();
        genesis.processGenesis();
        this.blockChainApp.mine(genesis);
        this.blockChainApp.addBlock(genesis);
        String previousHash = genesis.getHash();
        this.blockChainApp.isChainValid();
        CarteirasRegistradas.abre().getFinalBalances();
        for(BlockBDD blockBDD : this.blockBDDS) {
            SystemOutPrintlnDecorator.ciano("******************************************************");
            Block block = blockBDD.execute();
            block.process(previousHash);
            this.blockChainApp.mine(block);
            this.blockChainApp.addBlock(block);
            previousHash = block.getHash();
            this.blockChainApp.isChainValid();
            CarteirasRegistradas.abre().getFinalBalances();
        }
    }
}
