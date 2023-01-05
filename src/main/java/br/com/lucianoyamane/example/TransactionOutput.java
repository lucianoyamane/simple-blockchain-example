package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
	private PublicKeyDecorator receiverPublicKey;
	private Integer value;

	private String owner;

	private String type;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicKeyDecorator receiverPublicKey, Integer value, String transactionId, String owner, String type) {
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setId(StringUtil.encode(this.getReceiverPublicKey().toString() + value + transactionId));
		this.setOwner(owner);
		this.setType(type);
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public static TransactionOutput create(Transaction transaction, String owner, String type) {
		return new TransactionOutput(transaction.getReceiverPublicKey(), transaction.getValue(), transaction.getTransactionId(), owner, type);
	}

	public static TransactionOutput create(PublicKeyDecorator receiverPublicKey, Integer value, String transactionId, String owner, String type) {
		return new TransactionOutput(receiverPublicKey, value, transactionId, owner, type);
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
