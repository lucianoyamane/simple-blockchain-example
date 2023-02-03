package br.com.lucianoyamane.example.valida.block.condicao;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.block.BlockValida;

public class HashNaoFoiMineradoCondicao extends Condicao<BlockValida> {

    private HashNaoFoiMineradoCondicao(BlockValida valida) {
        super(valida);
    }

    public static HashNaoFoiMineradoCondicao inicia(BlockValida valida) {
        return new HashNaoFoiMineradoCondicao(valida);
    }

    public Boolean hashIsSolved(int difficulty) {
        return this.getValida().getBlockBlockChain().getBlock().getHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if(!this.hashIsSolved(previousBlockData.getDifficulty())) {
            throw new BlockChainException("This block hasn't been mined");
        }
    }
}
