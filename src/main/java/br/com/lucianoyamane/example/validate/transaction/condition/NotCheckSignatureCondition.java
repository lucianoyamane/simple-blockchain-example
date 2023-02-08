package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class NotCheckSignatureCondition extends Condition<TransactionValidate> {

    private NotCheckSignatureCondition(TransactionValidate valida) {
        super(valida);
    }

    @Override
    protected String getMessage() {
        return "Transaction " + this.getValida().getTransactionBlockChain().getHash()  + " Signature failed to verify";
    }

    public static NotCheckSignatureCondition inicia(TransactionValidate valida) {
        return new NotCheckSignatureCondition(valida);
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValida().getTransactionBlockChain().verifiySignature();
    }
}
