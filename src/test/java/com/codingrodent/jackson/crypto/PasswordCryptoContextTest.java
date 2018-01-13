package com.codingrodent.jackson.crypto;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class PasswordCryptoContextTest {

    @Test
    public void testConstructorGood() {
        String password = "password1";
        assertTrue(password.length() >= PasswordCryptoContext.MIN_PASSWORD_LENGTH);
        new PasswordCryptoContext(password);
    }

    @Test(expected = EncryptionException.class)
    public void testConstructorShort() {
        String password = "short";
        assertTrue(password.length() < PasswordCryptoContext.MIN_PASSWORD_LENGTH);
        new PasswordCryptoContext(password);
    }

    @Test(expected = EncryptionException.class)
    public void testConstructor1() {
        new PasswordCryptoContext(null);
    }

    @Test(expected = EncryptionException.class)
    public void testConstructor2() {
        new PasswordCryptoContext(null, "password1");
    }

    @Test(expected = EncryptionException.class)
    public void testConstructor3() {
        new PasswordCryptoContext("password1", null);
    }
}