package br.com.lucianoyamane.example.keypair;

import java.security.KeyPair;
import java.security.Security;

public class BouncyCastleKeyPair {

    private BouncyCastleKeyPair() {
        this.config();
    }

    private void config() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static BouncyCastleKeyPair init() {
        return new BouncyCastleKeyPair();
    }

    public KeyPair getKeyPair() {
        return KeyPairBuilder.init()
                .algorithm("ECDSA")
                .provider("BC")
                .secureRandom("SHA1PRNG")
                .ecGenParameterSpec("prime192v1").build();
    }
}
