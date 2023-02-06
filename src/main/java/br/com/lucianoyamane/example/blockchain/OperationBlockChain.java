package br.com.lucianoyamane.example.blockchain;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.models.Operation;

public class OperationBlockChain implements BlockChainObject {

	private Operation operation;

	private OperationBlockChain(PublicKeyDecorator publicKeyDecorator, Integer value) {
		this.setTransactionOperation(Operation.init(publicKeyDecorator, value));
	}

	public static OperationBlockChain create(PublicKeyDecorator publicKeyDecorator, Integer value) {
		return new OperationBlockChain(publicKeyDecorator, value);
	}

	public Integer getValue() {
		return this.getTransactionOperation().getValue();
	}

	public Operation getTransactionOperation() {
		return operation;
	}

	public Integer getTransactionOperationValue() {
		return this.getTransactionOperation().getValue();
	}

	public String getTransactionOperationId() {
		return this.getTransactionOperation().getId();
	}

	private void setTransactionOperation(Operation operation) {
		this.operation = operation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof OperationBlockChain)) {
			return false;
		}
		OperationBlockChain other = (OperationBlockChain) obj;
		return this.getTransactionOperation().equals(other.getTransactionOperation());
	}

}
