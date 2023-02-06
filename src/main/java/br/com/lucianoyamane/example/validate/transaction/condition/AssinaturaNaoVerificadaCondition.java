package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class AssinaturaNaoVerificadaCondition extends Condition<TransactionValidate> {

    private AssinaturaNaoVerificadaCondition(TransactionValidate valida) {
        super(valida);
    }

    public static AssinaturaNaoVerificadaCondition inicia(TransactionValidate valida) {
        return new AssinaturaNaoVerificadaCondition(valida);
    }

    @Override
    protected void definicao(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getTransactionBlockChain().verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }
    }
}
