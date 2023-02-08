package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condition;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashAtualDiferenteDoAnteriorCondition extends Condition<BlockValidate> {

    private HashAtualDiferenteDoAnteriorCondition(BlockValidate valida) {
        super(valida);
    }

    public static HashAtualDiferenteDoAnteriorCondition inicia(BlockValidate valida) {
        return new HashAtualDiferenteDoAnteriorCondition(valida);
    }

    public Boolean compareWithCurrentBlockPreviousHash(String previousHash) {
        return this.getValida().getCurrentBlockPreviousHash().equals(previousHash);
    }

    @Override
    protected String getMessage() {
        return "Previous Hashes not equal";
    }

    @Override
    protected Boolean rule(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        return !this.compareWithCurrentBlockPreviousHash(previousBlockData.getPreviousHash());
    }
}
