package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class NotCheckSignatureCondition extends Condition<TransactionValidate> {

    private NotCheckSignatureCondition(TransactionValidate valida) {
        super(valida);
    }

    public static NotCheckSignatureCondition inicia(TransactionValidate valida) {
        return new NotCheckSignatureCondition(valida);
    }

    @Override
    protected void rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().getTransactionBlockChain().verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }
    }
}
