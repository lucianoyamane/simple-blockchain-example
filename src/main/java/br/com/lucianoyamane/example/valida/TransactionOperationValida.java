package br.com.lucianoyamane.example.valida;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.transaction.TransactionOperationBlockChain;

import java.util.Objects;

public class TransactionOperationValida implements Valida {

    private TransactionOperationBlockChain transactionOperationBlockChain;

    public static TransactionOperationValida valida(TransactionOperationBlockChain transactionOperationBlockChain) {
        return new TransactionOperationValida(transactionOperationBlockChain);
    }

    private TransactionOperationValida(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.setTransactionOperationBlockChain(transactionOperationBlockChain);
    }

    private TransactionOperationBlockChain getTransactionOperationBlockChain() {
        return transactionOperationBlockChain;
    }

    private void setTransactionOperationBlockChain(TransactionOperationBlockChain transactionOperationBlockChain) {
        this.transactionOperationBlockChain = transactionOperationBlockChain;
    }

    private Boolean possueMesmoValor(TransactionOperationBlockChain transactionOperationBlockChain) {
        return this.getTransactionOperationBlockChain().getTransactionOperationValue().equals(transactionOperationBlockChain.getValue());
    }

    @Override
    public void isConsistent(BlockChainValida.PreviousBlockData previousBlockData) {
        TransactionOperationBlockChain referenceTransactionOperationBlockChain = previousBlockData.findReferencedTransactionOperationBlockChain(this.getTransactionOperationBlockChain());
        if(Objects.isNull(referenceTransactionOperationBlockChain)) {
            throw new BlockChainException("#Referenced input on Transaction(" + this.getTransactionOperationBlockChain().getTransactionOperationId() + ") is Missing");
        }
        if(!this.possueMesmoValor(referenceTransactionOperationBlockChain)) {
            throw new BlockChainException("#Referenced input Transaction(" + this.getTransactionOperationBlockChain().getTransactionOperationId() + ") value is Invalid");
        }
        previousBlockData.removeTransactionOperationBlockChains(this.getTransactionOperationBlockChain());


    }
}
