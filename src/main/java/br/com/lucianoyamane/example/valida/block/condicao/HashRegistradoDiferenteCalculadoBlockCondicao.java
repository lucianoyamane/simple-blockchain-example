package br.com.lucianoyamane.example.valida.block.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.valida.BlockChainValidaApp;
import br.com.lucianoyamane.example.valida.Condicao;
import br.com.lucianoyamane.example.valida.block.BlockValida;

public class HashRegistradoDiferenteCalculadoBlockCondicao extends Condicao<BlockValida> {

    private HashRegistradoDiferenteCalculadoBlockCondicao(BlockValida valida) {
        super(valida);
    }

    public static HashRegistradoDiferenteCalculadoBlockCondicao inicia(BlockValida valida) {
        return new HashRegistradoDiferenteCalculadoBlockCondicao(valida);
    }

    @Override
    protected void definicao(BlockChainValidaApp.PreviousBlockData previousBlockData) {
        if (!this.getValida().compareRegisteredAndCalculatedHash()) {
            throw new BlockChainException("Current Hashes not equal");
        }

    }
}
