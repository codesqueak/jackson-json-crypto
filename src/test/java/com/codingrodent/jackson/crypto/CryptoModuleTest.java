package com.codingrodent.jackson.crypto;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoModuleTest {

    private CryptoModule cryptoModule;

    @BeforeEach
    public void setup() {
        cryptoModule = new CryptoModule();
    }

    @Test
    public void addEncryptionService() {
        EncryptionService encryptionService = new EncryptionService(new ObjectMapper(), new PasswordCryptoContext("password"));
        cryptoModule = new CryptoModule();
        assertTrue(cryptoModule == cryptoModule.addEncryptionService(encryptionService));
    }

    @Test
    public void getModuleName() {
        assertEquals(CryptoModule.ARTIFACT_ID, cryptoModule.getModuleName());
    }

    @Test
    public void version() {
        cryptoModule = new CryptoModule();
        Version version = cryptoModule.version();
        assertEquals("2.0.0", version.toString());
        assertEquals(CryptoModule.ARTIFACT_ID, version.getArtifactId());
        assertEquals(CryptoModule.GROUP_ID, version.getGroupId());
    }

    @Test
    public void setupModuleFail() {
        SimpleModule.SetupContext context = EasyMock.createMock(SimpleModule.SetupContext.class);
        replay();
        assertThrows(EncryptionException.class, () -> cryptoModule.setupModule(context));
    }

    @Test
    public void setupModule() {
        SimpleModule.SetupContext context = EasyMock.createMock(SimpleModule.SetupContext.class);
        context.addBeanDeserializerModifier(isA(BeanDeserializerModifier.class));
        expectLastCall().times(1);
        context.addBeanSerializerModifier(isA(BeanSerializerModifier.class));
        expectLastCall().times(1);
        replay();
        //
        EncryptionService encryptionService = new EncryptionService(new ObjectMapper(), new PasswordCryptoContext("password"));
        cryptoModule.addEncryptionService(encryptionService);
        cryptoModule.setupModule(context);
        //
        verify();
    }
}