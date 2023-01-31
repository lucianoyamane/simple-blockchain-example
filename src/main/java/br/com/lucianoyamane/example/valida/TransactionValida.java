package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionBlockChain;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.List;

public class TransactionValida implements Valida {

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
        return this.getTransactionBlockChain().getSenderTransactionOperationBlockChain().getValue() + this.getTransactionBlockChain().getReceiverTransactionOperationBlockChain().getValue();
    }

    @Override
    public void isConsistent(BlockChainValida.PreviousBlockData previousBlockData) {
        if (!this.getTransactionBlockChain().verifiySignature()) {
            throw new BlockChainException("Transaction Signature failed to verify");
        }

        if (!this.isInputEqualOutputValue()) {
            throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getTransactionBlockChain().getTransaction().getFingerPrint() + ")");
        }

        TransactionOperationBlockChain senderTransactionOperationBlockChain = this.getTransactionBlockChain().getSenderTransactionOperationBlockChain();
        if (!senderTransactionOperationBlockChain.isMine(this.getTransactionBlockChain().getTransaction().getReceiverPublickeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + senderTransactionOperationBlockChain + ") is not who it should be");
        }

        TransactionOperationBlockChain receiverTransactionOperationBlockChain = this.getTransactionBlockChain().getReceiverTransactionOperationBlockChain();
        if (!receiverTransactionOperationBlockChain.isMine(this.getTransactionBlockChain().getTransaction().getSenderPublicKeyDecorator())) {
            throw new BlockChainException("#TransactionOutput(" + receiverTransactionOperationBlockChain + ") is not who it should be");
        }

        List<TransactionOperationBlockChain> transactionsOperationBlockChain = this.getTransactionBlockChain().getUnspentTransactionsOperationBlockChain();

        for(TransactionOperationBlockChain output : transactionsOperationBlockChain) {
            TransactionOperationValida.valida(output).isConsistent(previousBlockData);
        }

        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getSenderTransactionOperationBlockChain());
        previousBlockData.addTransactionOperationBlockChains(this.getTransactionBlockChain().getReceiverTransactionOperationBlockChain());
    }
}
