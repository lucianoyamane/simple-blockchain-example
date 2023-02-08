package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class ValoresDeEntradaDiferenteValoresSaidaCondition extends Condition<TransactionValidate> {

    private ValoresDeEntradaDiferenteValoresSaidaCondition(TransactionValidate valida) {
        super(valida);
    }

    public static ValoresDeEntradaDiferenteValoresSaidaCondition inicia(TransactionValidate valida) {
        return new ValoresDeEntradaDiferenteValoresSaidaCondition(valida);
    }

    public Boolean isInputEqualOutputValue() {
        return this.getValida().getTransactionBlockChain().getUnspentValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getValida().getTransactionBlockChain().getCurrentTransactionOperationBlockChain().getValue() + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getValue();
    }

    @Override
    protected String getMessage() {
        return "Inputs are note equal to outputs on Transaction(" + this.getValida().getTransactionBlockChain().getTransaction().getHash() + ")";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.isInputEqualOutputValue();
    }
}
