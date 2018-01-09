/*
The MIT License

Copyright (c) 2017

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
import com.fasterxml.jackson.databind.*;

/**
 * Crypto Module for Jackson JSON library
 */
public class CryptoModule extends Module {
    private final static int MAJOR = 1;
    private final static int MINOR = 0;
    private final static int PATCH = 0;
    private final static String GROUP_ID = "com.codingrodent.jackson.crypto";
    private final static String ARTIFACT_ID = "jackson-json-crypto";
    private final EncryptedSerializerModifier serializerModifierModifier;
    private final EncryptedDeserializerModifier deserializerModifierModifier;

    /**
     * Initialize module
     * @param encryptionService Service to supply crypto functionality
     */
    public CryptoModule(final EncryptionService encryptionService) {
        serializerModifierModifier = new EncryptedSerializerModifier(encryptionService);
        deserializerModifierModifier = new EncryptedDeserializerModifier(encryptionService);
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
        return new Version(MAJOR, MINOR, PATCH, null, GROUP_ID, ARTIFACT_ID);
    }

    /**
     * Method called by {@link ObjectMapper} when module is registered.
     *
     * @param context Context for 'registering extended' functionality.
     */
    @Override
    public void setupModule(final SetupContext context) {
        context.addBeanSerializerModifier(serializerModifierModifier);
        context.addBeanDeserializerModifier(deserializerModifierModifier);
    }
}

