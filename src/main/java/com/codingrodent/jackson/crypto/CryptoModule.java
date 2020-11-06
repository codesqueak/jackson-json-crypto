/*
The MIT License

Copyright (c) 2018

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.codingrodent.jackson.crypto;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.util.ResourceBundle;

/**
 * Crypto Module for Jackson JSON library
 */
public class CryptoModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public final static String GROUP_ID = "com.codingrodent.jackson.crypto";
    public final static String ARTIFACT_ID = "jackson-json-crypto";
    //
    private final static String BUNDLE = CryptoModule.class.getPackage().getName() + ".config";
    //
    private final int major;
    private final int minor;
    private final int patch;
    //
    private transient EncryptedSerializerModifier serializerModifierModifier;
    private transient EncryptedDeserializerModifier deserializerModifierModifier;

    /**
     * Initialize module
     */
    public CryptoModule() {
        var rb = ResourceBundle.getBundle(BUNDLE);
        major = Integer.parseInt(rb.getString("projectVersionMajor"));
        minor = Integer.parseInt(rb.getString("projectVersionMinor"));
        patch = Integer.parseInt(rb.getString("projectVersionBuild"));
    }

    /**
     * Set the encryption service to use
     *
     * @param encryptionService Service to supply crypto functionality
     * @return Updated module
     */
    public CryptoModule addEncryptionService(final EncryptionService encryptionService) {
        serializerModifierModifier = new EncryptedSerializerModifier(encryptionService);
        deserializerModifierModifier = new EncryptedDeserializerModifier(encryptionService);
        return this;
    }

    /**
     * Method that returns a display that can be used by Jackson
     *
     * @return Name for display
     */
    @Override
    public String getModuleName() {
        return ARTIFACT_ID;
    }

    /**
     * Method that returns version of this module.
     *
     * @return Version info
     */
    @Override
    public Version version() {
        return new Version(major, minor, patch, null, GROUP_ID, ARTIFACT_ID);
    }

    /**
     * Method called by {@link ObjectMapper} when module is registered.
     *
     * @param context Context for 'registering extended' functionality.
     */
    @Override
    public void setupModule(final SetupContext context) {
        if ((null == serializerModifierModifier) || (null == deserializerModifierModifier))
            throw new EncryptionException("Crypto module not initialised with an encryption service");
        context.addBeanSerializerModifier(serializerModifierModifier);
        context.addBeanDeserializerModifier(deserializerModifierModifier);
    }
}

