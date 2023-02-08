package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class TransacaoValorRestanteNaoPertenceAoRemetenteCondition extends Condition<TransactionValidate> {

    private TransacaoValorRestanteNaoPertenceAoRemetenteCondition(TransactionValidate valida) {
        super(valida);
    }

    public static TransacaoValorRestanteNaoPertenceAoRemetenteCondition inicia(TransactionValidate valida) {
        return new TransacaoValorRestanteNaoPertenceAoRemetenteCondition(valida);
    }

    @Override
    protected String getMessage() {
        return "#TransactionOutput(" + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain() + ") is not who it should be";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValida().getCurrentTransactionSenderPublicKeyDecorator().mePertence(this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
