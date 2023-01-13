package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.configurations.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.PublicData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
	private PublicData senderPublicData;
	private PublicData receiverPublicData;
	private TransactionOutput senderTransactionOutput;
	private TransactionOutput receiverTransactionOutput;
	private Integer value;// this is to prevent anybody else from spending funds in our wallet.
	private List<TransactionInput> inputs;

	protected Transaction(PublicData senderPublicData, PublicData receiverPublicData, Integer value) {
		this.setSenderOperator(senderPublicData);
		this.setReceiverOperator(receiverPublicData);
		this.setValue(value);
		this.setInputs(new ArrayList<>());
	}

	protected Transaction(PublicData senderPublicData, PublicData receiverPublicData, Integer value, List<TransactionInput> inputs) {
		this.setSenderOperator(senderPublicData);
		this.setReceiverOperator(receiverPublicData);
		this.setValue(value);
		this.setInputs(inputs);
	}

	public static Transaction genesis(PublicData senderPublicData, PublicData receiverPublicData, Integer value) {
		return new Transaction(senderPublicData, receiverPublicData, value);
	}

	public static Transaction create(PublicData senderPublicData, PublicData receiverPublicData, Integer value, List<TransactionInput> inputs) {
		return new Transaction(senderPublicData, receiverPublicData, value, inputs);
	}

	public PublicData getSenderOperator() {
		return senderPublicData;
	}

	private void setSenderOperator(PublicData senderPublicData) {
		this.senderPublicData = senderPublicData;
	}

	public PublicData getReceiverOperator() {
		return receiverPublicData;
	}

	private void setReceiverOperator(PublicData receiverPublicData) {
		this.receiverPublicData = receiverPublicData;
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

	public PublicKeyDecorator getSenderPublicKey() {
		return this.getSenderOperator().getPublicKeyDecorator();
	}

	public String getSenderPublicKeyString() {
		return this.getSenderOperator().getPublicKeyString();
	}

	public PublicKeyDecorator getReceiverPublicKey() {
		return this.getReceiverOperator().getPublicKeyDecorator();
	}

	public TransactionOutput getSenderTransactionOutput() {
		return this.senderTransactionOutput;
	}

	public String getReceiverPublicKeyString() {
		return this.getReceiverOperator().getPublicKeyString();
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









    
}
