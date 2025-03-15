package com.automation.core.utils;

import com.automation.core.logger.LoggerManager;
import com.automation.core.testData.TestDataManager;
import org.slf4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.util.Base64;

public class PasswordManagerUtil implements PasswordManager {

    private static final Logger logger = LoggerManager.getLogger(PasswordManagerUtil.class);
    private static final String AES_ALGORITHM = "AES";
    private static final String KEYSTORE_FILE = "./certs/keystore/mykeystore.p12"; // Keystore file
    private static final String KEYSTORE_PASSWORD = "changeit";   // Keystore password
    private static final String SECRET_KEY_ALIAS = System.getenv("KEYSTORE_SECRET_ALIAS"); // Load alias from env variable
    private static final String SECRET_KEY_STRING = System.getenv("KEYSTORE_SECRET_ALIAS"); // Fixed 16-byte AES key

    public SecretKey loadKeyFromKeystore() throws Exception {
        // Validate that the alias is set
        if (SECRET_KEY_ALIAS == null || SECRET_KEY_ALIAS.isEmpty()) {
            throw new RuntimeException("Error: Secret key alias is missing. Set the environment variable 'KEYSTORE_SECRET_ALIAS'.");
        }

        // Load the KeyStore
        FileInputStream fis = new FileInputStream(KEYSTORE_FILE);
        KeyStore keystore = KeyStore.getInstance("PKCS12");
        keystore.load(fis, KEYSTORE_PASSWORD.toCharArray());

        // Check if alias exists
        if (!keystore.containsAlias(SECRET_KEY_ALIAS)) {
            throw new RuntimeException("Error: Secret key alias '" + SECRET_KEY_ALIAS + "' not found in KeyStore!");
        }

        // Retrieve SecretKey
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keystore.getEntry(
                SECRET_KEY_ALIAS,
                new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray())
        );

        if (entry == null) {
            throw new RuntimeException("Error: Secret key entry is null!");
        }

        return entry.getSecretKey();
    }


    /**
     * Encrypts a given password using AES and Base64 encoding.
     * @param password The password to encrypt.
     * @return Base64 encoded encrypted password.
     */
    public String encrypt(String password) throws Exception {
        SecretKey secretKey = loadKeyFromKeystore();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts an encrypted password using AES and Base64 decoding.
     * @param encryptedPassword The Base64 encoded encrypted password.
     * @return Decrypted original password.
     */
    public String decrypt(String encryptedPassword) throws Exception {
        SecretKey secretKey = loadKeyFromKeystore();
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        return new String(decryptedBytes);
    }

    public void createPKCS12KeyStore(){
        try {
            // Create a new PKCS12 Keystore
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            keystore.load(null, null);

            // Create AES Secret Key
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY_STRING.getBytes(), "AES");

            // Store the AES Key inside the Keystore
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(secretKey);
            KeyStore.ProtectionParameter entryPassword = new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray());
            keystore.setEntry(SECRET_KEY_ALIAS, secretKeyEntry, entryPassword);

            // Save Keystore to file
            try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
                keystore.store(fos, KEYSTORE_PASSWORD.toCharArray());
            }

            logger.info("Keystore successfully created with alias: " + SECRET_KEY_ALIAS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
