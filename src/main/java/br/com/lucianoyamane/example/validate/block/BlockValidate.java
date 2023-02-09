package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.block.condicao.HashAtualDiferenteDoAnteriorCondicao;
import br.com.lucianoyamane.example.validate.block.condicao.HashNaoFoiMineradoCondicao;
import br.com.lucianoyamane.example.validate.block.condicao.HashRegistradoDiferenteCalculadoBlockCondicao;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

import java.util.Objects;

public class BlockValidate extends Validate {

    private BlockBlockChain blockBlockChain;

    private BlockValidate(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);
    }

    @Override
    protected void configConditions() {
        this.addCondition(HashAtualDiferenteDoAnteriorCondicao.inicia(this));
        this.addCondition(HashRegistradoDiferenteCalculadoBlockCondicao.inicia(this));
        this.addCondition(HashNaoFoiMineradoCondicao.inicia(this));
    }

    public static BlockValidate valida(BlockBlockChain blockBlockChain) {
        return new BlockValidate(blockBlockChain);
    }

    public BlockBlockChain getBlockBlockChain() {
        return blockBlockChain;
    }

    private void setBlockBlockChain(BlockBlockChain blockBlockChain) {
        this.blockBlockChain = blockBlockChain;
    }

    public String getCurrentBlockPreviousHash() {
        return this.getBlockBlockChain().getPreviousHash();
    }

    public String getCurrentBlockHash() {
        return this.getBlockBlockChain().getHash();
    }

    public String getCurrentBlockCalculatedHash() {
        return this.getBlockBlockChain().calculateHash();
    }

    @Override
    public void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        TransactionBlockChain currentBlockTransaction = this.getBlockBlockChain().getTransactionBlockChain();
        if (Objects.nonNull(currentBlockTransaction)) {
            this.addValidate(TransactionValidate.valida(currentBlockTransaction).execute(previousBlockData));
        }
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

    @Override
    protected String getLevel() {
        return "BLOCK";
    }

}
