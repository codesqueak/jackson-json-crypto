/*
 * MIT License
 *
 *         Copyright (c) 2018
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy
 *         of this software and associated documentation files (the "Software"), to deal
 *         in the Software without restriction, including without limitation the rights
 *         to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *         copies of the Software, and to permit persons to whom the Software is
 *         furnished to do so, subject to the following conditions:
 *
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *         FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *         AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *         LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *         OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *         SOFTWARE.
 */
package com.codingrodent.jackson.crypto;

import com.codingrodent.jackson.crypto.pojos.SecurePropertyPoJo;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ValidationTest {

    private final static String TEST_JSON_NULL_SALT = "{\"critical\":{\"salt\":null,\"iv\":\"bfKvxBhq7X5su9VtvDdOGQ==\"," +
            "\"value\":\"pXWsFPzCnmPieitbGfkvofeQE3fj0Kb4mSP7e28+Jc0=\"}}";
    private final static String TEST_JSON_NULL_IV = "{\"critical\":{\"salt\":\"IRqsz99no75sx9SCGrzOSEdoMVw=\",\"iv\":null," +
            "\"value\":\"pXWsFPzCnmPieitbGfkvofeQE3fj0Kb4mSP7e28+Jc0=\"}}";
    private final static String TEST_JSON_NULL_VALUE = "{\"critical\":{\"salt\":\"IRqsz99no75sx9SCGrzOSEdoMVw=\",\"iv\":\"bfKvxBhq7X5su9VtvDdOGQ==\"," + "\"value\":null}}";
    private final static String TEST_JSON_MULTIPLE_NULLS = "{\"critical\":{\"salt\":null,\"iv\":null," + "\"value\":null}}";

    @Test
    public void nullSaltValidatorTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(TEST_JSON_NULL_SALT, SecurePropertyPoJo.class));
    }

    @Test
    public void nullIVValidatorTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(TEST_JSON_NULL_IV, SecurePropertyPoJo.class));
    }

    @Test
    public void nullValueValidatorTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(TEST_JSON_NULL_VALUE, SecurePropertyPoJo.class));
    }

    @Test
    public void multipleErrorsValidatorTest() {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(TEST_JSON_MULTIPLE_NULLS, SecurePropertyPoJo.class));
    }
}