package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.block.condicao.HashAtualDiferenteDoAnteriorCondition;
import br.com.lucianoyamane.example.validate.block.condicao.HashNaoFoiMineradoCondition;
import br.com.lucianoyamane.example.validate.block.condicao.HashRegistradoDiferenteCalculadoBlockCondition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

import java.util.Objects;

public class BlockValidate extends Validate {

    private BlockBlockChain blockBlockChain;

    private BlockValidate(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);
    }

    @Override
    protected void setConditions() {
        this.addCondition(HashAtualDiferenteDoAnteriorCondition.inicia(this));
        this.addCondition(HashRegistradoDiferenteCalculadoBlockCondition.inicia(this));
        this.addCondition(HashNaoFoiMineradoCondition.inicia(this));
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
            TransactionValidate.valida(currentBlockTransaction).execute(previousBlockData);
        }
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

}
