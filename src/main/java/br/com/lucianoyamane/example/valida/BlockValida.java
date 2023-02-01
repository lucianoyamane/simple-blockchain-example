package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;

import java.util.Objects;

public class BlockValida extends Valida {

    private BlockBlockChain blockBlockChain;

    private BlockValida(BlockBlockChain blockBlockChain) {
        this.setBlockBlockChain(blockBlockChain);;
    }

    public static BlockValida valida(BlockBlockChain blockBlockChain) {
        return new BlockValida(blockBlockChain);
    }

    private BlockBlockChain getBlockBlockChain() {
        return blockBlockChain;
    }

    private void setBlockBlockChain(BlockBlockChain blockBlockChain) {
        this.blockBlockChain = blockBlockChain;
    }

    public Boolean compareRegisteredAndCalculatedHash() {
        return this.getBlockBlockChain().getBlock().getHash().equals(this.getBlockBlockChain().calculateHash());
    }
    public Boolean compareWithPreviousHash(String previousHash) {
        return this.getBlockBlockChain().getBlock().getPreviousHash().equals(previousHash);
    }

    public Boolean hashIsSolved(int difficulty) {
        return this.getBlockBlockChain().getBlock().getHash().substring( 0, difficulty).equals(StringUtil.getCharsZeroByDifficuty(difficulty));
    }

    @Override
    void valida(BlockChainValida.PreviousBlockData previousBlockData) {
        if (!this.compareRegisteredAndCalculatedHash()) {
            throw new BlockChainException("Current Hashes not equal");
        }
        if (!this.compareWithPreviousHash(previousBlockData.getPreviousHash())) {
            throw new BlockChainException("Previous Hashes not equal");
        }
        if(!this.hashIsSolved(previousBlockData.getDifficulty())) {
            throw new BlockChainException("This block hasn't been mined");
        }
    }

    @Override
    void processaDadosProximoBloco(BlockChainValida.PreviousBlockData previousBlockData) {
        TransactionBlockChain currentBlockTransaction = this.getBlockBlockChain().getTransactionBlockChain();
        if (Objects.nonNull(currentBlockTransaction)) {
            TransactionValida.valida(currentBlockTransaction).executa(previousBlockData);
        }
        previousBlockData.setPreviousHash(this.getBlockBlockChain().getHash());
    }

}
