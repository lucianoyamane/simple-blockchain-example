package br.com.lucianoyamane.example.dundermifflin;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.exception.BlockChainException;

import java.util.ArrayList;
import java.util.List;

public class DunderMifflinCompanhiaDePapelAdapter {

    private List<Transacao> transacoes;
    private Transacao transacaoBootstrap;

    private BlockChainApp blockChainApp;

    private DunderMifflinCompanhiaDePapelAdapter() {
        this.transacoes = new ArrayList<>();
        this.blockChainApp = BlockChainApp.create();
    }

    public static DunderMifflinCompanhiaDePapelAdapter iniciaOsTrabalhos() {
        return new DunderMifflinCompanhiaDePapelAdapter();
    }

    public Transacao emissor(Vendedor nome) {
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
        SystemOutPrintlnDecorator.verde("Criando a transferência inicial");
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
