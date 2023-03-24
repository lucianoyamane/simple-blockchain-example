package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.block.condition.CurrentAndPreviousHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.block.condition.NotMineHashCondition;
import br.com.lucianoyamane.example.validate.block.condition.RegisteredAndCalculatedHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

import java.util.Objects;

public class BlockValidate extends Validate {

    private BlockBlockChain blockBlockChain;

    private BlockValidate(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);
        this.createValidate(blockBlockChain);
    }

    @Override
    protected void configConditions() {
        this.addCondition(CurrentAndPreviousHashNotEqualsCondition.init(this));
        this.addCondition(RegisteredAndCalculatedHashNotEqualsCondition.init(this));
        this.addCondition(NotMineHashCondition.init(this));
    }

    public static BlockValidate validate(BlockBlockChain blockBlockChain) {
        return new BlockValidate(blockBlockChain);
    }

    private BlockBlockChain getBlockBlockChain() {
        return blockBlockChain;
    }

    private void setBlockBlockChain(BlockBlockChain blockBlockChain) {
        this.blockBlockChain = blockBlockChain;
    }

    private void createValidate(BlockBlockChain blockBlockChain) {
        TransactionBlockChain currentBlockTransaction = blockBlockChain.getTransactionBlockChain();
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
