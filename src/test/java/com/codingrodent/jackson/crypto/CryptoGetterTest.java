/*
 * MIT License
 *
 *         Copyright (c) 2017
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

import com.codingrodent.jackson.crypto.pojos.SecureGetterPoJo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import javax.validation.Validation;

import static org.junit.Assert.assertEquals;

public class CryptoGetterTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void encryptDefault() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new CryptoModule());
        EncryptionService.getInstance(objectMapper);

        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("Something very secure ...");

        String json = objectMapper.writeValueAsString(pojo);
        System.out.println(json);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        System.out.println(pojo2.getCritical());
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptCustomValidator() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new CryptoModule());
        EncryptionService.getInstance(objectMapper, Validation.buildDefaultValidatorFactory().getValidator());

        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("Something very secure ...");

        String json = objectMapper.writeValueAsString(pojo);
        System.out.println(json);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        System.out.println(pojo2.getCritical());
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

}