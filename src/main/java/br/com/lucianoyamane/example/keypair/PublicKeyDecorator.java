package br.com.lucianoyamane.example.keypair;

import br.com.lucianoyamane.example.StringUtil;
import br.com.lucianoyamane.example.blockchain.OperationExecutor;

import java.security.PublicKey;

public class PublicKeyDecorator {

    private PublicKey publicKey;
    private String stringValue;

    private PublicKeyDecorator(PublicKey publicKey) {
        this.setPublicKey(publicKey);
        this.setStringValue(publicKey);
    }

    public static PublicKeyDecorator inicia(PublicKey publicKey) {
        return new PublicKeyDecorator(publicKey);
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    private void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    private void setStringValue(PublicKey publicKey) {
        this.stringValue = StringUtil.getStringFromKey(publicKey);
    }

    public Boolean mePertence(OperationExecutor operationExecutor) {
        return this.equals(operationExecutor.getTransactionOperation().getPublicKeyDecorator());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PublicKeyDecorator)) {
            return false;
        }
        PublicKeyDecorator other = (PublicKeyDecorator) obj;
        return this.toString().equals(other.toString());
    }
}
