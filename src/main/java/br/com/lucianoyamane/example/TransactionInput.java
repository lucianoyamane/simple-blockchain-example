package br.com.lucianoyamane.example;

public class TransactionInput {

	private TransactionOutput UTXO; //Contains the Unspent transaction output
	
	private TransactionInput(TransactionOutput UTXO) {
		this.UTXO = UTXO;
	}

	public static TransactionInput create(TransactionOutput UTXO) {
		return new TransactionInput(UTXO);
	}

	public String getTransactionOutputId() {
		return this.getUTXO().getId();
	}

	public TransactionOutput getUTXO() {
		return UTXO;
	}
}
