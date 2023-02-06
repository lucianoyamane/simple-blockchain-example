package br.com.lucianoyamane.example.validate;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.configurations.SystemOutPrintlnDecorator;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

import java.util.ArrayList;
import java.util.List;

public class BlockChainValidateApp {

    public void validate(BlockBlockChain genesis, List<BlockBlockChain> blockchain) {
        PreviousBlockData previousBlockData = new PreviousBlockData(Difficulty.getInstance().getDifficulty());

        this.bootstrap(genesis, previousBlockData);

        for(BlockBlockChain currentBlockBlockChain : blockchain) {
            BlockValidate.valida(currentBlockBlockChain).execute(previousBlockData);

        }
        SystemOutPrintlnDecorator.roxo("Blockchain is valid");
    }

    private void bootstrap(BlockBlockChain genesis, PreviousBlockData previousBlockData) {
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

        private List<OperationBlockChain> operationBlockChains;

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

        public List<OperationBlockChain> getTransactionOperationBlockChains() {
            return operationBlockChains;
        }

        private void setTransactionOperationBlockChains(List<OperationBlockChain> operationBlockChains) {
            this.operationBlockChains = operationBlockChains;
        }

        public void addTransactionOperationBlockChains(OperationBlockChain operationBlockChain) {
            if (operationBlockChain != null) {
                this.getTransactionOperationBlockChains().add(operationBlockChain);
            }
        }

        public void removeTransactionOperationBlockChains(OperationBlockChain operationBlockChain) {
            this.getTransactionOperationBlockChains().remove(operationBlockChain);
        }

        public OperationBlockChain findReferencedTransactionOperationBlockChain(OperationBlockChain operationBlockChain) {
            return this.getTransactionOperationBlockChains().stream().filter(outputTemp -> outputTemp.equals(operationBlockChain)).findFirst().orElse(null);
        }
    }
}
