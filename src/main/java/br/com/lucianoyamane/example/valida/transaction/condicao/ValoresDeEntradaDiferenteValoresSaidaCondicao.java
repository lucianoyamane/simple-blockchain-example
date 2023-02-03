package br.com.lucianoyamane.example.valida.transaction.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.transaction.TransactionValida;

public class ValoresDeEntradaDiferenteValoresSaidaCondicao extends Condicao<TransactionValida> {

    private ValoresDeEntradaDiferenteValoresSaidaCondicao(TransactionValida valida) {
        super(valida);
    }

    public static ValoresDeEntradaDiferenteValoresSaidaCondicao inicia(TransactionValida valida) {
        return new ValoresDeEntradaDiferenteValoresSaidaCondicao(valida);
    }

    @Override
    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getValida().getTransactionBlockChain().getTransaction().getFingerPrint() + ")");
        }
    }
}
