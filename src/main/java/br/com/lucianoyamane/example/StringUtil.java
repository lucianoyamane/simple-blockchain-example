package br.com.lucianoyamane.example;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
		try {
			Signature dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			return dsa.sign();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//Verifies a String signature 
	public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature);
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

    public static String getCharsZeroByDifficuty(int difficulty) {
		return new String(new char[difficulty]).replace('\0', '0');
	}

	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

    public static String getMerkleRoot(List<String> values) {
		if (values.isEmpty()) {
			return null;
		}

		List<String> treeLayer = values;
		while(treeLayer.size() > 1) {
			List<String> currentLayer = new ArrayList();
			for(int i = 1; i < treeLayer.size(); i++) {
				currentLayer.add(encode(treeLayer.get(i - 1) + treeLayer.get(i)));
			}
			treeLayer = currentLayer;
		}
		return treeLayer.get(0);
	}
}
