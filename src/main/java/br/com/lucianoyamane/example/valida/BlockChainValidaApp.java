package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.block.BlockBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;
import br.com.lucianoyamane.example.valida.block.BlockValida;

import java.util.ArrayList;
import java.util.List;

public class BlockChainValidaApp {

    public void isValid(BlockBlockChain genesis, List<BlockBlockChain> blockchain) {
        PreviousBlockData previousBlockData = new PreviousBlockData(Difficulty.getInstance().getDifficulty());

        this.bootstrapIsChainValid(genesis, previousBlockData);

        for(BlockBlockChain currentBlockBlockChain : blockchain) {
            BlockValida.valida(currentBlockBlockChain).executa(previousBlockData);

        }
        SystemOutPrintlnDecorator.roxo("Blockchain is valid");
    }

    private void bootstrapIsChainValid(BlockBlockChain genesis, PreviousBlockData previousBlockData) {
        BlockBlockChain blockBlockChainGenesis = genesis;

        TransactionBlockChain transactionBlockChain = blockBlockChainGenesis.getTransactionBlockChain();
        if (transactionBlockChain.getCurrentTransactionOperationBlockChain() != null) {
            previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getCurrentTransactionOperationBlockChain());
        }
        if (transactionBlockChain.getLeftOverTransactionOperationBlockChain() != null) {
            previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getLeftOverTransactionOperationBlockChain());
        }
        previousBlockData.setPreviousHash(blockBlockChainGenesis.getHash());
    }

    public class PreviousBlockData {

        private Integer difficulty;

        private String previousHash;

        private List<TransactionOperationBlockChain> transactionOperationBlockChains;

        public PreviousBlockData(Integer difficulty) {
            this.setTransactionOperationBlockChains(new ArrayList<>());
            this.setDifficulty(difficulty);
        }

        public Integer getDifficulty() {
            return difficulty;
        }

        private void setDifficulty(Integer difficulty) {
            this.difficulty = difficulty;
        }

        public String getPreviousHash() {
            return previousHash;
        }

        public void setPreviousHash(String previousHash) {
            this.previousHash = previousHash;
        }

        public List<TransactionOperationBlockChain> getTransactionOperationBlockChains() {
            return transactionOperationBlockChains;
        }

        private void setTransactionOperationBlockChains(List<TransactionOperationBlockChain> transactionOperationBlockChains) {
            this.transactionOperationBlockChains = transactionOperationBlockChains;
        }

        public void addTransactionOperationBlockChains(TransactionOperationBlockChain transactionOperationBlockChain) {
            if (transactionOperationBlockChain != null) {
                this.getTransactionOperationBlockChains().add(transactionOperationBlockChain);
            }
        }

        public void removeTransactionOperationBlockChains(TransactionOperationBlockChain transactionOperationBlockChain) {
            this.getTransactionOperationBlockChains().remove(transactionOperationBlockChain);
        }

        public TransactionOperationBlockChain findReferencedTransactionOperationBlockChain(TransactionOperationBlockChain transactionOperationBlockChain) {
            return this.getTransactionOperationBlockChains().stream().filter(outputTemp -> outputTemp.equals(transactionOperationBlockChain)).findFirst().orElse(null);
        }
    }
}
