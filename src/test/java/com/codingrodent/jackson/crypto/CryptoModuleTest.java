/*
 * MIT License
 *
 *         Copyright (c) 2018,2019,2020
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
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
        assertEquals("2.2.0", version.toString());
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