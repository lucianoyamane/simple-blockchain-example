package br.com.lucianoyamane.example.valida.operation.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.operation.TransactionOperationValida;

import java.util.Objects;

public class NaoPossueReferenciaOperacaoPreviaCondicao extends Condicao<TransactionOperationValida> {

    private NaoPossueReferenciaOperacaoPreviaCondicao(TransactionOperationValida valida) {
        super(valida);
    }

    public static NaoPossueReferenciaOperacaoPreviaCondicao inicia(TransactionOperationValida valida) {
        return new NaoPossueReferenciaOperacaoPreviaCondicao(valida);
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        TransactionOperationBlockChain referenceTransactionOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValida().getTransactionOperationBlockChain());
        if (Objects.isNull(referenceTransactionOperationBlockChain)) {
            throw new BlockChainException("#Referenced input on Transaction(" + this.getValida().getTransactionOperationBlockChain().getTransactionOperationId() + ") is Missing");
        }
    }
}
