package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class ValoresDeEntradaDiferenteValoresSaidaCondicao extends Condicao<TransactionValidate> {

    private ValoresDeEntradaDiferenteValoresSaidaCondicao(TransactionValidate valida) {
        super(valida);
    }

    public static ValoresDeEntradaDiferenteValoresSaidaCondicao inicia(TransactionValidate valida) {
        return new ValoresDeEntradaDiferenteValoresSaidaCondicao(valida);
    }

    public Boolean isInputEqualOutputValue() {
        return this.getValida().getTransactionBlockChain().getUnspentValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getValida().getTransactionBlockChain().getCurrentTransactionOperationBlockChain().getValue() + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getValue();
    }

    @Override
    protected String getMessage() {
        return "Valores divegentes de entrada e saida(" + this.getValida().getTransactionBlockChain().getTransaction().getHash() + ")";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.isInputEqualOutputValue();
    }
}
