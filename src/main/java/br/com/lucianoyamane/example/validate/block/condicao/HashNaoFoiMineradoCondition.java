package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashNaoFoiMineradoCondition extends Condition<BlockValidate> {

    private HashNaoFoiMineradoCondition(BlockValidate valida) {
        super(valida);
    }

    public static HashNaoFoiMineradoCondition inicia(BlockValidate valida) {
        return new HashNaoFoiMineradoCondition(valida);
    }

    public Boolean hashIsSolved(int difficulty) {
        return this.getValida().getCurrentBlockHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    protected void rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        if(!this.hashIsSolved(previousBlockData.getDifficulty())) {
            throw new BlockChainException("This block hasn't been mined");
        }
    }
}
