package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class Operator {

    private PublicKeyDecorator publicKey;
    private String name;

    public static Operator create(PublicKeyDecorator publicKey, String name) {
        return new Operator(publicKey, name);
    }

    private Operator(PublicKeyDecorator publicKey, String name) {
        this.setPublicKey(publicKey);
        this.setName(name);
    }

    public PublicKeyDecorator getPublicKeyDecorator() {
        return publicKey;
    }

    private void setPublicKey(PublicKeyDecorator publicKey) {
        this.publicKey = publicKey;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getPublicKeyString() {
        return publicKey.toString();
    }
}
