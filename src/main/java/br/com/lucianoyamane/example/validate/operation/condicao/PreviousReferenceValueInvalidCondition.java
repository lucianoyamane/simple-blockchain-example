package br.com.lucianoyamane.example.validate.operation.condicao;

import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;

public class PreviousReferenceValueInvalidCondition extends Condition<OperationValidate> {

    private PreviousReferenceValueInvalidCondition(OperationValidate valida) {
        super(valida);
    }

    public static PreviousReferenceValueInvalidCondition init(OperationValidate valida) {
        return new PreviousReferenceValueInvalidCondition(valida);
    }

    public Boolean possueMesmoValor(OperationBlockChain previousReferenceOperationBlockChain) {
        return this.getValidate().getTransactionOperationBlockChainValue().equals(previousReferenceOperationBlockChain.getValue());
    }

    @Override
    protected String getMessage() {
        return "#Referenced operation on Transaction(" + this.getValidate().getTransactionOperationBlockChain().getTransactionOperationId() + ") value is Invalid";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        OperationBlockChain previousReferenceOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValidate().getTransactionOperationBlockChain());
        return !this.possueMesmoValor(previousReferenceOperationBlockChain);
    }
}
