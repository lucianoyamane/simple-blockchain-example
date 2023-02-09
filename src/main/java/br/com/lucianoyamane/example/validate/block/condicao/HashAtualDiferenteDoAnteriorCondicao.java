package br.com.lucianoyamane.example.validate.block.condicao;

import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Condicao;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

public class HashAtualDiferenteDoAnteriorCondicao extends Condicao<BlockValidate> {

    private HashAtualDiferenteDoAnteriorCondicao(BlockValidate valida) {
        super(valida);
    }

    public static HashAtualDiferenteDoAnteriorCondicao inicia(BlockValidate valida) {
        return new HashAtualDiferenteDoAnteriorCondicao(valida);
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
