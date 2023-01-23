package br.com.lucianoyamane.example.transaction;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

import java.util.List;

public class Transaction {
	private PublicKeyDecorator senderPublicKeyDecorator;
	private PublicKeyDecorator receiverPublickeyDecorator;
	private Integer value;
	private String fingerPrint;
	private byte[] signature;

	protected Transaction(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
		this.setSenderPublicKeyDecorator(senderPublicKeyDecorator);
		this.setReceiverPublickeyDecorator(receiverPublickeyDecorator);
		this.setValue(value);
	}

	public static Transaction create(PublicKeyDecorator senderPublicKeyDecorator, PublicKeyDecorator receiverPublickeyDecorator, Integer value) {
		return new Transaction(senderPublicKeyDecorator, receiverPublickeyDecorator, value);
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

	public Integer getValue() {
		return value;
	}

	private void setValue(Integer value) {
		this.value = value;
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
