package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashRegistradoDiferenteCalculadoBlockCondition extends Condition<BlockValidate> {

    private HashRegistradoDiferenteCalculadoBlockCondition(BlockValidate valida) {
        super(valida);
    }

    public static HashRegistradoDiferenteCalculadoBlockCondition inicia(BlockValidate valida) {
        return new HashRegistradoDiferenteCalculadoBlockCondition(valida);
    }

    public Boolean compareRegisteredAndCalculatedHash() {
        return this.getValida().getCurrentBlockHash().equals(this.getValida().getCurrentBlockCalculatedHash());
    }

    @Override
    protected void definicao(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        if (!this.compareRegisteredAndCalculatedHash()) {
            throw new BlockChainException("Current Hashes not equal");
        }

    }
}
