package br.com.lucianoyamane.example.valida.transaction.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.transaction.TransactionValida;

public class TransacaoValorRestanteNaoPertenceAoRemetenteCondicao extends Condicao<TransactionValida> {

    private TransacaoValorRestanteNaoPertenceAoRemetenteCondicao(TransactionValida valida) {
        super(valida);
    }

    public static TransacaoValorRestanteNaoPertenceAoRemetenteCondicao inicia(TransactionValida valida) {
        return new TransacaoValorRestanteNaoPertenceAoRemetenteCondicao(valida);
    }

    @Override
    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().isMine(this.getValida().getTransactionBlockChain().getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain() + ") is not who it should be");
        }
    }
}
