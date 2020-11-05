package com.codingrodent.jackson.crypto;

import com.codingrodent.jackson.crypto.pojos.SecurePropertyPoJo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionServiceTest {

    private ObjectMapper objectMapper;
    private ICryptoContext context;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        context = new PasswordCryptoContext("Password1");
    }

    @Test
    public void constructor1Test() {
        assertThrows(EncryptionException.class, () -> new EncryptionService(null, context));
    }

    @Test
    public void constructor2Test() {
        assertThrows(EncryptionException.class, () -> new EncryptionService(objectMapper, null));
    }

    @Test
    public void constructor3Test() {
        assertThrows(EncryptionException.class, () -> new EncryptionService(objectMapper, null, context));
    }

    @Test
    public void encryptBytes() {
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
    public void encryptString() {
        EncryptionService service = new EncryptionService(objectMapper, context);
        String clear = "A clear string to encrypt";
        //
        EncryptedJson encryptedJson = service.encrypt(clear, "UTF-8");
        assertArrayEquals(encryptedJson.getSalt(), context.getSalt());
        assertArrayEquals(encryptedJson.getIv(), context.getIv());
        //
        byte[] decrypted = service.decrypt(encryptedJson);
        assertEquals(clear, new String(decrypted, StandardCharsets.UTF_8));
    }

    @Test
    public void quickEncryptString() throws Exception {
        var objectMapper = EncryptionService.getInstance("Password1");
        //
        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("A clear string to encrypt");

        String json = objectMapper.writeValueAsString(pojo);
        SecurePropertyPoJo pojo2 = objectMapper.readValue(json, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

}