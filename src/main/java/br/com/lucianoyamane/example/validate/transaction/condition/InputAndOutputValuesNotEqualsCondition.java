package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class InputAndOutputValuesNotEqualsCondition extends Condition<TransactionValidate> {

    private InputAndOutputValuesNotEqualsCondition(TransactionValidate valida) {
        super(valida);
    }

    public static InputAndOutputValuesNotEqualsCondition init(TransactionValidate valida) {
        return new InputAndOutputValuesNotEqualsCondition(valida);
    }

    public Boolean isInputEqualOutputValue() {
        return this.getValidate().getTransactionBlockChain().getUnspentValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getValidate().getTransactionBlockChain().getCurrentTransactionOperationBlockChain().getValue() + this.getValidate().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getValue();
    }

    @Override
    protected String getMessage() {
        return "Input and Output value are not equals(" + this.getValidate().getTransactionBlockChain().getTransaction().getHash() + ")";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.isInputEqualOutputValue();
    }
}
