package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import com.google.gson.Gson;

import java.util.UUID;

public class TransactionOutput {
    private String id;
	private PublicKeyDecorator publicKeyDecorator;
	private Integer value;
	private String type;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicKeyDecorator publicKeyDecorator, Integer value, String type) {
		this.setPublicKeyDecorator(publicKeyDecorator);
		this.setValue(value);
		this.setType(type);
		this.setId(UUID.randomUUID().toString());
	}

	public PublicKeyDecorator getPublicKeyDecorator() {
		return publicKeyDecorator;
	}

	public void setPublicKeyDecorator(PublicKeyDecorator publicKeyDecorator) {
		this.publicKeyDecorator = publicKeyDecorator;
	}

	public void setType(String type) {
		this.type = type;
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

	public static TransactionOutput leftover(PublicKeyDecorator publicKeyDecorator, Integer value) {
		return new TransactionOutput(publicKeyDecorator, value, "LEFTOVER");
	}

	public static TransactionOutput current(PublicKeyDecorator publicKeyDecorator, Integer value) {
		return new TransactionOutput(publicKeyDecorator, value, "CURRENT");
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(this.getPublicKeyDecorator());
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

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
