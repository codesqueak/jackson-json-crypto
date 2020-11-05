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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CryptoPropertyTest {

    @Test
    public void encryptDefault() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));

        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("Something very secure ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecurePropertyPoJo pojo2 = objectMapper.readValue(json, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptCustomValidator() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        new EncryptionService(objectMapper, Validation.buildDefaultValidatorFactory().getValidator(), new PasswordCryptoContext("Password1"));

        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("Something very secure ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecurePropertyPoJo pojo2 = objectMapper.readValue(json, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }
}