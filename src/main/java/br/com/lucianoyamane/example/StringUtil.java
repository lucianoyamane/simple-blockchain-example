package br.com.lucianoyamane.example;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.bouncycastle.util.encoders.Hex;

public class StringUtil {

    public static String encode(String input) {
        byte[] hash = getSHA256().digest(input.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }

    private static MessageDigest getSHA256() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } 
    }
}
