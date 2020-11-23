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

import com.codingrodent.jackson.crypto.pojos.SecureGetterPoJo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CryptoDemoTest {

    @Test
    public void encryptDetailedSetupDemoWithDESAlgo() throws Exception {
        // get an object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // set up a custom crypto context - Defines teh interface to the crypto algorithms used
        ICryptoContext cryptoContext = new PasswordCryptoContext("Password", "Password",
                "DES/CBC/PKCS5Padding", "PBKDF2WithHmacSHA256", 10000, 64, "DES");
        // The encryption service holds functionality to map clear to / from encrypted JSON
        EncryptionService encryptionService = new EncryptionService(objectMapper, cryptoContext);
        // Create a Jackson module and tell it about the encryption service
        CryptoModule cryptoModule = new CryptoModule().addEncryptionService(encryptionService);
        // Tell Jackson about the new module
        objectMapper.registerModule(cryptoModule);
        //
        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("The long way to set up JSON crypto ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptDetailedSetupDemo() throws Exception {
        // get an object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        // set up a custom crypto context - Defines teh interface to the crypto algorithms used
        ICryptoContext cryptoContext = new PasswordCryptoContext("Password");
        // The encryption service holds functionality to map clear to / from encrypted JSON
        EncryptionService encryptionService = new EncryptionService(objectMapper, cryptoContext);
        // Create a Jackson module and tell it about the encryption service
        CryptoModule cryptoModule = new CryptoModule().addEncryptionService(encryptionService);
        // Tell Jackson about the new module
        objectMapper.registerModule(cryptoModule);
        //
        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("The long way to set up JSON crypto ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptQuickSetupDemo() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        EncryptionService encryptionService = new EncryptionService(objectMapper, new PasswordCryptoContext("Password1"));
        objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));

        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("The short way to set up JSON crypto ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

    @Test
    public void encryptVeryQuickSetupDemo() throws Exception {
        ObjectMapper objectMapper = EncryptionService.getInstance("Password1");

        SecureGetterPoJo pojo = new SecureGetterPoJo();
        pojo.setCritical("The very short way to set up JSON crypto ...");

        String json = objectMapper.writeValueAsString(pojo);
        SecureGetterPoJo pojo2 = objectMapper.readValue(json, SecureGetterPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }
}