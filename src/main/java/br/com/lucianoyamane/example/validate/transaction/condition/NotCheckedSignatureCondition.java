package br.com.lucianoyamane.example.validate.transaction.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

public class NotCheckedSignatureCondition extends Condition<TransactionValidate> {

    private NotCheckedSignatureCondition(TransactionValidate valida) {
        super(valida);
    }

    public static NotCheckedSignatureCondition init(TransactionValidate valida) {
        return new NotCheckedSignatureCondition(valida);
    }

    @Override
    protected String getMessage() {
        return "Signature not valid on Transaction( " + this.getValidate().getTransactionBlockChain().getHash() + ")";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.getValidate().getTransactionBlockChain().verifiySignature();
    }
}
