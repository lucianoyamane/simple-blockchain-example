package br.com.lucianoyamane.example;

public class TransactionInput {

	private TransactionOutput unspentTransaction;
	
	private TransactionInput(TransactionOutput unspentTransaction) {
		this.unspentTransaction = unspentTransaction;
	}

	public static TransactionInput create(TransactionOutput unspentTransaction) {
		return new TransactionInput(unspentTransaction);
	}

	public String getUnspentTransactionId() {
		return this.getUnspentTransaction().getId();
	}

	public TransactionOutput getUnspentTransaction() {
		return unspentTransaction;
	}
}
