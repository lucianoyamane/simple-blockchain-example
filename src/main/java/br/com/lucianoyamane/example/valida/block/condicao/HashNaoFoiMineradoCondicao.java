package br.com.lucianoyamane.example.valida.block.condicao;

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

    @Override
    public void executa(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if(!this.getValida().hashIsSolved(previousBlockData.getDifficulty())) {
            throw new BlockChainException("This block hasn't been mined");
        }
    }
}
