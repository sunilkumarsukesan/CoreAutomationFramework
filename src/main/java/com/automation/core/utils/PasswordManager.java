package com.automation.core.utils;

import javax.crypto.SecretKey;

public interface PasswordManager {

    SecretKey loadKeyFromKeystore() throws Exception;

    String encrypt(String password) throws Exception;

    String decrypt(String encryptedPassword) throws Exception;

    void createPKCS12KeyStore();


}
