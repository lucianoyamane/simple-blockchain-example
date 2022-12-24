package br.com.lucianoyamane.example;

import br.com.lucianoyamane.example.keypair.BouncyCastleKeyPair;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PublicKey;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    void testProcessTransaction() {
        KeyPair keyPairFrom = BouncyCastleKeyPair.init().getKeyPair();
        KeyPair keyPairTo = BouncyCastleKeyPair.init().getKeyPair();

//        Transaction transaction = new Transaction(keyPairFrom.getPublic(), keyPairTo.getPublic(), 100f, null);
//        transaction.generateSignature(keyPairFrom.getPrivate());
//        transaction.setTransactionId("0");
//        assertTrue(transaction.processTransaction());
//        transaction.outputs.add(new TransactionOutput(transaction.reciepient, transaction.value, transaction.getTransactionId()));
    }

}
