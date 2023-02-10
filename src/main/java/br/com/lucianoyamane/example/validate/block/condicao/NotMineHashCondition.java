package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class NotMineHashCondition extends Condition<BlockValidate> {

    private NotMineHashCondition(BlockValidate valida) {
        super(valida);
    }

    public static NotMineHashCondition init(BlockValidate valida) {
        return new NotMineHashCondition(valida);
    }

    public Boolean hashIsMined(int difficulty) {
        return this.getValidate().getCurrentBlockHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    protected String getMessage() {
        return "This block ("+ this.getValidate().getBlockBlockChain().getHash()  +") hasn't been mined";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.hashIsMined(previousBlockData.getDifficulty());
    }
}
