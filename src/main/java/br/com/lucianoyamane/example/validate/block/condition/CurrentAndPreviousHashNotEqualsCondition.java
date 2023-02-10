package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class CurrentAndPreviousHashNotEqualsCondition extends Condition<BlockValidate> {

    private CurrentAndPreviousHashNotEqualsCondition(BlockValidate valida) {
        super(valida);
    }

    public static CurrentAndPreviousHashNotEqualsCondition init(BlockValidate valida) {
        return new CurrentAndPreviousHashNotEqualsCondition(valida);
    }

    public Boolean compareWithCurrentBlockPreviousHash(String previousHash) {
        return this.getValidate().getCurrentBlockPreviousHash().equals(previousHash);
    }

    @Override
    protected String getMessage() {
        return "Previous Hashes not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.compareWithCurrentBlockPreviousHash(previousBlockData.getPreviousHash());
    }
}
