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
import java.security.*;
import java.util.Optional;

public class DefaultCryptoContext extends BaseCryptoContext {
    private static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
    private static final String KEY_NAME = "PBKDF2WithHmacSHA256";
    private Optional<String> password;

    public DefaultCryptoContext() throws EncryptionException {
        this("password");
    }

    public DefaultCryptoContext(final String password) throws EncryptionException {
        try {
            this.setPassword(Optional.of(password));
            byte[] salt = this.generateSalt();
            this.setSalt(Optional.of(this.generateSalt()));
            SecretKeySpec secretKeySpec = this.getSecretKeySpec(salt, password);
            this.setSecretKeySpec(secretKeySpec);
            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(1, secretKeySpec);
            AlgorithmParameters params = cipher.getParameters();
            this.setIv(params.getParameterSpec(IvParameterSpec.class).getIV());
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return bytes;
    }

    private SecretKeySpec getSecretKeySpec(final byte[] salt, final String password) throws EncryptionException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_NAME);
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65556, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            return new SecretKeySpec(secretKey.getEncoded(), "AES");
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public String getKeyName() {
        return KEY_NAME;
    }

    public String getCipherName() {
        return CIPHER_NAME;
    }

    public void setPassword(final Optional<String> password) {
        this.password = password;
    }

    public Optional<String> getPassword() {
        return this.password;
    }
}

