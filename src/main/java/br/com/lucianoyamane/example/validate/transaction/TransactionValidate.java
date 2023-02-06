package br.com.lucianoyamane.example.validate.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;
import br.com.lucianoyamane.example.validate.transaction.condition.AssinaturaNaoVerificadaCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.TransacaoAtualNaoPertenceAoDestinatarioCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.TransacaoValorRestanteNaoPertenceAoRemetenteCondition;
import br.com.lucianoyamane.example.validate.transaction.condition.ValoresDeEntradaDiferenteValoresSaidaCondition;

import java.util.List;

public class TransactionValidate extends Validate {

    private TransactionBlockChain transactionBlockChain;

    private TransactionValidate(TransactionBlockChain transactionBlockChain) {
        this.setTransactionBlockChain(transactionBlockChain);
    }

    @Override
    protected void setConditions() {
        this.addCondition(AssinaturaNaoVerificadaCondition.inicia(this));
        this.addCondition(TransacaoAtualNaoPertenceAoDestinatarioCondition.inicia(this));
        this.addCondition(TransacaoValorRestanteNaoPertenceAoRemetenteCondition.inicia(this));
        this.addCondition(ValoresDeEntradaDiferenteValoresSaidaCondition.inicia(this));
    }

    public static TransactionValidate valida(TransactionBlockChain transactionBlockChain) {
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
    public void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        List<OperationBlockChain> transactionsOperationBlockChain = this.getTransactionBlockChain().getUnspentTransactionsOperationBlockChain();

        for(OperationBlockChain output : transactionsOperationBlockChain) {
            OperationValidate.valida(output).execute(previousBlockData);
        }

        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
