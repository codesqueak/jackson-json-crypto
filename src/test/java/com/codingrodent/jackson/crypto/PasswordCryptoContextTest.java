package com.codingrodent.jackson.crypto;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordCryptoContextTest {

    @Test
    public void testConstructorGood() {
        String password = "password1";
        new PasswordCryptoContext(password, password, PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME);
    }

    @Test
    public void testConstructorShort() {
        String password = "short";
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext(password));
    }

    @Test
    public void testConstructorNullCipherName() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", null, PasswordCryptoContext.KEY_NAME));
    }

    @Test
    public void testConstructorNullKeyName() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, null));
    }

    @Test
    public void testConstructorUnknownCipher() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", "UnknownCipher", PasswordCryptoContext.KEY_NAME));
    }

    @Test
    public void testConstructorUnknownKey() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, "UnknownKey"));
    }

    @Test
    public void testConstructorLowIterationCount() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME, 2000, 64, "DES"));
    }

    @Test
    public void testConstructorNullAlgorithmType() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME, 8000, 64, null));
    }

    @Test
    public void testConstructorWrongKeyLengthForDES() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME, 8000, 128, "DES"));
    }

    @Test
    public void testConstructorWrongKeyLengthForAES() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME, 8000, 64, "AES"));
    }

    @Test
    public void testConstructorUnknownAlgorithmType() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", "password1", PasswordCryptoContext.CIPHER_NAME, PasswordCryptoContext.KEY_NAME, 8000, 64, "UnknownAlgorithm"));
    }

    @Test
    public void testConstructor1() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext(null));
    }

    @Test
    public void testConstructor2() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext(null, "password1"));
    }

    @Test
    public void testConstructor3() {
        assertThrows(EncryptionException.class, () -> new PasswordCryptoContext("password1", null));
    }
}