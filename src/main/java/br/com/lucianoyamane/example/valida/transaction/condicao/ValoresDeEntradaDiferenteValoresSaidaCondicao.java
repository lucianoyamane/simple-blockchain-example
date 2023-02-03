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

    public Boolean isInputEqualOutputValue() {
        return this.getValida().getTransactionBlockChain().getUnspentValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getValida().getTransactionBlockChain().getCurrentTransactionOperationBlockChain().getValue() + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getValue();
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getValida().getTransactionBlockChain().getTransaction().getFingerPrint() + ")");
        }
    }
}
