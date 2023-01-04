package br.com.lucianoyamane.example;

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

	public Boolean isConsistent(TransactionOutput referenceTransactionOutput) {
		if(referenceTransactionOutput == null) {
			System.out.println("#Referenced input on Transaction(" + this.getUnspentTransaction().getId() + ") is Missing");
			return Boolean.FALSE;
		}
		if(this.getUnspentTransaction().getValue() != referenceTransactionOutput.getValue()) {
			System.out.println("#Referenced input Transaction(" + this.getUnspentTransaction().getId() + ") value is Invalid");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
