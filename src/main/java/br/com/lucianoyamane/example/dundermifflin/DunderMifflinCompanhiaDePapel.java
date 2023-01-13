package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.block.Block;
import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;

import java.util.ArrayList;
import java.util.List;

public class DunderMifflinCompanhiaDePapel {

    private List<Transacao> transacoes;
    private Transacao transacaoBootstrap;

    private BlockChainApp blockChainApp;

    private DunderMifflinCompanhiaDePapel() {
        this.transacoes = new ArrayList<>();
        this.blockChainApp = BlockChainApp.create();
    }

    public static DunderMifflinCompanhiaDePapel iniciaOsTrabalhos() {
        return new DunderMifflinCompanhiaDePapel();
    }

    public Transacao cliente(String nome) {
        Transacao transacao = Transacao.cria(this).remetente(nome);
        this.transacoes.add(transacao);
        return transacao;
    }

    public Transacao michaelScott() {
        this.transacaoBootstrap = Transacao.michaelScott(this);
        return this.transacaoBootstrap;
    }

    public void processa() {
        SystemOutPrintlnDecorator.ciano("******************************************************");
        SystemOutPrintlnDecorator.azul("Creating and Mining Genesis block... ");
        Block blockGenesis = Block.init();
        blockGenesis.addTransaction(this.transacaoBootstrap.toTransaction());
        blockGenesis.processGenesis();
        blockGenesis.mineBlock(Difficulty.getInstance().getDifficulty());
        this.blockChainApp.addBlock(blockGenesis);
        String previousHash = blockGenesis.getHash();
        this.blockChainApp.isChainValid();
        for(Transacao transacao : this.transacoes) {
            SystemOutPrintlnDecorator.ciano("******************************************************");
            Block block = Block.init();
            block.addTransaction(transacao.toTransaction());
            block.process(previousHash);
            this.blockChainApp.mine(block);
            this.blockChainApp.addBlock(block);
            previousHash = block.getHash();
            this.blockChainApp.isChainValid();
            CarteirasRegistradas.abre().getFinalBalances();
        }


    }

}
