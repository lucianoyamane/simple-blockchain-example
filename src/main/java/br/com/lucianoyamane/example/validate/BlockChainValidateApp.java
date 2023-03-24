package br.com.lucianoyamane.example.validate;

import br.com.lucianoyamane.example.blockchain.BlockBlockChain;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.blockchain.TransactionBlockChain;
import br.com.lucianoyamane.example.blockchain.OperationBlockChain;
import br.com.lucianoyamane.example.validate.block.BlockValidate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlockChainValidateApp {

    private BlockChainValidateApp() {
        this.setValidates(new ArrayList<>());
    }

    public static BlockChainValidateApp init() {
        return new BlockChainValidateApp();
    }

    private List<Validate> validates;

    public void validate(BlockBlockChain genesis, List<BlockBlockChain> blockchain) {
        PreviousBlockData previousBlockData = new PreviousBlockData(Difficulty.getInstance().getDifficulty());

        this.bootstrap(genesis, previousBlockData);

        for(BlockBlockChain currentBlockBlockChain : blockchain) {
            this.addValidate(BlockValidate.validate(currentBlockBlockChain).execute(previousBlockData));
        }
    }

    private void bootstrap(BlockBlockChain genesis, PreviousBlockData previousBlockData) {
        BlockBlockChain blockBlockChainGenesis = genesis;

        TransactionBlockChain transactionBlockChain = blockBlockChainGenesis.getTransactionBlockChain();
        if (Objects.nonNull(transactionBlockChain.getCurrentTransactionOperationBlockChain())) {
            previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getCurrentTransactionOperationBlockChain());
        }
        if (Objects.nonNull(transactionBlockChain.getLeftOverTransactionOperationBlockChain())) {
            previousBlockData.addTransactionOperationBlockChains(transactionBlockChain.getLeftOverTransactionOperationBlockChain());
        }
        previousBlockData.setPreviousHash(blockBlockChainGenesis.getHash());
    }

    public List<Map<String, String>> getErrorsMessages() {
        List<Map<String, String>> errorsMessages = new ArrayList<>();
        for (Validate validate : this.getValidates()) {
            errorsMessages.addAll(validate.getErrorsMessages());
        }
        return errorsMessages;
    }


    private List<Validate> getValidates() {
        return validates;
    }

    private void setValidates(List<Validate> validates) {
        this.validates = validates;
    }

    private void addValidate(Validate validate) {
        this.getValidates().add(validate);
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
