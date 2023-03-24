package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

import java.util.Objects;

public class RegisteredAndCalculatedHashNotEqualsCondition extends Condition<BlockValidate> {

    private RegisteredAndCalculatedHashNotEqualsCondition(BlockValidate valida) {
        super(valida);
    }

    public static RegisteredAndCalculatedHashNotEqualsCondition init(BlockValidate valida) {
        return new RegisteredAndCalculatedHashNotEqualsCondition(valida);
    }

    public Boolean registeredAndCalculatedHashAreEquals() {
        if (Objects.isNull(this.getValidate().getCurrentBlockHash())) {
            return Boolean.FALSE;
        }
        return this.getValidate().getCurrentBlockHash().equals(this.getValidate().getCurrentBlockCalculatedHash());
    }

    @Override
    public String getMessage() {
        return "Current(" + this.getValidate().getCurrentBlockHash() + ") and Calculated(" + this.getValidate().getCurrentBlockCalculatedHash()   + ") are not equal.";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.registeredAndCalculatedHashAreEquals();

    }
}
