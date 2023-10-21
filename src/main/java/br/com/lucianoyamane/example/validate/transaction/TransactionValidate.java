package br.com.lucianoyamane.example.validate.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.blockchain.OperationExecutor;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;
import br.com.lucianoyamane.example.validate.transaction.condition.InputAndOutputValuesNotEqualsCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.NotCheckedSignatureCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.CurrentOperationDoesntBelongToReceiverCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.LeftoverOperationDoesntBelongToSenderCondition;

import java.util.List;

public class TransactionValidate extends Validate {

    private TransactionExecutor transactionExecutor;

    private TransactionValidate(TransactionExecutor transactionExecutor) {
        this.setTransactionBlockChain(transactionExecutor);
        this.createValidates(transactionExecutor);
    }

    private void createValidates(TransactionExecutor transactionExecutor) {
        List<OperationExecutor> transactionsOperationExecutor = transactionExecutor.getUnspentTransactionsOperationBlockChain();

        for(OperationExecutor operationExecutor : transactionsOperationExecutor) {
            this.addValidate(OperationValidate.validate(operationExecutor));
        }

    }

    @Override
    protected void configConditions() {
        this.addCondition(NotCheckedSignatureCondition.init(this));
        this.addCondition(CurrentOperationDoesntBelongToReceiverCondition.init(this));
        this.addCondition(LeftoverOperationDoesntBelongToSenderCondition.init(this));
        this.addCondition(InputAndOutputValuesNotEqualsCondition.init(this));
    }

    public static TransactionValidate validate(TransactionExecutor transactionExecutor) {
        return new TransactionValidate(transactionExecutor);
    }

    public TransactionExecutor getTransactionBlockChain() {
        return transactionExecutor;
    }

    public PublicKeyDecorator getCurrentTransactionReceiverPublicKeyDecorator(){
        return this.getTransactionBlockChain().getTransactionReceiverPublicKeyDecorator();
    }

    public PublicKeyDecorator getCurrentTransactionSenderPublicKeyDecorator(){
        return this.getTransactionBlockChain().getTransactionSenderPublicKeyDecorator();
    }

    public OperationExecutor getCurrentTransactionOperationBlockChain(){
        return this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain();
    }

    private void setTransactionBlockChain(TransactionExecutor transactionExecutor) {
        this.transactionExecutor = transactionExecutor;
    }

    @Override
    protected void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }

    @Override
    protected String getLevel() {
        return "TRANSACTION";
    }
}
