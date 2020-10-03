package com.corral.casino.service.utils;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class EncryptionUtils {
    private static final StrongPasswordEncryptor PASSWORD_ENCRYPTOR = new StrongPasswordEncryptor();

    public static final String encryptPassword(String password) {
        return PASSWORD_ENCRYPTOR.encryptPassword(password);
    }

    public static final boolean checkPassword(String plainPassword, String encryptedPassword) {
        return PASSWORD_ENCRYPTOR.checkPassword(plainPassword, encryptedPassword);
    }
}
