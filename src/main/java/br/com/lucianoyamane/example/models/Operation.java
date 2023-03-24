package br.com.lucianoyamane.example.models;

import br.com.lucianoyamane.example.keypair.PublicKeyDecorator;
import com.google.gson.Gson;

import java.util.UUID;

public class Operation {

    private String id;
    private PublicKeyDecorator publicKeyDecorator;
    private Integer value;

    private Operation(PublicKeyDecorator publicKeyDecorator, Integer value) {
        this.setPublicKeyDecorator(publicKeyDecorator);
        this.setValue(value);
        this.setId(UUID.randomUUID().toString());
    }

    public static Operation init(PublicKeyDecorator publicKeyDecorator, Integer value) {
        return new Operation(publicKeyDecorator, value);
    }

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public PublicKeyDecorator getPublicKeyDecorator() {
        return publicKeyDecorator;
    }

    private void setPublicKeyDecorator(PublicKeyDecorator publicKeyDecorator) {
        this.publicKeyDecorator = publicKeyDecorator;
    }

    public Integer getValue() {
        return value;
    }

    private void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Operation)) {
            return false;
        }
        Operation other = (Operation) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
