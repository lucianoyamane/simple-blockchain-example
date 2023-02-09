package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashRegistradoDiferenteCalculadoBlockCondicao extends Condicao<BlockValidate> {

    private HashRegistradoDiferenteCalculadoBlockCondicao(BlockValidate valida) {
        super(valida);
    }

    public static HashRegistradoDiferenteCalculadoBlockCondicao inicia(BlockValidate valida) {
        return new HashRegistradoDiferenteCalculadoBlockCondicao(valida);
    }

    public Boolean compareRegisteredAndCalculatedHash() {
        return this.getValida().getCurrentBlockHash().equals(this.getValida().getCurrentBlockCalculatedHash());
    }

    @Override
    protected String getMessage() {
        return "Current Hashes not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.compareRegisteredAndCalculatedHash();

    }
}
