package br.com.lucianoyamane.example.validate.operation;

import br.com.lucianoyamane.example.blockchain.OperationExecutor;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.operation.condition.PreviousOperationReferenceNotExistsCondition;
import br.com.lucianoyamane.example.validate.operation.condition.PreviousReferenceValueInvalidCondition;

public class OperationValidate extends Validate {

    private OperationExecutor operationExecutor;

    public static OperationValidate validate(OperationExecutor operationExecutor) {
        return new OperationValidate(operationExecutor);
    }

    @Override
    protected void configConditions() {
        this.addCondition(PreviousReferenceValueInvalidCondition.init(this));
        this.addCondition(PreviousOperationReferenceNotExistsCondition.init(this));
    }

    private OperationValidate(OperationExecutor operationExecutor) {
        this.setTransactionOperationBlockChain(operationExecutor);
    }

    public OperationExecutor getTransactionOperationBlockChain() {
        return operationExecutor;
    }

    public Integer getTransactionOperationBlockChainValue() {
        return this.getTransactionOperationBlockChain().getTransactionOperationValue();
    }

    private void setTransactionOperationBlockChain(OperationExecutor operationExecutor) {
        this.operationExecutor = operationExecutor;
    }

    @Override
    public void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        previousBlockData.removeTransactionOperationBlockChains(this.getTransactionOperationBlockChain());
    }

    @Override
    protected String getLevel() {
        return "OPERATION";
    }
}
