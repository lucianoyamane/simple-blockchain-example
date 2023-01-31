package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class TransactionOperationBlockChain implements BlockChainObject {

	private TransactionOperation transactionOperation;

	private TransactionOperationBlockChain(PublicKeyDecorator publicKeyDecorator, Integer value) {
		this.setTransactionOperation(TransactionOperation.init(publicKeyDecorator, value));
	}

	public static TransactionOperationBlockChain create(PublicKeyDecorator publicKeyDecorator, Integer value) {
		return new TransactionOperationBlockChain(publicKeyDecorator, value);
	}

	public Boolean isMine(PublicKeyDecorator publicKey) {
		return publicKey.equals(this.getTransactionOperation().getPublicKeyDecorator());
	}

	public Integer getValue() {
		return this.getTransactionOperation().getValue();
	}

	public TransactionOperation getTransactionOperation() {
		return transactionOperation;
	}

	public Integer getTransactionOperationValue() {
		return this.getTransactionOperation().getValue();
	}

	public String getTransactionOperationId() {
		return this.getTransactionOperation().getId();
	}

	private void setTransactionOperation(TransactionOperation transactionOperation) {
		this.transactionOperation = transactionOperation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof TransactionOperationBlockChain)) {
			return false;
		}
		TransactionOperationBlockChain other = (TransactionOperationBlockChain) obj;
		return this.getTransactionOperation().equals(other.getTransactionOperation());
	}

}
