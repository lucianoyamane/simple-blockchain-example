package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {

    private String transactionId; // this is also the hash of the transaction.
	private PublicKeyDecorator senderPublicKey; // senders address/public key.
	private PublicKeyDecorator receiverPublicKey; // Recipients address/public key.
	private float value;
	private byte[] signature; // this is to prevent anybody else from spending funds in our wallet.
	TransactionInput input;
	public List<TransactionOutput> outputs;

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, float value) {
		this.setSenderPublicKey(senderPublicKey);
		this.setReceiverPublicKey(receiverPublicKey);
		this.setValue(value);
		this.setTransactionId(calulateHash());
		this.setOutputs(new ArrayList());
	}

	private Transaction(PublicKeyDecorator senderPublicKey, PublicKeyDecorator receiverPublicKey, float value,  TransactionInput input) {
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

	public float getValue() {
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

	private void setValue(float value) {
		this.value = value;
	}

	private void setInput(TransactionInput input) {
		this.input = input;
	}

	public static Transaction create(PublicKeyDecorator sender, PublicKeyDecorator receiver, float value, TransactionInput input) {
		return new Transaction(sender, receiver, value, input);
	}

	public static Transaction genesis(PublicKeyDecorator sender, PublicKeyDecorator receiver, float value) {
		Transaction transaction = new Transaction(sender, receiver, value);
		transaction.setTransactionId("0");
		transaction.addOutput(TransactionOutput.create(transaction));
		return transaction;
	}

	private void addOutput(TransactionOutput transactionOutput) {
		this.outputs.add(transactionOutput);
		NoobChain.UTXOs.put(transactionOutput.getId(), transactionOutput);
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
		NoobChain.UTXOs.remove(input.getTransactionOutputId());
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

	private float getLeftOverValue() {
		return this.getInputValue() - this.value;
	}


	public float getInputValue() {
		return input.getUTXO().getValue();
	}

	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs) {
			total += o.getValue();
		}
		return total;
	}
    
}