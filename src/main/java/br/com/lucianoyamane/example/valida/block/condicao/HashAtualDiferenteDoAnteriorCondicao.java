package br.com.lucianoyamane.example.valida.block.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.block.BlockValida;

public class HashAtualDiferenteDoAnteriorCondicao extends Condicao<BlockValida> {

    private HashAtualDiferenteDoAnteriorCondicao(BlockValida valida) {
        super(valida);
    }

    public static HashAtualDiferenteDoAnteriorCondicao inicia(BlockValida valida) {
        return new HashAtualDiferenteDoAnteriorCondicao(valida);
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().compareWithPreviousHash(previousBlockData.getPreviousHash())){
            throw new BlockChainException("Previous Hashes not equal");
        }
    }
}
