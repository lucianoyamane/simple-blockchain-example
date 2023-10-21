package br.com.lucianoyamane.example.validate;

import br.com.lucianoyamane.example.blockchain.BlockExecutor;
import br.com.lucianoyamane.example.configurations.Difficulty;
import br.com.lucianoyamane.example.blockchain.TransactionExecutor;
import br.com.lucianoyamane.example.blockchain.OperationExecutor;
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

    public void validate(BlockExecutor genesis, List<BlockExecutor> blockchain) {
        PreviousBlockData previousBlockData = new PreviousBlockData(Difficulty.getInstance().getDifficulty());

        this.bootstrap(genesis, previousBlockData);

        for(BlockExecutor currentBlockExecutor : blockchain) {
            this.addValidate(BlockValidate.validate(currentBlockExecutor).execute(previousBlockData));
        }
    }

    private void bootstrap(BlockExecutor genesis, PreviousBlockData previousBlockData) {
        BlockExecutor blockExecutorGenesis = genesis;

        TransactionExecutor transactionExecutor = blockExecutorGenesis.getTransactionBlockChain();
        if (Objects.nonNull(transactionExecutor.getCurrentTransactionOperationBlockChain())) {
            previousBlockData.addTransactionOperationBlockChains(transactionExecutor.getCurrentTransactionOperationBlockChain());
        }
        if (Objects.nonNull(transactionExecutor.getLeftOverTransactionOperationBlockChain())) {
            previousBlockData.addTransactionOperationBlockChains(transactionExecutor.getLeftOverTransactionOperationBlockChain());
        }
        previousBlockData.setPreviousHash(blockExecutorGenesis.getHash());
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

        private List<OperationExecutor> operationExecutors;

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

        public List<OperationExecutor> getTransactionOperationBlockChains() {
            return operationExecutors;
        }

        private void setTransactionOperationBlockChains(List<OperationExecutor> operationExecutors) {
            this.operationExecutors = operationExecutors;
        }

        public void addTransactionOperationBlockChains(OperationExecutor operationExecutor) {
            if (operationExecutor != null) {
                this.getTransactionOperationBlockChains().add(operationExecutor);
            }
        }

        public void removeTransactionOperationBlockChains(OperationExecutor operationExecutor) {
            this.getTransactionOperationBlockChains().remove(operationExecutor);
        }

        public OperationExecutor findReferencedTransactionOperationBlockChain(OperationExecutor operationExecutor) {
            return this.getTransactionOperationBlockChains().stream().filter(outputTemp -> outputTemp.equals(operationExecutor)).findFirst().orElse(null);
        }
    }
}
