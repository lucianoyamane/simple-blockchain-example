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

	private List<TransactionInput> inputs;

	private List<TransactionOutput> outputs;

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setOutputs(new ArrayList());
		this.setInputs(new ArrayList<>());
	}

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, Integer value,  List<TransactionInput> inputs) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setOutputs(new ArrayList());
		this.setInputs(inputs);
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

	private void setInputs(List<TransactionInput> inputs) {
		this.inputs = inputs;
	}

	public List<TransactionInput> getInputs() {
		return this.inputs;
	}

	public List<TransactionOutput> getOutputs() {
		return this.outputs;
	}


	public static Transaction create(PublicKeyDecorator sender, PublicKeyDecorator receiver, Integer value, List<TransactionInput> inputs) {
		return new Transaction(sender, receiver, value, inputs);
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
    public Boolean verifiySignature() {
        Boolean verify = StringUtil.verifyECDSASig(senderPublicKey.getPublicKey(), this.getData(), signature);
		if (!verify) {
			System.out.println("#Transaction Signature failed to verify");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
    }

	private void removeCurrentOutput() {
		for(TransactionInput transactionInput : this.inputs) {
			UnspentTransactions.getInstance().remove(transactionInput.getUnspentTransaction());
		}
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
		return this.inputs.stream().mapToInt(input -> input.getUnspentTransaction().getValue()).sum();
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

	public Boolean isReceiverOutputConsistent() {
		TransactionOutput receiverOutput = this.outputs.get(0);
		if(!receiverOutput.getReceiverPublicKey().equals(this.getReceiverPublicKey())) {
			System.out.println("#Transaction(" + getTransactionId() + ") output reciepient is not who it should be");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public Boolean isSenderOutputConsistent() {
		TransactionOutput senderOutput = this.outputs.get(1);
		if( !senderOutput.getReceiverPublicKey().equals(this.getSenderPublicKey())) {
			System.out.println("#Transaction(" + getTransactionId() + ") output 'change' is not sender.");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
    
}
