package br.com.lucianoyamane.example.validate.block;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.validate.BlockChainValidateApp;
import br.com.lucianoyamane.example.validate.Validate;
import br.com.lucianoyamane.example.validate.block.condicao.CurrentAndPreviousHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.block.condicao.NotMineHashCondition;
import br.com.lucianoyamane.example.validate.block.condicao.RegisteredAndCalculatedHashNotEqualsCondition;
import br.com.lucianoyamane.example.validate.transaction.TransactionValidate;

import java.util.Objects;

public class BlockValidate extends Validate {

    private BlockBlockChain blockBlockChain;

    private BlockValidate(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);
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
            this.addValidate(TransactionValidate.validate(currentBlockTransaction).execute(previousBlockData));
        }
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

    @Override
    protected String getLevel() {
        return "BLOCK";
    }

}
