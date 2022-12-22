package br.com.lucianoyamane.example.keypair;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class KeyPairBuilder {

    private String algorithm;
    private String provider;
    private String secureRandom;
    private String ecGenParameterSpec;

    private KeyPairBuilder() {

    }

    public static KeyPairBuilder init() {
        return new KeyPairBuilder();
    }


    public KeyPairBuilder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public KeyPairBuilder provider(String provider) {
        this.provider = provider;
        return this;
    }

    public KeyPairBuilder secureRandom(String secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }

    public KeyPairBuilder ecGenParameterSpec(String ecGenParameterSpec) {
        this.ecGenParameterSpec = ecGenParameterSpec;
        return this;
    }

    private String getAlgorithm() {
        return algorithm;
    }

    private String getProvider() {
        return provider;
    }

    private String getSecureRandom() {
        return secureRandom;
    }

    private String getEcGenParameterSpec() {
        return ecGenParameterSpec;
    }

    public KeyPair build() {
        KeyPairGenerator keyGen = this.getKeyPairGenerator(this.getAlgorithm(),this.getProvider());
        SecureRandom random = this.getSecureRandom(this.getSecureRandom());
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(this.getEcGenParameterSpec());
        try {
            keyGen.initialize(ecSpec, random);
            return keyGen.generateKeyPair();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private KeyPairGenerator getKeyPairGenerator(String algorithm, String provider) {
        try {
            return KeyPairGenerator.getInstance(algorithm, provider);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    private SecureRandom getSecureRandom(String algorithm) {
        try {
            return SecureRandom.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
