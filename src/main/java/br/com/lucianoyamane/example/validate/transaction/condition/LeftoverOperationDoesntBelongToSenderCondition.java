package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class LeftoverOperationDoesntBelongToSenderCondition extends Condition<TransactionValidate> {

    private LeftoverOperationDoesntBelongToSenderCondition(TransactionValidate valida) {
        super(valida);
    }

    public static LeftoverOperationDoesntBelongToSenderCondition init(TransactionValidate valida) {
        return new LeftoverOperationDoesntBelongToSenderCondition(valida);
    }

    @Override
    protected String getMessage() {
        return "Looks like Leftover Operation (" + this.getValidate().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getTransactionOperationId() + ") doesn't belong to sender  ";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValidate().getCurrentTransactionSenderPublicKeyDecorator().mePertence(this.getValidate().getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
