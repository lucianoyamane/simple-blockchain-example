package br.com.lucianoyamane.example.validate.block.condition;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

import java.util.Objects;

public class NotMineHashCondition extends Condition<BlockValidate> {

    private NotMineHashCondition(BlockValidate valida) {
        super(valida);
    }

    public static NotMineHashCondition init(BlockValidate valida) {
        return new NotMineHashCondition(valida);
    }

    private Boolean hashIsMined(Integer difficulty) {
        if (Objects.isNull(this.getValidate().getCurrentBlockHash())) {
            return Boolean.FALSE;
        }
        return this.getValidate().getCurrentBlockHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    public String getMessage() {
        return "This block ("+ this.getValidate().getCurrentBlockHash()  +") hasn't been mined";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.hashIsMined(previousBlockData.getDifficulty());
    }
}
