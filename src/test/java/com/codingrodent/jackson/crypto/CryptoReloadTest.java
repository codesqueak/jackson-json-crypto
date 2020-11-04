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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CryptoReloadTest {

    private final static String TEST_JSON = "{\"critical\":{\"salt\":\"IRqsz99no75sx9SCGrzOSEdoMVw=\",\"iv\":\"bfKvxBhq7X5su9VtvDdOGQ==\"," +
            "\"value\":\"pXWsFPzCnmPieitbGfkvofeQE3fj0Kb4mSP7e28+Jc0=\"}}";

    @Test
    public void encryptReload() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EncryptionService encryptionService = new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));

        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("Something very secure ...");

        SecurePropertyPoJo pojo2 = objectMapper.readValue(TEST_JSON, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptReloadChangePasswordsThrowException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EncryptionService encryptionService = new EncryptionService(objectMapper, new PasswordCryptoContext("Password1", "Password2"));
        objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));

        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("Something very secure ...");

        SecurePropertyPoJo pojo2 = objectMapper.readValue(TEST_JSON, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());

        String json = objectMapper.writeValueAsString(pojo2);
        assertThrows(JsonMappingException.class, () -> objectMapper.readValue(json, SecurePropertyPoJo.class));
    }

    @Test
    public void encryptReloadChangePasswordsSecondService() throws Exception {
        ObjectMapper objectMapper1 = new ObjectMapper();
        EncryptionService encryptionService1 = new EncryptionService(objectMapper1, new PasswordCryptoContext("Password1", "Password2"));
        objectMapper1.registerModule(new CryptoModule().addEncryptionService(encryptionService1));

        ObjectMapper objectMapper2 = new ObjectMapper();
        EncryptionService encryptionService2 = new EncryptionService(objectMapper2, new PasswordCryptoContext("Password2"));
        objectMapper2.registerModule(new CryptoModule().addEncryptionService(encryptionService2));

        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("Something very secure ...");

        SecurePropertyPoJo pojo2 = objectMapper1.readValue(TEST_JSON, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());

        String json = objectMapper1.writeValueAsString(pojo2);
        SecurePropertyPoJo pojo3 = objectMapper2.readValue(json, SecurePropertyPoJo.class);
        assertEquals(pojo2.getCritical(), pojo3.getCritical());
    }

}