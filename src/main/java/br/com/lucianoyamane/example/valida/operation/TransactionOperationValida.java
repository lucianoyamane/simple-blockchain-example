package br.com.lucianoyamane.example.valida.operation;

import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Valida;
import br.com.lucianoyamane.example.valida.operation.condicao.NaoPossueMesmoValorReferenciaPreviaCondicao;
import br.com.lucianoyamane.example.valida.operation.condicao.NaoPossueReferenciaOperacaoPreviaCondicao;

public class TransactionOperationValida extends Valida {

    private TransactionOperationBlockChain transactionOperationBlockChain;

    public static TransactionOperationValida valida(TransactionOperationBlockChain transactionOperationBlockChain) {
        return new TransactionOperationValida(transactionOperationBlockChain);
    }

    @Override
    protected void defineCondicoes() {
        this.addCondicao(NaoPossueMesmoValorReferenciaPreviaCondicao.inicia(this));
        this.addCondicao(NaoPossueReferenciaOperacaoPreviaCondicao.inicia(this));
    }

    private TransactionOperationValida(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.setTransactionOperationBlockChain(transactionOperationBlockChain);
    }

    public TransactionOperationBlockChain getTransactionOperationBlockChain() {
        return transactionOperationBlockChain;
    }

    public Integer getTransactionOperationBlockChainValue() {
        return this.getTransactionOperationBlockChain().getTransactionOperationValue();
    }

    private void setTransactionOperationBlockChain(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.transactionOperationBlockChain = transactionOperationBlockChain;
    }

    @Override
    public void processaDadosProximoBloco(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        previousBlockData.removeTransactionOperationBlockChains(this.getTransactionOperationBlockChain());
    }
}
