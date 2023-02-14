package br.com.lucianoyamane.example.keypair;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class BouncyCastleKeyPairTest {

    @Test
    void testInit() {
        BouncyCastleKeyPair bouncyCastleKeyPair = BouncyCastleKeyPair.init();
        assertNotNull(bouncyCastleKeyPair);
    }

    @Test
    void testGetKeyPair() {
        KeyPair keyPair = BouncyCastleKeyPair.init().getKeyPair();
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPublic());
        assertNotNull(keyPair.getPrivate());

        PublicKey publicKey = keyPair.getPublic();
        assertEquals("ECDSA", publicKey.getAlgorithm());
    }
}