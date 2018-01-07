package com.codingrodent.jackson.crypto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.security.SecureRandom;

import static org.junit.Assert.*;

public class EncryptionServiceTest {

    @Test
    public void encryptBytes() {
        ObjectMapper objectMapper = new ObjectMapper();
        ICryptoContext context = new PasswordCryptoContext("Password1");
        EncryptionService service = new EncryptionService(objectMapper, context);
        SecureRandom random = new SecureRandom();
        byte[] clear = new byte[100];
        random.nextBytes(clear);
        //
        EncryptedJson encryptedJson = service.encrypt(clear);
        assertArrayEquals(encryptedJson.getSalt(), context.getSalt());
        assertArrayEquals(encryptedJson.getIv(), context.getIv());
        //
        byte[] decrypted = service.decrypt(encryptedJson);
        assertArrayEquals(clear, decrypted);
    }

    @Test
    public void encryptString() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ICryptoContext context = new PasswordCryptoContext("Password1");
        EncryptionService service = new EncryptionService(objectMapper, context);
        String clear = "A clear string to encrypt";
        //
        EncryptedJson encryptedJson = service.encrypt(clear, "UTF-8");
        assertArrayEquals(encryptedJson.getSalt(), context.getSalt());
        assertArrayEquals(encryptedJson.getIv(), context.getIv());
        //
        byte[] decrypted = service.decrypt(encryptedJson);
        assertEquals(clear, new String(decrypted, "UTF-8"));
    }

}