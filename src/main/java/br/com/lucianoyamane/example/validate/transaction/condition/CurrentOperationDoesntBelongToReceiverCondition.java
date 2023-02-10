package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class CurrentOperationDoesntBelongToReceiverCondition extends Condition<TransactionValidate> {

    private CurrentOperationDoesntBelongToReceiverCondition(TransactionValidate valida) {
        super(valida);
    }

    public static CurrentOperationDoesntBelongToReceiverCondition init(TransactionValidate valida) {
        return new CurrentOperationDoesntBelongToReceiverCondition(valida);
    }

    @Override
    protected String getMessage() {
        return "Looks like current Operation (" + this.getValidate().getCurrentTransactionOperationBlockChain().getTransactionOperationId() + ") don't belong to receiver";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValidate().getCurrentTransactionReceiverPublicKeyDecorator().mePertence(this.getValidate().getCurrentTransactionOperationBlockChain());
    }
}
