package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transaction.Operator;

public class TransactionOutput {
    private String id;
	private Operator operator;
	private Integer value;
	private String type;
	
	//TODO: implementar validacao valor minimo transação
	//		if(transactionOutput.getValue() < minimumTransaction) {
//			System.out.println("#Transaction Inputs to small: " + getInputValue());
//			return false;
//		}
	private TransactionOutput(Operator operator, Integer value, String transactionId, String type) {
		this.setOperator(operator);
		this.setValue(value);
		this.setId(StringUtil.encode(operator.getPublicKeyDecorator().toString() + value + transactionId));
		this.setType(type);
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
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

	public static TransactionOutput leftover(Operator operator, Integer value, String transactionId) {
		return new TransactionOutput(operator, value, transactionId, "LEFTOVER");
	}

	public static TransactionOutput current(Operator operator, Integer value, String transactionId) {
		return new TransactionOutput(operator, value, transactionId, "CURRENT");
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(this.getOperator().getPublicKeyDecorator());
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
