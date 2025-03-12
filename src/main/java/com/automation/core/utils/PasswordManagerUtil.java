package com.automation.core.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordManagerUtil {

    private static final String AES_ALGORITHM = "AES";

    /**
     * Encrypts a given password using AES and Base64 encoding.
     * @param password The password to encrypt.
     * @param secretKey The secret key used for encryption.
     * @return Base64 encoded encrypted password.
     */
    public static String encrypt(String password, String secretKey) throws Exception {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts an encrypted password using AES and Base64 decoding.
     * @param encryptedPassword The Base64 encoded encrypted password.
     * @param secretKey The secret key used for decryption.
     * @return Decrypted original password.
     */
    public static String decrypt(String encryptedPassword, String secretKey) throws Exception {
        SecretKey key = new SecretKeySpec(secretKey.getBytes(), AES_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes);
    }
}
