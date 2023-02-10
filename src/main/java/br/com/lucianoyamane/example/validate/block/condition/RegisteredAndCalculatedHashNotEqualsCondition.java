package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class RegisteredAndCalculatedHashNotEqualsCondition extends Condition<BlockValidate> {

    private RegisteredAndCalculatedHashNotEqualsCondition(BlockValidate valida) {
        super(valida);
    }

    public static RegisteredAndCalculatedHashNotEqualsCondition init(BlockValidate valida) {
        return new RegisteredAndCalculatedHashNotEqualsCondition(valida);
    }

    public Boolean compareRegisteredAndCalculatedHash() {
        return this.getValidate().getCurrentBlockHash().equals(this.getValidate().getCurrentBlockCalculatedHash());
    }

    @Override
    protected String getMessage() {
        return "Current Hash(" + this.getValidate().getCurrentBlockHash() + ") and Calculated Hash(" + this.getValidate().getCurrentBlockCalculatedHash()   + ") not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.compareRegisteredAndCalculatedHash();

    }
}
