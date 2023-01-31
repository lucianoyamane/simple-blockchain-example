package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;

public class BlockValida implements Valida{

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

    @Override
    public void isConsistent(BlockChainValida.PreviousBlockData previousBlockData) {
        if (!this.getBlockBlockChain().compareRegisteredAndCalculatedHash()) {
            throw new BlockChainException("Current Hashes not equal");
        }
        if (!this.getBlockBlockChain().compareHash(previousBlockData.getPreviousHash())) {
            throw new BlockChainException("Previous Hashes not equal");
        }
        if(!this.getBlockBlockChain().hashIsSolved(previousBlockData.getDifficulty())) {
            throw new BlockChainException("This block hasn't been mined");
        }

        TransactionBlockChain currentBlockTransaction = this.getBlockBlockChain().getTransactionBlockChain();
        if (currentBlockTransaction != null) {
            TransactionValida.valida(currentBlockTransaction).isConsistent(previousBlockData);
        }
        previousBlockData.setPreviousHash(blockBlockChain.getHash());
    }
}
