package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class TransactionOutput {
    private String id;
	private PublicKeyDecorator publicKeyDecorator;
	private Integer value;

	private String owner;

	private String type;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicKeyDecorator publicKeyDecorator, Integer value, String transactionId, String owner, String type) {
		this.setPublicKeyDecorator(publicKeyDecorator);
		this.setValue(value);
		this.setId(StringUtil.encode(this.getPublicKeyDecorator().toString() + value + transactionId));
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

	public PublicKeyDecorator getPublicKeyDecorator() {
		return publicKeyDecorator;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setPublicKeyDecorator(PublicKeyDecorator publicKeyDecorator) {
		this.publicKeyDecorator = publicKeyDecorator;
	}

	private void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static TransactionOutput leftover(PublicKeyDecorator publicKeyDecorator, Integer value, String transactionId, String owner) {
		return new TransactionOutput(publicKeyDecorator, value, transactionId, owner, "LEFTOVER");
	}

	public static TransactionOutput current(PublicKeyDecorator publicKeyDecorator, Integer value, String transactionId, String owner) {
		return new TransactionOutput(publicKeyDecorator, value, transactionId, owner, "CURRENT");
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(publicKeyDecorator);
	}

	public void isConsistent(PublicKeyDecorator publicKeyDecorator) {
		if (!this.isMine(publicKeyDecorator)) {
			throw new BlockChainException("#TransactionOutput(" + getId() + ") is not who it should be");
		}
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
