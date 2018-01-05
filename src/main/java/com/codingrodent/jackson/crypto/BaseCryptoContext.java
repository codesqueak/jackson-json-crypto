/*
The MIT License

Copyright (c) 2017

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.codingrodent.jackson.crypto;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.SecureRandom;
import java.util.*;

import static javax.crypto.Cipher.*;

public abstract class BaseCryptoContext implements ICryptoContext {
    public static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
    public static final String KEY_NAME = "PBKDF2WithHmacSHA512";

    private byte[] iv;
    private byte[] salt;
    private SecretKeySpec secretKeySpec;
    private String readPassword;

    @Override
    public byte[] decrypt(final EncryptedJson value) throws EncryptionException {
        try {
            return getDecryptCipher(value.getIv(), value.getSalt()).doFinal(value.getValue());
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String encrypt(final String source) throws EncryptionException {
        try {
            byte[] encryptedTextBytes = getEncryptCipher().doFinal(source.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedTextBytes);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public byte[] encrypt(final byte[] source) throws EncryptionException {
        try {
            return getEncryptCipher().doFinal(source);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private Cipher getDecryptCipher(final byte[] iv, final byte[] salt) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(getCipherName());

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKeySpec = getSecretKeySpec(salt, readPassword);

            cipher.init(DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private Cipher getEncryptCipher() throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(getCipherName());

            IvParameterSpec ivParameterSpec = new IvParameterSpec(getIv());

            cipher.init(ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    public SecretKeySpec getSecretKeySpec(final byte[] salt, final String password) throws EncryptionException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_NAME);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65556, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    @Override
    public void setSecretKeySpec(final SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    @Override
    public byte[] getIv() {
        return Arrays.copyOf(iv, iv.length);
    }

    @Override
    public void setIv(byte[] iv) {
        this.iv = Arrays.copyOf(iv, iv.length);
    }

    @Override
    public byte[] getSalt() {
        return Arrays.copyOf(salt, salt.length);
    }

    @Override
    public void setSalt(final byte[] salt) {
        this.salt = Arrays.copyOf(salt, salt.length);
    }

    @Override
    public void setReadPassword(String password) {
        this.readPassword = password;
    }

    @Override
    public String getKeyName() {
        return KEY_NAME;
    }

    @Override
    public String getCipherName() {
        return CIPHER_NAME;
    }
}
