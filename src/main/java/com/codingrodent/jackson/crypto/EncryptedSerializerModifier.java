/*
The MIT License

Copyright (c) 2018,2019,2020

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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation that defines API for objects that can be registered (for {@link BeanSerializerFactory}
 * to participate in constructing {@link BeanSerializer} instances.
 */
public class EncryptedSerializerModifier extends BeanSerializerModifier {
    private static final Logger logger = LoggerFactory.getLogger(EncryptedSerializerModifier.class);

    private final EncryptionService encryptionService;

    /**
     * Constructor
     *
     * @param encryptionService Encryption services to use to handle {@link Encrypt} marked fields
     */
    public EncryptedSerializerModifier(final EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Add serialization functionality for {@link Encrypt} marked fields
     */
    @Override
    public List<BeanPropertyWriter> changeProperties(final SerializationConfig config, final BeanDescription beanDescription, final List<BeanPropertyWriter> beanProperties) {
        var newWriters = new ArrayList<BeanPropertyWriter>();
        for (final BeanPropertyWriter writer : beanProperties) {
            if (null == writer.getAnnotation(Encrypt.class)) {
                newWriters.add(writer);
            } else {
                try {
                    // ToDo - only encrypt strings at the moment
                    var encryptSer = new EncryptedJsonSerializer(encryptionService, writer.getSerializer());
                    newWriters.add(new EncryptedPropertyWriter(writer, encryptSer));
                } catch (Exception e) {
                    logger.error("Only string encryption supported at present");
                    throw new EncryptionException("Type to encrypt is unsupported");
                }
            }
        }
        return newWriters;
    }

    static class EncryptedPropertyWriter extends BeanPropertyWriter {
        EncryptedPropertyWriter(final BeanPropertyWriter base, final JsonSerializer<Object> serializer) {
            super(base);
            this._serializer = serializer;
        }
    }
}
