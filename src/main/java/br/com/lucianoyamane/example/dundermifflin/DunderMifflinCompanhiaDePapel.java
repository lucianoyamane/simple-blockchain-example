package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.configurations.CarteirasRegistradas;
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
        SystemOutPrintlnDecorator.ciano("Creating and Mining Genesis block... ");
        String previousHash = this.blockChainApp.genesisBlock(this.transacaoBootstrap.toTransaction());
        for(Transacao transacao : this.transacoes) {
            SystemOutPrintlnDecorator.ciano("******************************************************");
            previousHash = this.blockChainApp.transactionBlock(transacao.toTransaction(), previousHash);
            CarteirasRegistradas.abre().getFinalBalances();
        }
        this.blockChainApp.isValid();

    }

}
