package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

import java.util.Objects;

public final class CurrentAndPreviousHashNotEqualsCondition extends Condition<BlockValidate> {

    private CurrentAndPreviousHashNotEqualsCondition(BlockValidate validate) {
        super(validate);
    }

    public static CurrentAndPreviousHashNotEqualsCondition init(BlockValidate validate) {
        return new CurrentAndPreviousHashNotEqualsCondition(validate);
    }

    private Boolean currentBlockPreviousHashAreEquals(String previousHash) {
        if (Objects.isNull(this.getValidate().getCurrentBlockPreviousHash())) {
            return Boolean.FALSE;
        }
        return this.getValidate().getCurrentBlockPreviousHash().equals(previousHash);
    }

    @Override
    public String getMessage() {
        return "Previous Hashes not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.currentBlockPreviousHashAreEquals(previousBlockData.getPreviousHash());
    }
}
