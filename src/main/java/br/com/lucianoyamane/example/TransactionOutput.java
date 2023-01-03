package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
	private PublicKeyDecorator receiverPublicKey;
	private Integer value;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicKeyDecorator receiverPublicKey, Integer value, String transactionId) {
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setId(StringUtil.encode(this.getReceiverPublicKey().toString() + value + transactionId));
	}

	public String getId() {
		return id;
	}

	public PublicKeyDecorator getReceiverPublicKey() {
		return receiverPublicKey;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setReceiverPublicKey(PublicKeyDecorator receiverPublicKey) {
		this.receiverPublicKey = receiverPublicKey;
	}

	private void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static TransactionOutput create(Transaction transaction) {
		return new TransactionOutput(transaction.getReceiverPublicKey(), transaction.getValue(), transaction.getTransactionId());
	}

	public static TransactionOutput create(PublicKeyDecorator receiverPublicKey, Integer value, String transactionId) {
		return new TransactionOutput(receiverPublicKey, value, transactionId);
	}

	public boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(receiverPublicKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TransactionOutput)) {
			return false;
		}
		TransactionOutput other = (TransactionOutput) obj;
		return this.getId().equals(other.getId());
	}
    
}
