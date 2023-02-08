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
    protected String getMessage() {
        return "Current Hashes not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.compareRegisteredAndCalculatedHash();

    }
}
