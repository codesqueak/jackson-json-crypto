package com.codingrodent.jackson.crypto;

import com.codingrodent.jackson.crypto.pojos.SecurePropertyPoJo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import java.security.SecureRandom;

import static org.junit.Assert.*;

public class EncryptionServiceTest {

    private ObjectMapper objectMapper;
    private ICryptoContext context;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        context = new PasswordCryptoContext("Password1");
    }

    @Test(expected = EncryptionException.class)
    public void constructor1Test() {
        new EncryptionService(null, context);
    }

    @Test(expected = EncryptionException.class)
    public void constructor2Test() {
        new EncryptionService(objectMapper, null);
    }

    @Test(expected = EncryptionException.class)
    public void constructor3Test() {
        new EncryptionService(objectMapper, null, context);
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
    public void encryptString() throws Exception {
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

    @Test
    public void quickEncryptString() throws Exception {
        ObjectMapper objectMapper = EncryptionService.getInstance(new PasswordCryptoContext("Password1"));
        //
        SecurePropertyPoJo pojo = new SecurePropertyPoJo();
        pojo.setCritical("A clear string to encrypt");

        String json = objectMapper.writeValueAsString(pojo);
        SecurePropertyPoJo pojo2 = objectMapper.readValue(json, SecurePropertyPoJo.class);
        assertEquals(pojo.getCritical(), pojo2.getCritical());
    }

}