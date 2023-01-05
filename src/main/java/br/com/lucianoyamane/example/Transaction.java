package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transactions.UnspentTransactions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {

    private String transactionId; // this is also the hash of the transaction.
	private PublicKeyDecorator senderPublicKey; // senders address/public key.
	private PublicKeyDecorator receiverPublicKey; // Recipients address/public key.
	private Integer value;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

	private List<TransactionInput> inputs;

	private TransactionOutput currentTransactionOutput;

	private TransactionOutput leftOverTransactionOutput;
	private String owner;

	private String receiver;

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setInputs(new ArrayList<>());
	}

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value,  List<TransactionInput> inputs, String owner, String receiver) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setInputs(inputs);
		this.setOwner(owner);
		this.setReceiver(receiver);
	}


	public TransactionOutput getCurrentTransactionOutput() {
		return currentTransactionOutput;
	}

	public void setCurrentTransactionOutput(TransactionOutput currentTransactionOutput) {
		this.currentTransactionOutput = currentTransactionOutput;
	}

	public TransactionOutput getLeftOverTransactionOutput() {
		return leftOverTransactionOutput;
	}

	public void setLeftOverTransactionOutput(TransactionOutput leftOverTransactionOutput) {
		this.leftOverTransactionOutput = leftOverTransactionOutput;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public PublicKeyDecorator getSenderPublicKey() {
		return senderPublicKey;
	}

	public PublicKeyDecorator getReceiverPublicKey() {
		return receiverPublicKey;
	}

	public Integer getValue() {
		return value;
	}

	private void setSenderPublicKey(PublicKeyDecorator senderPublicKey) {
		this.senderPublicKey = senderPublicKey;
	}

	private void setReceiverPublicKey(PublicKeyDecorator receiverPublicKey) {
		this.receiverPublicKey = receiverPublicKey;
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

	public static Transaction create(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value, List<TransactionInput> inputs, String owner, String nameReceiver) {
		return new Transaction(sender, receiver, value, inputs, owner, nameReceiver);
	}

	public static Transaction genesis(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value) {
		Transaction transaction = new Transaction(sender, receiver, value);
		transaction.setTransactionId("0");
		TransactionOutput leftover = TransactionOutput.leftover(transaction.getReceiverPublicKey(), transaction.getValue(), transaction.getTransactionId(), "genesis");
		transaction.setLeftOverTransactionOutput(leftover);
		transaction.addUnspentTransaction(leftover);
		return transaction;
	}

	private void addUnspentTransaction(TransactionOutput transactionOutput) {
		UnspentTransactions.getInstance().add(transactionOutput);
	}

	public String getTransactionId() {
		return transactionId;
	}

	private void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	private String calulateHash() {
		return StringUtil.encode(
				this.getData() + UUID.randomUUID());
	}

	public String getData() {
		return getSenderPublicKey().toString() +
				getReceiverPublicKey().toString() +
				value;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}


	private void removeCurrentOutput() {
		for(TransactionInput transactionInput : this.inputs) {
			UnspentTransactions.getInstance().remove(transactionInput.getUnspentTransaction());
		}
	}

	private void addCurrentTransactionOutput() {
		TransactionOutput current = TransactionOutput.current( this.receiverPublicKey, value, transactionId, this.getReceiver());
		this.setCurrentTransactionOutput(current);
		this.addUnspentTransaction(current);
	}

	private void addLeftOverTransactionOutput() {
		TransactionOutput leftover = TransactionOutput.leftover( this.senderPublicKey, this.getLeftOverValue(), transactionId, this.getOwner());
		this.setLeftOverTransactionOutput(leftover);
		this.addUnspentTransaction(leftover);
	}


    public boolean processTransaction() {
		this.verifiySignature();
		this.removeCurrentOutput();
		this.addCurrentTransactionOutput();
		this.addLeftOverTransactionOutput();

		return true;
	}

	private Integer getLeftOverValue() {
		return this.getInputValue() - this.value;
	}


	public Integer getInputValue() {
		return this.inputs.stream().mapToInt(input -> input.getUnspentTransaction().getValue()).sum();
	}

	public Integer getOutputsValue() {
		return this.getCurrentTransactionOutput().getValue() + this.getLeftOverTransactionOutput().getValue();
	}

	public void isConsistent() {
		if (!this.verifiySignature()) {
			throw new BlockChainException("Transaction Signature failed to verify");
		}

		if (!this.isInputEqualOutputValue()) {
			throw new BlockChainException("Inputs are note equal to outputs on Transaction(" + this.getTransactionId() + ")");
		}

		if (!this.isCurrentOutputConsistent()) {
			throw new BlockChainException("#Transaction(" + getTransactionId() + ") output reciepient is not who it should be");
		}

		if (!this.isLeftoverOutputConsistent()) {
			throw new BlockChainException("#Transaction(" + getTransactionId() + ") output 'change' is not sender.");
		}
	}

	public Boolean verifiySignature() {
		return StringUtil.verifyECDSASig(senderPublicKey.getPublicKey(), this.getData(), signature);
	}

	public Boolean isInputEqualOutputValue() {
		return this.getInputValue().equals(this.getOutputsValue());
	}

	public Boolean isCurrentOutputConsistent() {
		TransactionOutput receiverOutput = this.getCurrentTransactionOutput();
		return receiverOutput.getReceiverPublicKey().equals(this.getReceiverPublicKey());
	}

	public Boolean isLeftoverOutputConsistent() {
		TransactionOutput senderOutput = this.getLeftOverTransactionOutput();
		return senderOutput.getReceiverPublicKey().equals(this.getSenderPublicKey());
	}
    
}
