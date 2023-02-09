package br.com.lucianoyamane.example.validate.operation;

import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.operation.condicao.NaoPossueMesmoValorReferenciaPreviaCondicao;
import br.com.lucianoyamane.example.validate.operation.condicao.NaoPossueReferenciaOperacaoPreviaCondicao;

public class OperationValidate extends Validate {

    private OperationBlockChain operationBlockChain;

    public static OperationValidate validate(OperationBlockChain operationBlockChain) {
        return new OperationValidate(operationBlockChain);
    }

    @Override
    protected void configConditions() {
        this.addCondition(NaoPossueMesmoValorReferenciaPreviaCondicao.inicia(this));
        this.addCondition(NaoPossueReferenciaOperacaoPreviaCondicao.inicia(this));
    }

    private OperationValidate(OperationBlockChain operationBlockChain) {
        this.setTransactionOperationBlockChain(operationBlockChain);
    }

    public OperationBlockChain getTransactionOperationBlockChain() {
        return operationBlockChain;
    }

    public Integer getTransactionOperationBlockChainValue() {
        return this.getTransactionOperationBlockChain().getTransactionOperationValue();
    }

    private void setTransactionOperationBlockChain(OperationBlockChain operationBlockChain) {
        this.operationBlockChain = operationBlockChain;
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
