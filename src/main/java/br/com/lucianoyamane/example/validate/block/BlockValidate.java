package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockExecutor;
import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.block.condition.CurrentAndPreviousHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.block.condition.NotMineHashCondition;
import br.com.lucianoyamane.example.validate.block.condition.RegisteredAndCalculatedHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

import java.util.Objects;

public class BlockValidate extends Validate {

    private BlockExecutor blockExecutor;

    private BlockValidate(BlockExecutor blockExecutor) {
        this.setBlockBlockChain(blockExecutor);
        this.createValidate(blockExecutor);
    }

    @Override
    protected void configConditions() {
        this.addCondition(CurrentAndPreviousHashNotEqualsCondition.init(this));
        this.addCondition(RegisteredAndCalculatedHashNotEqualsCondition.init(this));
        this.addCondition(NotMineHashCondition.init(this));
    }

    public static BlockValidate validate(BlockExecutor blockExecutor) {
        return new BlockValidate(blockExecutor);
    }

    private BlockExecutor getBlockBlockChain() {
        return blockExecutor;
    }

    private void setBlockBlockChain(BlockExecutor blockExecutor) {
        this.blockExecutor = blockExecutor;
    }

    private void createValidate(BlockExecutor blockExecutor) {
        TransactionExecutor currentBlockTransaction = blockExecutor.getTransactionBlockChain();
        if (Objects.nonNull(currentBlockTransaction)) {
            this.addValidate(TransactionValidate.validate(currentBlockTransaction));
        }
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
    protected void processNextBlockData(BlockChainValidateApp.PreviousBlockData previousBlockData) {
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

    @Override
    protected String getLevel() {
        return "BLOCK";
    }

}
