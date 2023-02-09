package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashNaoFoiMineradoCondicao extends Condicao<BlockValidate> {

    private HashNaoFoiMineradoCondicao(BlockValidate valida) {
        super(valida);
    }

    public static HashNaoFoiMineradoCondicao inicia(BlockValidate valida) {
        return new HashNaoFoiMineradoCondicao(valida);
    }

    public Boolean hashIsSolved(int difficulty) {
        return this.getValida().getCurrentBlockHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    protected String getMessage() {
        return "This block hasn't been mined";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.hashIsSolved(previousBlockData.getDifficulty());
    }
}
