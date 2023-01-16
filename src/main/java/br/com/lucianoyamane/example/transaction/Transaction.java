package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.List;

public class Transaction {
	private PublicKeyDecorator senderPublicKeyDecorator;
	private PublicKeyDecorator receiverPublickeyDecorator;
	private TransactionOperation senderTransactionOperation;
	private TransactionOperation receiverTransactionOperation;
	private Integer value;
	private List<TransactionOperation> unspentTransactions;
	private String fingerPrint;
	private byte[] signature;

	protected Transaction(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value, List<TransactionOperation> unspentTransactions) {
		this.setSenderPublicKeyDecorator(senderPublicKeyDecorator);
		this.setReceiverPublickeyDecorator(receiverPublickeyDecorator);
		this.setValue(value);
		this.setUnspentTransactions(unspentTransactions);
	}

	public static Transaction create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value, List<TransactionOperation> inputs) {
		return new Transaction(senderPublicKeyDecorator, receiverPublickeyDecorator, value, inputs);
	}

	public PublicKeyDecorator getSenderPublicKeyDecorator() {
		return senderPublicKeyDecorator;
	}

	public void setSenderPublicKeyDecorator(PublicKeyDecorator senderPublicKeyDecorator) {
		this.senderPublicKeyDecorator = senderPublicKeyDecorator;
	}

	public PublicKeyDecorator getReceiverPublickeyDecorator() {
		return receiverPublickeyDecorator;
	}

	public void setReceiverPublickeyDecorator(PublicKeyDecorator receiverPublickeyDecorator) {
		this.receiverPublickeyDecorator = receiverPublickeyDecorator;
	}

	public void setSenderTransactionOutput(TransactionOperation senderTransactionOperation) {
		this.senderTransactionOperation = senderTransactionOperation;
	}

	public TransactionOperation getReceiverTransactionOutput() {
		return this.receiverTransactionOperation;
	}

	public void setReceiverTransactionOutput(TransactionOperation receiverTransactionOperation) {
		this.receiverTransactionOperation = receiverTransactionOperation;
	}

	public TransactionOperation getSenderTransactionOutput() {
		return senderTransactionOperation;
	}

	public Integer getValue() {
		return value;
	}

	private void setValue(Integer value) {
		this.value = value;
	}

	private void setUnspentTransactions(List<TransactionOperation> unspentTransactions) {
		this.unspentTransactions = unspentTransactions;
	}

	public List<TransactionOperation> getUnspentTransactions() {
		return this.unspentTransactions;
	}

	public String getFingerPrint() {
		return fingerPrint;
	}

	public void setFingerPrint(String fingerPrint) {
		this.fingerPrint = fingerPrint;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
}
