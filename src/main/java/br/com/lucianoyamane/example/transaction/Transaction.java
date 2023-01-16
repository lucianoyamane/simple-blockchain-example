package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.wallet.PublicData;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
	private PublicKeyDecorator senderPublicKeyDecorator;
	private PublicKeyDecorator receiverPublickeyDecorator;
	private TransactionOutput senderTransactionOutput;
	private TransactionOutput receiverTransactionOutput;
	private Integer value;
	private List<TransactionInput> inputs;
	private String fingerPrint;
	private byte[] signature;

	protected Transaction(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value, List<TransactionInput> inputs) {
		this.setSenderPublicKeyDecorator(senderPublicKeyDecorator);
		this.setReceiverPublickeyDecorator(receiverPublickeyDecorator);
		this.setValue(value);
		this.setInputs(inputs);
	}

	public static Transaction create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value, List<TransactionInput> inputs) {
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

	public void setSenderTransactionOutput(TransactionOutput senderTransactionOutput) {
		this.senderTransactionOutput = senderTransactionOutput;
	}

	public TransactionOutput getReceiverTransactionOutput() {
		return this.receiverTransactionOutput;
	}

	public void setReceiverTransactionOutput(TransactionOutput receiverTransactionOutput) {
		this.receiverTransactionOutput = receiverTransactionOutput;
	}

	public TransactionOutput getSenderTransactionOutput() {
		return senderTransactionOutput;
	}

	public Integer getValue() {
		return value;
	}

	private void setValue(Integer value) {
		this.value = value;
	}

	private void setInputs(List<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public List<TransactionInput> getInputs() {
		return this.inputs;
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
