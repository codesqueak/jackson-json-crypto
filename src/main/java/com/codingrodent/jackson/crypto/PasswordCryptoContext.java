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

/**
 * Default crypto context for handling password based encryption processes
 */
public class PasswordCryptoContext extends BaseCryptoContext {
    public static final String CIPHER_NAME = "AES/CBC/PKCS5Padding";
    public static final String KEY_NAME = "PBKDF2WithHmacSHA512";
    public static final int MIN_PASSWORD_LENGTH = 8;

    public PasswordCryptoContext(final String readPassword, final String writePassword) throws EncryptionException {
        super(readPassword, writePassword, CIPHER_NAME, KEY_NAME);
        if ((null == readPassword) || (null == writePassword))
            throw new IllegalArgumentException("Password cannot be null");

        if ((readPassword.length() < MIN_PASSWORD_LENGTH) || (writePassword.length() < MIN_PASSWORD_LENGTH))
            throw new IllegalArgumentException("Minimum password length " + MIN_PASSWORD_LENGTH + " characters");
    }

    public PasswordCryptoContext(final String password) throws EncryptionException {
        this(password, password);
    }
}

