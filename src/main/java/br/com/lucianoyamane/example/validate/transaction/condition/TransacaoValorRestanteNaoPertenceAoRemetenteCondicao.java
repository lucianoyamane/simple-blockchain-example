package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class TransacaoValorRestanteNaoPertenceAoRemetenteCondicao extends Condicao<TransactionValidate> {

    private TransacaoValorRestanteNaoPertenceAoRemetenteCondicao(TransactionValidate valida) {
        super(valida);
    }

    public static TransacaoValorRestanteNaoPertenceAoRemetenteCondicao inicia(TransactionValidate valida) {
        return new TransacaoValorRestanteNaoPertenceAoRemetenteCondicao(valida);
    }

    @Override
    protected String getMessage() {
        return "Operação (" + this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getTransactionOperationId() + ") não pertence ";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValida().getCurrentTransactionSenderPublicKeyDecorator().mePertence(this.getValida().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
