package utility;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

    private static final String ALGORITHM = "RC2";

    public static String encrypt(String plainText, String key) throws Exception {
        key = pad(key);
        SecretKeySpec keySpec = new
        SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedBytes =
        cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String cipherText, String key) throws Exception {
        key = pad(key);
        SecretKeySpec keySpec = new
        SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes =
        cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // per evitare problemi di lunghezza di chiave, anche se non sarebbe proprio la
    // scelta migliore
    private static String pad(String key) {
        if (key.length() > 16) {
            return key.substring(0, 16);
        }

        while (key.length() % 8 != 0) {
            key += "0";
        }

        return key;
    }

    public static String hash(String plain) {
        try {
            String hash = BCrypt.hashpw(plain, BCrypt.gensalt(12));

            return hash.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return plain;
        }
    }

    public static boolean validatePassword(String plainPW, String hashedPW) {
        try {
            return BCrypt.checkpw(plainPW, hashedPW);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}