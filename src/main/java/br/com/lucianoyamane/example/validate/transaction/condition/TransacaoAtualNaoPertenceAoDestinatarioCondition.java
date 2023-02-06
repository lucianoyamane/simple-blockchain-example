package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class TransacaoAtualNaoPertenceAoDestinatarioCondition extends Condition<TransactionValidate> {

    private TransacaoAtualNaoPertenceAoDestinatarioCondition(TransactionValidate valida) {
        super(valida);
    }

    public static TransacaoAtualNaoPertenceAoDestinatarioCondition inicia(TransactionValidate valida) {
        return new TransacaoAtualNaoPertenceAoDestinatarioCondition(valida);
    }

    @Override
    protected void rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getCurrentTransactionReceiverPublicKeyDecorator().mePertence(this.getValida().getCurrentTransactionOperationBlockChain())) {
            throw new BlockChainException("#TransactionOutput(" + this.getValida().getCurrentTransactionOperationBlockChain() + ") is not who it should be");
        }
    }
}
