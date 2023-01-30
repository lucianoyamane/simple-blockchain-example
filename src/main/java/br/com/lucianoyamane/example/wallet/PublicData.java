package br.com.lucianoyamane.example.wallet;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;

public class PublicData {

    private PublicKeyDecorator publicKey;
    private String name;

    public static PublicData create(PublicKeyDecorator publicKey, String name) {
        return new PublicData(publicKey, name);
    }

    private PublicData(PublicKeyDecorator publicKey, String name) {
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
}
