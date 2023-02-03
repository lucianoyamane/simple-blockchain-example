package br.com.lucianoyamane.example.valida.operation.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.operation.TransactionOperationValida;

public class NaoPossueMesmoValorReferenciaPreviaCondicao extends Condicao<TransactionOperationValida> {

    private NaoPossueMesmoValorReferenciaPreviaCondicao(TransactionOperationValida valida) {
        super(valida);
    }

    public static NaoPossueMesmoValorReferenciaPreviaCondicao inicia(TransactionOperationValida valida) {
        return new NaoPossueMesmoValorReferenciaPreviaCondicao(valida);
    }

    @Override
    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        TransactionOperationBlockChain referenceTransactionOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValida().getTransactionOperationBlockChain());
        if (!this.getValida().possueMesmoValor(referenceTransactionOperationBlockChain)){
            throw new BlockChainException("#Referenced input Transaction(" + this.getValida().getTransactionOperationBlockChain().getTransactionOperationId() + ") value is Invalid");
        }


    }
}