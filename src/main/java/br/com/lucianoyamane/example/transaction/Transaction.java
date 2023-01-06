package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.TransactionInput;
import br.com.lucianoyamane.example.TransactionOutput;
import br.com.lucianoyamane.example.exception.BlockChainException;
import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import br.com.lucianoyamane.example.transactions.UnspentTransactions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
	private SenderTransaction senderTransaction;
	private ReceiverTransaction receiverTransaction;
	private String hash; // this is also the hash of the transaction.
	private Integer value;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	private List<TransactionInput> inputs;

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value, String sender, String receiver) {
		SenderTransaction senderTransaction = SenderTransaction.create(senderPublicKey, sender);
		this.setSenderTransaction(senderTransaction);

		ReceiverTransaction receiverTransaction = ReceiverTransaction.create(receiverPublicKey, receiver);
		this.setReceiverTransaction(receiverTransaction);

		this.setValue(value);
		this.setHash(calculateHash());
		this.setInputs(new ArrayList<>());
	}

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value,  List<TransactionInput> inputs, String owner, String receiver) {
		SenderTransaction senderTransaction = SenderTransaction.create(senderPublicKey, owner);
		this.setSenderTransaction(senderTransaction);

		ReceiverTransaction receiverTransaction = ReceiverTransaction.create(receiverPublicKey, receiver);
		this.setReceiverTransaction(receiverTransaction);

		this.setValue(value);
		this.setHash(calculateHash());
		this.setInputs(inputs);
	}


	public SenderTransaction getSenderTransaction() {
		return senderTransaction;
	}

	public void setSenderTransaction(SenderTransaction senderTransaction) {
		this.senderTransaction = senderTransaction;
	}

	public ReceiverTransaction getReceiverTransaction() {
		return receiverTransaction;
	}

	private void setReceiverTransaction(ReceiverTransaction receiverTransaction) {
		this.receiverTransaction = receiverTransaction;
	}

	private void setSenderTransactionOutput(TransactionOutput senderCurrentTransactionOutput) {
		this.getSenderTransaction().setTransactionOutput(senderCurrentTransactionOutput);
	}

	public TransactionOutput getReceiverTransactionOutput() {
		return this.getReceiverTransaction().getTransactionOutput();
	}

	private void setReceiverTransactionOutput(TransactionOutput receiverTransactionOutput) {
		this.getReceiverTransaction().setTransactionOutput(receiverTransactionOutput);
	}

	public String getOwner() {
		return this.getSenderTransaction().getName();
	}

	public String getReceiver() {
		return this.getReceiverTransaction().getName();
	}

	private PublicKeyDecorator getSenderPublicKey() {
		return this.getSenderTransaction().getPublicKey();
	}

	private String getSenderPublicKeyString() {
		return this.getSenderTransaction().getPublicKeyString();
	}

	private PublicKeyDecorator getReceiverPublicKey() {
		return this.getReceiverTransaction().getPublicKey();
	}

	public TransactionOutput getSenderTransactionOutput() {
		return this.getSenderTransaction().getTransactionOutput();
	}

	private String getReceiverPublicKeyString() {
		return this.getReceiverTransaction().getPublicKeyString();
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

	public static Transaction create(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value, List<TransactionInput> inputs, String owner, String nameReceiver) {
		return new Transaction(sender, receiver, value, inputs, owner, nameReceiver);
	}

	public static Transaction genesis(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value, String receiverName) {
		String senderName = "genesis";
		Transaction transaction = new Transaction(sender, receiver, value, senderName, receiverName);
		transaction.setHash("0");
		TransactionOutput leftover = TransactionOutput.leftover(transaction.getReceiverPublicKey(), transaction.getValue(), transaction.getHash(), senderName);
		transaction.setReceiverTransactionOutput(leftover);
		transaction.addUnspentTransaction(leftover);
		return transaction;
	}

	private void addUnspentTransaction(TransactionOutput transactionOutput) {
		UnspentTransactions.getInstance().add(transactionOutput);
	}

	public String getHash() {
		return hash;
	}

	private void setHash(String hash) {
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

	private void removeCurrentOutput() {
		for(TransactionInput transactionInput : this.inputs) {
			UnspentTransactions.getInstance().remove(transactionInput.getUnspentTransaction());
		}
	}

	private void addCurrentTransactionOutput() {
		TransactionOutput current = TransactionOutput.current( this.getReceiverPublicKey(), this.getValue(), this.getHash(), this.getReceiver());
		this.setSenderTransactionOutput(current);
		this.addUnspentTransaction(current);
	}

	private void addLeftOverTransactionOutput() {
		TransactionOutput leftover = TransactionOutput.leftover( this.getSenderPublicKey(), this.getLeftOverValue(), this.getHash(), this.getOwner());
		this.setReceiverTransactionOutput(leftover);
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
		return StringUtil.verifyECDSASig(this.getSenderPublicKey().getPublicKey(), this.getHash(), signature);
	}

	public Boolean isInputEqualOutputValue() {
		return this.getInputValue().equals(this.getOutputsValue());
	}
    
}
