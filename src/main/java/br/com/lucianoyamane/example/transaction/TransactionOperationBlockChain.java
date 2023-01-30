package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.BlockChainApp;
import br.com.lucianoyamane.example.BlockChainObject;
import br.com.lucianoyamane.example.exception.BlockChainException;
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

	private TransactionOperation getTransactionOperation() {
		return transactionOperation;
	}

	private void setTransactionOperation(TransactionOperation transactionOperation) {
		this.transactionOperation = transactionOperation;
	}

	@Override
	public void isConsistent(BlockChainApp.PreviousBlockData previousBlockData) {

		TransactionOperationBlockChain referenceTransactionOperationBlockChain = previousBlockData.getTransactionOperationBlockChains().stream().filter(outputTemp -> outputTemp.equals(this)).findFirst().orElse(null);
		if(referenceTransactionOperationBlockChain == null) {
			throw new BlockChainException("#Referenced input on Transaction(" + this.getTransactionOperation().getId() + ") is Missing");
		}
		if(this.getTransactionOperation().getValue() != referenceTransactionOperationBlockChain.getValue()) {
			throw new BlockChainException("#Referenced input Transaction(" + this.getTransactionOperation().getId() + ") value is Invalid");
		}
		previousBlockData.removeTransactionOperationBlockChains(this);
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
