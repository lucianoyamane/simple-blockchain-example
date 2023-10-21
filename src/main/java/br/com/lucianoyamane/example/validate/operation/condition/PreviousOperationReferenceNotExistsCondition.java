package br.com.lucianoyamane.example.validate.operation.condition;

import br.com.lucianoyamane.example.blockchain.OperationExecutor;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;

import java.util.Objects;

public class PreviousOperationReferenceNotExistsCondition extends Condition<OperationValidate> {

    private PreviousOperationReferenceNotExistsCondition(OperationValidate valida) {
        super(valida);
    }

    public static PreviousOperationReferenceNotExistsCondition init(OperationValidate valida) {
        return new PreviousOperationReferenceNotExistsCondition(valida);
    }

    @Override
    public String getMessage() {
        return "#Referenced operation on Transaction(" + this.getValidate().getTransactionOperationBlockChain().getTransactionOperationId() + ") is Missing";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        OperationExecutor referenceOperationExecutor = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValidate().getTransactionOperationBlockChain());
        return Objects.isNull(referenceOperationExecutor);
    }
}
