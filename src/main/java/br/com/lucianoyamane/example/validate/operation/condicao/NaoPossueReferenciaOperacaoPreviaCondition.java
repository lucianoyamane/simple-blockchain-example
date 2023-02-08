package br.com.lucianoyamane.example.validate.operation.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;

import java.util.Objects;

public class NaoPossueReferenciaOperacaoPreviaCondition extends Condition<OperationValidate> {

    private NaoPossueReferenciaOperacaoPreviaCondition(OperationValidate valida) {
        super(valida);
    }

    public static NaoPossueReferenciaOperacaoPreviaCondition inicia(OperationValidate valida) {
        return new NaoPossueReferenciaOperacaoPreviaCondition(valida);
    }

    @Override
    protected String getMessage() {
        return "#Referenced input on Transaction(" + this.getValida().getTransactionOperationBlockChain().getTransactionOperationId() + ") is Missing";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        OperationBlockChain referenceOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValida().getTransactionOperationBlockChain());
        return Objects.isNull(referenceOperationBlockChain);
    }
}
