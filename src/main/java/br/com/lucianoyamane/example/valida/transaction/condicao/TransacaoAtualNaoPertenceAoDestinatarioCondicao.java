package br.com.lucianoyamane.example.valida.transaction.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.transaction.TransactionValida;

public class TransacaoAtualNaoPertenceAoDestinatarioCondicao extends Condicao<TransactionValida> {

    private TransacaoAtualNaoPertenceAoDestinatarioCondicao(TransactionValida valida) {
        super(valida);
    }

    public static TransacaoAtualNaoPertenceAoDestinatarioCondicao inicia(TransactionValida valida) {
        return new TransacaoAtualNaoPertenceAoDestinatarioCondicao(valida);
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getTransactionBlockChain().getCurrentTransactionOperationBlockChain().isMine(this.getValida().getTransactionBlockChain().getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + this.getValida().getTransactionBlockChain().getCurrentTransactionOperationBlockChain() + ") is not who it should be");
        }
    }
}
