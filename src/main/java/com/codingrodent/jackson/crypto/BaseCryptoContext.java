/*
The MIT License

Copyright (c) 2018,2019,2020

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
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.*;
import java.security.SecureRandom;
import java.util.Arrays;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

/**
 * Core crypto functionality. To be extended for specific purposes, e.g. password encryption
 */
public abstract class BaseCryptoContext implements ICryptoContext {

    public static final int DEFAULT_ITERATION_COUNT = 65556;
    public static final int DEFAULT_KEY_LENGTH = 256;
    public static final String DEFAULT_ALGORITHM_TYPE = "AES";

    private final int iterationCount;
    private final int keyLength;
    private final String algorithmType;

    private final byte[] iv;
    private final byte[] salt;
    private final SecretKeySpec writeSecretKeySpec;
    private final String readPassword, cipherName, keyAlgorithm;

    /**
     * Initialize crypto environment - Can use different passwords for read and write - e.g. When changing passwords
     *
     * @param readPassword   Password for read operations
     * @param writePassword  Password for write operations
     * @param cipherName     Name of cipher to be used for encryption, e.g. AES/CBC/PKCS5Padding
     * @param keyAlgorithm   Name of kay algorithm to use, e.g. PBKDF2WithHmacSHA512
     * @param iterationCount the iteration count e.g. 5000
     * @param keyLength      the to-be-derived key length e.g. 64
     * @param algorithmType  Name of algorithm type e.g. DES
     */
    public BaseCryptoContext(final String readPassword, final String writePassword, final String cipherName,
                             final String keyAlgorithm, int iterationCount, int keyLength, final String algorithmType) {

        if ((null == readPassword) || (null == writePassword))
            throw new EncryptionException("Password cannot be null");
        if (null == cipherName)
            throw new EncryptionException("Cipher Name cannot be null");
        if (null == keyAlgorithm)
            throw new EncryptionException("Key Algorithm cannot be null");
        if (null == algorithmType)
            throw new EncryptionException("Algorithm type cannot be null");
        if (iterationCount < 5000)
            throw new EncryptionException("Iteration Count cannot be less than 5000");

        this.iterationCount = iterationCount;
        this.keyLength = keyLength;
        this.algorithmType = algorithmType;
        this.readPassword = readPassword;
        this.cipherName = cipherName;
        this.keyAlgorithm = keyAlgorithm;
        this.salt = generateSalt();
        this.writeSecretKeySpec = createSecretKeySpec(salt, writePassword);

        try {
            var cipher = Cipher.getInstance(cipherName);
            cipher.init(ENCRYPT_MODE, writeSecretKeySpec);
            var algorithmParameters = cipher.getParameters();
            iv = algorithmParameters.getParameterSpec(IvParameterSpec.class).getIV();
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Initialize crypto environment - Can use different passwords for read and write - e.g. When changing passwords
     *
     * @param readPassword  Password for read operations
     * @param writePassword Password for write operations
     * @param cipherName    Name of cipher to be used for encryption, e.g. AES/CBC/PKCS5Padding
     * @param keyAlgorithm  Name of kay algorithm to use, e.g. PBKDF2WithHmacSHA512
     */
    public BaseCryptoContext(final String readPassword, final String writePassword, final String cipherName, final String keyAlgorithm) {
        this(readPassword, writePassword, cipherName, keyAlgorithm, DEFAULT_ITERATION_COUNT, DEFAULT_KEY_LENGTH, DEFAULT_ALGORITHM_TYPE);
    }

    /**
     * Decrypt an encrypted JSON object. Contains salt and iv as fields
     *
     * @param value JSON data
     * @return Decrypted byte array
     * @throws EncryptionException Something failed
     */
    @Override
    public byte[] decrypt(final EncryptedJson value) throws EncryptionException {
        try {
            return getDecryptCipher(value.getIv(), value.getSalt()).doFinal(value.getValue());
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Encrypted a string as a byte array and encode using base 64
     *
     * @param source Byte array to be encrypted
     * @return Encrypted data
     * @throws EncryptionException Something failed
     */
    @Override
    public byte[] encrypt(final byte[] source) throws EncryptionException {
        try {
            return getEncryptCipher().doFinal(source);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Get the initialization vector
     *
     * @return Vector as byte array
     */
    public byte[] getIv() {
        return Arrays.copyOf(iv, iv.length);
    }

    /**
     * Get the salt
     *
     * @return Salt as byte array
     */
    public byte[] getSalt() {
        return Arrays.copyOf(salt, salt.length);
    }

    // Internal functionality

    /**
     * Generate a cipher for decryption based on the supplied iv and salt
     *
     * @param iv   Initialization vector
     * @param salt Salt
     * @return Decryption cipher ready to use
     * @throws EncryptionException Something failed
     */
    private Cipher getDecryptCipher(final byte[] iv, final byte[] salt) throws EncryptionException {
        try {
            var cipher = Cipher.getInstance(cipherName);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            var secretKeySpec = createSecretKeySpec(salt, readPassword);
            cipher.init(DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Generate a cipher for encryption based on the supplied crypto parameters
     *
     * @return Encryption cipher ready to use
     * @throws EncryptionException Something failed
     */
    private Cipher getEncryptCipher() throws EncryptionException {
        try {
            var cipher = Cipher.getInstance(cipherName);
            var ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(ENCRYPT_MODE, writeSecretKeySpec, ivParameterSpec);
            return cipher;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Generate a random salt value
     *
     * @return Salt
     */
    private byte[] generateSalt() {
        var random = new SecureRandom();
        var bytes = new byte[16];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * Generate a secret key spec from supplied password and salt
     *
     * @param salt     Salt
     * @param password Password
     * @return Secret key spec
     * @throws EncryptionException Something failed
     */
    private SecretKeySpec createSecretKeySpec(final byte[] salt, final String password) throws EncryptionException {
        try {
            var factory = SecretKeyFactory.getInstance(keyAlgorithm);
            var passwordBasedEncryptionKeySpec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);
            var secretKey = factory.generateSecret(passwordBasedEncryptionKeySpec);
            return new SecretKeySpec(secretKey.getEncoded(), algorithmType);
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

}
