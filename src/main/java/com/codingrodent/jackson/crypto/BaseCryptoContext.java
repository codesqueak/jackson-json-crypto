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

import javax.crypto.Cipher;
import javax.crypto.spec.*;
import java.util.*;

import static javax.crypto.Cipher.*;

public abstract class BaseCryptoContext implements ICryptoContext {
    private byte[] iv;
    private Optional<byte[]> salt;
    private SecretKeySpec secretKeySpec;

    public String decrypt(final String source) throws EncryptionException {
        try {
            byte[] cipherBytes = Base64.getDecoder().decode(source);
            byte[] decryptedTextBytes = getDecryptCipher().doFinal(cipherBytes);
            return new String(decryptedTextBytes, "UTF-8");
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public byte[] decrypt(final byte[] source) throws EncryptionException {
        try {
            return getDecryptCipher().doFinal(source);
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

    private Cipher getDecryptCipher() throws EncryptionException {
        return getCipher(DECRYPT_MODE);
    }

    private Cipher getEncryptCipher() throws EncryptionException {
        return getCipher(ENCRYPT_MODE);
    }

    private Cipher getCipher(final int mode) throws EncryptionException {
        try {
            Cipher cipher = Cipher.getInstance(getCipherName());
            cipher.init(mode, getSecretKeySpec(), new IvParameterSpec(getIv()));
            return cipher;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    public void setIv(final byte[] iv) {
        this.iv = Arrays.copyOf(iv, iv.length);
    }

    public void setSalt(final Optional<byte[]> salt) {
        this.salt = salt;
    }

    public SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }

    public void setSecretKeySpec(final SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    public byte[] getIv() {
        return Arrays.copyOf(iv, iv.length);
    }

    public Optional<byte[]> getSalt() {
        return salt;
    }
}
