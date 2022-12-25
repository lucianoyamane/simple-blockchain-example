package br.com.lucianoyamane.example;

public class TransactionInput {
    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
	public TransactionOutput UTXO; //Contains the Unspent transaction output
	
	private TransactionInput(String transactionOutputId, TransactionOutput UTXO) {
		this.transactionOutputId = transactionOutputId;
		this.UTXO = UTXO;
	}

	public static TransactionInput create(String transactionOutputId, TransactionOutput UTXO) {
		return new TransactionInput(transactionOutputId, UTXO);
	}
    
}
