package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.TransactionInput;
import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transactions.UnspentTransactions;
import br.com.lucianoyamane.example.wallet.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
	private Operator senderOperator;
	private Operator receiverOperator;

	private TransactionOutput senderTransactionOutput;

	private TransactionOutput receiverTransactionOutput;

	private String hash; // this is also the hash of the transaction.
	private Integer value;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	private List<TransactionInput> inputs;

	protected Transaction(Operator senderOperator, Operator receiverOperator, Integer value) {
		this.setSenderOperator(senderOperator);
		this.setReceiverOperator(receiverOperator);
		this.setValue(value);
		this.setHash(calculateHash());
		this.setInputs(new ArrayList<>());
	}

	protected Transaction(Operator senderOperator, Operator receiverOperator, Integer value, List<TransactionInput> inputs) {
		this.setSenderOperator(senderOperator);
		this.setReceiverOperator(receiverOperator);
		this.setValue(value);
		this.setHash(calculateHash());
		this.setInputs(inputs);
	}


	public Operator getSenderOperator() {
		return senderOperator;
	}

	public void setSenderOperator(Operator senderOperator) {
		this.senderOperator = senderOperator;
	}

	public Operator getReceiverOperator() {
		return receiverOperator;
	}

	private void setReceiverOperator(Operator receiverOperator) {
		this.receiverOperator = receiverOperator;
	}

	private void setSenderTransactionOutput(TransactionOutput senderTransactionOutput) {
		this.senderTransactionOutput = senderTransactionOutput;
	}

	public TransactionOutput getReceiverTransactionOutput() {
		return this.receiverTransactionOutput;
	}

	private void setReceiverTransactionOutput(TransactionOutput receiverTransactionOutput) {
		this.receiverTransactionOutput = receiverTransactionOutput;
	}

	private PublicKeyDecorator getSenderPublicKey() {
		return this.getSenderOperator().getPublicKeyDecorator();
	}

	private String getSenderPublicKeyString() {
		return this.getSenderOperator().getPublicKeyString();
	}

	private PublicKeyDecorator getReceiverPublicKey() {
		return this.getReceiverOperator().getPublicKeyDecorator();
	}

	public TransactionOutput getSenderTransactionOutput() {
		return this.senderTransactionOutput;
	}

	private String getReceiverPublicKeyString() {
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

	public static Transaction create(Operator senderOperator, Operator receiverOperator, Integer value, List<TransactionInput> inputs) {
		return new Transaction(senderOperator, receiverOperator, value, inputs);
	}

	private void addUnspentTransaction(TransactionOutput transactionOutput) {
		UnspentTransactions.getInstance().add(transactionOutput);
	}

	public String getHash() {
		return hash;
	}

	protected void setHash(String hash) {
		this.hash = hash;
	}

	private String calculateHash() {
		return StringUtil.encode(this.getData());
	}

	private String getData() {
		return new StringBuilder(UUID.randomUUID().toString())
				.append(this.getSenderPublicKeyString())
				.append(this.getReceiverPublicKeyString())
				.append(this.getValue())
				.toString();
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	private byte[] getSignature() {
		return signature;
	}

	private void removeCurrentOutput() {
		for(TransactionInput transactionInput : this.inputs) {
			UnspentTransactions.getInstance().remove(transactionInput.getUnspentTransaction());
		}
	}

	private void addCurrentTransactionOutput() {
		TransactionOutput current = TransactionOutput.current( this.getReceiverOperator(), this.getValue(), this.getHash());
		this.setSenderTransactionOutput(current);
		this.addUnspentTransaction(current);
	}

	private void addLeftOverTransactionOutput() {
		TransactionOutput leftover = TransactionOutput.leftover( this.getSenderOperator(), this.getLeftOverValue(), this.getHash());
		this.setReceiverTransactionOutput(leftover);
		this.addUnspentTransaction(leftover);
	}


    public boolean processTransaction() {
		Boolean signatureVerified = this.verifiySignature();
		if (signatureVerified) {
			this.removeCurrentOutput();
			this.addCurrentTransactionOutput();
			this.addLeftOverTransactionOutput();
		}
		return signatureVerified;
	}

	private Integer getLeftOverValue() {
		return this.getInputValue() - this.getValue();
	}

	public Integer getInputValue() {
		return this.inputs.stream().mapToInt(input -> input.getUnspentTransaction().getValue()).sum();
	}

	public Integer getOutputsValue() {
		return this.getSenderTransactionOutput().getValue() + this.getReceiverTransactionOutput().getValue();
	}

	public void isConsistent() {
		if (!this.verifiySignature()) {
			throw new BlockChainException("Transaction Signature failed to verify");
		}

		if (!this.isInputEqualOutputValue()) {
			throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getHash() + ")");
		}

		this.getSenderTransactionOutput().isConsistent(this.getReceiverPublicKey());
		this.getReceiverTransactionOutput().isConsistent(this.getSenderPublicKey());
	}

	public Boolean verifiySignature() {
		return StringUtil.verifyECDSASig(this.getSenderPublicKey().getPublicKey(), this.getHash(), this.getSignature());
	}

	public Boolean isInputEqualOutputValue() {
		return this.getInputValue().equals(this.getOutputsValue());
	}
    
}
