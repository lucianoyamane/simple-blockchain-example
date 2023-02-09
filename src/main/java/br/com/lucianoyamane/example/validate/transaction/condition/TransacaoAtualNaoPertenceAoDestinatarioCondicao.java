package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class TransacaoAtualNaoPertenceAoDestinatarioCondicao extends Condicao<TransactionValidate> {

    private TransacaoAtualNaoPertenceAoDestinatarioCondicao(TransactionValidate valida) {
        super(valida);
    }

    public static TransacaoAtualNaoPertenceAoDestinatarioCondicao inicia(TransactionValidate valida) {
        return new TransacaoAtualNaoPertenceAoDestinatarioCondicao(valida);
    }

    @Override
    protected String getMessage() {
        return "Operação (" + this.getValida().getCurrentTransactionOperationBlockChain().getTransactionOperationId() + ") nao pertence ao destinatario";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValida().getCurrentTransactionReceiverPublicKeyDecorator().mePertence(this.getValida().getCurrentTransactionOperationBlockChain());
    }
}
