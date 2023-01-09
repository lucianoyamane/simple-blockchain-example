package br.com.lucianoyamane.example.transaction;

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

}
