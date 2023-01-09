package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.wallet.PublicData;

public class TransactionOutput {
    private String id;
	private PublicData publicData;
	private Integer value;
	private String type;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(PublicData publicData, Integer value, String transactionId, String type) {
		this.setOperator(publicData);
		this.setValue(value);
		this.setId(StringUtil.encode(publicData.getPublicKeyDecorator().toString() + value + transactionId));
		this.setType(type);
	}

	public PublicData getOperator() {
		return publicData;
	}

	public void setOperator(PublicData publicData) {
		this.publicData = publicData;
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

	public static TransactionOutput leftover(PublicData publicData, Integer value, String transactionId) {
		return new TransactionOutput(publicData, value, transactionId, "LEFTOVER");
	}

	public static TransactionOutput current(PublicData publicData, Integer value, String transactionId) {
		return new TransactionOutput(publicData, value, transactionId, "CURRENT");
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(this.getOperator().getPublicKeyDecorator());
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
