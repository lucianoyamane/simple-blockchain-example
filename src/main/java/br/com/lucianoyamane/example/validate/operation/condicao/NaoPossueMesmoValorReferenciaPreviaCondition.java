package br.com.lucianoyamane.example.validate.operation.condicao;

import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;

public class NaoPossueMesmoValorReferenciaPreviaCondition extends Condition<OperationValidate> {

    private NaoPossueMesmoValorReferenciaPreviaCondition(OperationValidate valida) {
        super(valida);
    }

    public static NaoPossueMesmoValorReferenciaPreviaCondition inicia(OperationValidate valida) {
        return new NaoPossueMesmoValorReferenciaPreviaCondition(valida);
    }

    public Boolean possueMesmoValor(OperationBlockChain previousReferenceOperationBlockChain) {
        return this.getValida().getTransactionOperationBlockChainValue().equals(previousReferenceOperationBlockChain.getValue());
    }

    @Override
    protected String getMessage() {
        return "#Referenced input Transaction(" + this.getValida().getTransactionOperationBlockChain().getTransactionOperationId() + ") value is Invalid";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        OperationBlockChain previousReferenceOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValida().getTransactionOperationBlockChain());
        return !this.possueMesmoValor(previousReferenceOperationBlockChain);
    }
}
