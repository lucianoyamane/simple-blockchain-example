package br.com.lucianoyamane.example;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
	private PublicKey receiverPublicKey; //also known as the new owner of these coins.
	private float value; //the amount of coins they own
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicKey receiverPublicKey, float value, String transactionId) {
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setId(StringUtil.encode(StringUtil.getStringFromKey(receiverPublicKey) + value + transactionId));
	}

	public String getId() {
		return id;
	}

	public PublicKey getReceiverPublicKey() {
		return receiverPublicKey;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setReceiverPublicKey(PublicKey receiverPublicKey) {
		this.receiverPublicKey = receiverPublicKey;
	}

	private void setValue(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	public static TransactionOutput create(Transaction transaction) {
		return new TransactionOutput(transaction.getReceiverPublicKey(), transaction.getValue(), transaction.getTransactionId());
	}

	public static TransactionOutput create(PublicKey receiverPublicKey, float value, String transactionId) {
		return new TransactionOutput(receiverPublicKey, value, transactionId);
	}

	public boolean isMine(PublicKey publicKey) {
		return (publicKey == receiverPublicKey);
	}
    
}
