package br.com.lucianoyamane.example.validate.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;
import br.com.lucianoyamane.example.validate.transaction.condition.InputAndOutputValuesNotEqualsCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.NotCheckedSignatureCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.CurrentOperationDoesntBelongToReceiverCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.LeftoverOperationDoesntBelongToSenderCondition;

import java.util.List;

public class TransactionValidate extends Validate {

    private TransactionBlockChain transactionBlockChain;

    private TransactionValidate(TransactionBlockChain transactionBlockChain) {
        this.setTransactionBlockChain(transactionBlockChain);
        this.createValidates(transactionBlockChain);
    }

    private void createValidates(TransactionBlockChain transactionBlockChain) {
        List<OperationBlockChain> transactionsOperationBlockChain = transactionBlockChain.getUnspentTransactionsOperationBlockChain();

        for(OperationBlockChain operationBlockChain : transactionsOperationBlockChain) {
            this.addValidate(OperationValidate.validate(operationBlockChain));
        }

    }

    @Override
    protected void configConditions() {
        this.addCondition(NotCheckedSignatureCondition.init(this));
        this.addCondition(CurrentOperationDoesntBelongToReceiverCondition.init(this));
        this.addCondition(LeftoverOperationDoesntBelongToSenderCondition.init(this));
        this.addCondition(InputAndOutputValuesNotEqualsCondition.init(this));
    }

    public static TransactionValidate validate(TransactionBlockChain transactionBlockChain) {
        return new TransactionValidate(transactionBlockChain);
    }

    public TransactionBlockChain getTransactionBlockChain() {
        return transactionBlockChain;
    }

    public PublicKeyDecorator getCurrentTransactionReceiverPublicKeyDecorator(){
        return this.getTransactionBlockChain().getTransactionReceiverPublicKeyDecorator();
    }

    public PublicKeyDecorator getCurrentTransactionSenderPublicKeyDecorator(){
        return this.getTransactionBlockChain().getTransactionSenderPublicKeyDecorator();
    }

    public OperationBlockChain getCurrentTransactionOperationBlockChain(){
        return this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain();
    }

    private void setTransactionBlockChain(TransactionBlockChain transactionBlockChain) {
        this.transactionBlockChain = transactionBlockChain;
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
