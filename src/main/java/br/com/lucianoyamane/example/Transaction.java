package br.com.lucianoyamane.example;

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
	TransactionInput input;
	public List<TransactionOutput> outputs;

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setOutputs(new ArrayList());
	}

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value,  TransactionInput input) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setOutputs(new ArrayList());
		this.setInput(input);
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

	public void setOutputs(List<TransactionOutput> outputs) {
		this.outputs = outputs;
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

	private void setInput(TransactionInput input) {
		this.input = input;
	}

	public static Transaction create(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value, TransactionInput input) {
		return new Transaction(sender, receiver, value, input);
	}

	public static Transaction genesis(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value) {
		Transaction transaction = new Transaction(sender, receiver, value);
		transaction.setTransactionId("0");
		transaction.addOutput(TransactionOutput.create(transaction));
		return transaction;
	}

	private void addOutput(TransactionOutput transactionOutput) {
		this.outputs.add(transactionOutput);
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

    //Verifies the data we signed hasnt been tampered with
    public void verifiySignature() {
        Boolean verify = StringUtil.verifyECDSASig(senderPublicKey.getPublicKey(), this.getData(), signature);
		if (!verify) {
			System.out.println("#Transaction Signature failed to verify");
		}
    }

	private void removeCurrentOutput() {
		UnspentTransactions.getInstance().remove(input.getUnspentTransaction());
	}

	private void addCurrentTransactionOutput() {
		this.addOutput(TransactionOutput.create( this.receiverPublicKey, value, transactionId));
	}

	private void addLeftOverTransactionOutput() {
		this.addOutput(TransactionOutput.create( this.senderPublicKey, this.getLeftOverValue(), transactionId));
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
		return input.getUnspentTransaction().getValue();
	}

	public Integer getOutputsValue() {
		return this.outputs.stream().mapToInt(output -> output.getValue()).sum();
	}

	public Boolean isConsistent() {
		if (!this.getInputValue().equals(this.getOutputsValue())) {
			System.out.println("#Inputs are note equal to outputs on Transaction(" + this.getTransactionId() + ")");
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}
    
}
