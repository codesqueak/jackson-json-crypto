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

    @Test
    public void addEncryptionService() {
        var encryptionService = new EncryptionService(new ObjectMapper(), new PasswordCryptoContext("password"));
        var cryptoModule = new CryptoModule();
        assertSame(cryptoModule, cryptoModule.addEncryptionService(encryptionService));
    }

    @Test
    public void getModuleName() {
        var cryptoModule = new CryptoModule();
        assertEquals(CryptoModule.ARTIFACT_ID, cryptoModule.getModuleName());
    }

    @Test
    public void version() {
        var cryptoModule = new CryptoModule();
        var version = cryptoModule.version();
        assertEquals("2.0.0", version.toString());
        assertEquals(CryptoModule.ARTIFACT_ID, version.getArtifactId());
        assertEquals(CryptoModule.GROUP_ID, version.getGroupId());
    }

    @Test
    public void setupModuleFail() {
        var cryptoModule = new CryptoModule();
        SimpleModule.SetupContext context = EasyMock.createMock(SimpleModule.SetupContext.class);
        assertThrows(EncryptionException.class, () -> cryptoModule.setupModule(context));
    }

    @Test
    public void setupModule() {
        var cryptoModule = new CryptoModule();
        SimpleModule.SetupContext context = EasyMock.createMock(SimpleModule.SetupContext.class);
        context.addBeanDeserializerModifier(isA(BeanDeserializerModifier.class));
        expectLastCall().times(1);
        context.addBeanSerializerModifier(isA(BeanSerializerModifier.class));
        expectLastCall().times(1);
        //
        var encryptionService = new EncryptionService(new ObjectMapper(), new PasswordCryptoContext("password"));
        cryptoModule.addEncryptionService(encryptionService);
        cryptoModule.setupModule(context);
    }
}