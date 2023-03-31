package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;

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

    public Transacao cliente(Vendedores nome) {
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
        String previousHash = this.blockChainApp.transactionGenesis(this.transacaoBootstrap.toTransaction());
        VendedoresRegistrados.abre().saldos();
        for(Transacao transacao : this.transacoes) {
            SystemOutPrintlnDecorator.ciano("******************************************************");
            previousHash = this.blockChainApp.transaction(transacao.toTransaction(), previousHash);
            VendedoresRegistrados.abre().saldos();
            this.validateBlockChain();
        }
    }

    public void validateBlockChain() {
        try {
            this.blockChainApp.validate();
        } catch (BlockChainException e) {
            SystemOutPrintlnDecorator.vermelho(e.getMessage());
        }
    }

}
