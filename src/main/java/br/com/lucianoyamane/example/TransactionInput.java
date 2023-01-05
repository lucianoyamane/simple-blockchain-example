package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.exception.BlockChainException;

public class TransactionInput {

	private TransactionOutput unspentTransaction;
	
	private TransactionInput(TransactionOutput unspentTransaction) {
		this.unspentTransaction = unspentTransaction;
	}

	public static TransactionInput create(TransactionOutput unspentTransaction) {
		return new TransactionInput(unspentTransaction);
	}

	public TransactionOutput getUnspentTransaction() {
		return unspentTransaction;
	}

	public void isConsistent(TransactionOutput referenceTransactionOutput) {
		if(referenceTransactionOutput == null) {
			throw new BlockChainException("#Referenced input on Transaction(" + this.getUnspentTransaction().getId() + ") is Missing");
		}
		if(this.getUnspentTransaction().getValue() != referenceTransactionOutput.getValue()) {
			throw new BlockChainException("#Referenced input Transaction(" + this.getUnspentTransaction().getId() + ") value is Invalid");
		}
	}
}
