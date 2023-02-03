package br.com.lucianoyamane.example.valida.transaction;

import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Valida;
import br.com.lucianoyamane.example.valida.operation.TransactionOperationValida;
import br.com.lucianoyamane.example.valida.transaction.condicao.AssinaturaNaoVerificadaCondicao;
import br.com.lucianoyamane.example.valida.transaction.condicao.TransacaoAtualNaoPertenceAoDestinatarioCondicao;
import br.com.lucianoyamane.example.valida.transaction.condicao.TransacaoValorRestanteNaoPertenceAoRemetenteCondicao;
import br.com.lucianoyamane.example.valida.transaction.condicao.ValoresDeEntradaDiferenteValoresSaidaCondicao;

import java.util.List;

public class TransactionValida extends Valida {

    private TransactionBlockChain transactionBlockChain;

    private TransactionValida(TransactionBlockChain transactionBlockChain) {
        this.setTransactionBlockChain(transactionBlockChain);
    }

    @Override
    protected void defineCondicoes() {
        this.addCondicao(AssinaturaNaoVerificadaCondicao.inicia(this));
        this.addCondicao(TransacaoAtualNaoPertenceAoDestinatarioCondicao.inicia(this));
        this.addCondicao(TransacaoValorRestanteNaoPertenceAoRemetenteCondicao.inicia(this));
        this.addCondicao(ValoresDeEntradaDiferenteValoresSaidaCondicao.inicia(this));
    }

    public static TransactionValida valida(TransactionBlockChain transactionBlockChain) {
        return new TransactionValida(transactionBlockChain);
    }

    public TransactionBlockChain getTransactionBlockChain() {
        return transactionBlockChain;
    }

    private void setTransactionBlockChain(TransactionBlockChain transactionBlockChain) {
        this.transactionBlockChain = transactionBlockChain;
    }

    @Override
    public void processaDadosProximoBloco(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        List<TransactionOperationBlockChain> transactionsOperationBlockChain = this.getTransactionBlockChain().getUnspentTransactionsOperationBlockChain();

        for(TransactionOperationBlockChain output : transactionsOperationBlockChain) {
            TransactionOperationValida.valida(output).executa(previousBlockData);
        }

        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
