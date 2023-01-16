package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import com.google.gson.Gson;

import java.util.UUID;

public class TransactionOperation {
    private String id;
	private PublicKeyDecorator publicKeyDecorator;
	private Integer value;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOperation(PublicKeyDecorator publicKeyDecorator, Integer value) {
		this.setPublicKeyDecorator(publicKeyDecorator);
		this.setValue(value);
		this.setId(UUID.randomUUID().toString());
	}

	public PublicKeyDecorator getPublicKeyDecorator() {
		return publicKeyDecorator;
	}

	public void setPublicKeyDecorator(PublicKeyDecorator publicKeyDecorator) {
		this.publicKeyDecorator = publicKeyDecorator;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	private void setValue(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static TransactionOperation create(PublicKeyDecorator publicKeyDecorator, Integer value) {
		return new TransactionOperation(publicKeyDecorator, value);
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(this.getPublicKeyDecorator());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TransactionOperation)) {
			return false;
		}
		TransactionOperation other = (TransactionOperation) obj;
		return this.getId().equals(other.getId());
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
