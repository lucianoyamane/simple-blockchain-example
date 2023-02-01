package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.List;

public class TransactionValida extends Valida {

    private TransactionBlockChain transactionBlockChain;

    private TransactionValida(TransactionBlockChain transactionBlockChain) {
        this.setTransactionBlockChain(transactionBlockChain);
    }

    public static TransactionValida valida(TransactionBlockChain transactionBlockChain) {
        return new TransactionValida(transactionBlockChain);
    }

    private TransactionBlockChain getTransactionBlockChain() {
        return transactionBlockChain;
    }

    private void setTransactionBlockChain(TransactionBlockChain transactionBlockChain) {
        this.transactionBlockChain = transactionBlockChain;
    }

    public Boolean isInputEqualOutputValue() {
        return this.getTransactionBlockChain().getUnspentValue().equals(this.getOutputsValue());
    }

    public Integer getOutputsValue() {
        return this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain().getValue() + this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().getValue();
    }

    @Override
    void valida(BlockChainValida.PreviousBlockData previousBlockData) {
        if (!this.getTransactionBlockChain().verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }

        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getTransactionBlockChain().getTransaction().getFingerPrint() + ")");
        }

        if (!this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain().isMine(this.getTransactionBlockChain().getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain() + ") is not who it should be");
        }

        if (!this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain().isMine(this.getTransactionBlockChain().getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain() + ") is not who it should be");
        }
    }

    @Override
    void processaDadosProximoBloco(BlockChainValida.PreviousBlockData previousBlockData) {
        List<TransactionOperationBlockChain> transactionsOperationBlockChain = this.getTransactionBlockChain().getUnspentTransactionsOperationBlockChain();

        for(TransactionOperationBlockChain output : transactionsOperationBlockChain) {
            TransactionOperationValida.valida(output).executa(previousBlockData);
        }

        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getCurrentTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getLeftOverTransactionOperationBlockChain());
    }
}
