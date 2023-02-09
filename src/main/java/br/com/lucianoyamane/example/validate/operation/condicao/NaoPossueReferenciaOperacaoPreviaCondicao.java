package br.com.lucianoyamane.example.validate.operation.condicao;

import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.operation.OperationValidate;

import java.util.Objects;

public class NaoPossueReferenciaOperacaoPreviaCondicao extends Condicao<OperationValidate> {

    private NaoPossueReferenciaOperacaoPreviaCondicao(OperationValidate valida) {
        super(valida);
    }

    public static NaoPossueReferenciaOperacaoPreviaCondicao inicia(OperationValidate valida) {
        return new NaoPossueReferenciaOperacaoPreviaCondicao(valida);
    }

    @Override
    protected String getMessage() {
        return "#Referenced input on Transaction(" + this.getValida().getTransactionOperationBlockChain().getTransactionOperationId() + ") is Missing";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        OperationBlockChain referenceOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getValida().getTransactionOperationBlockChain());
        return Objects.isNull(referenceOperationBlockChain);
    }
}
