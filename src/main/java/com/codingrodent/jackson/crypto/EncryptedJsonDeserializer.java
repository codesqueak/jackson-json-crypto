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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

/**
 * Implementation of  {@link JsonDeserializer} to supply a callback
 * that can be used to create contextual (context-dependent) instances of
 * deserializer - Useful for annotation handling
 */
public class EncryptedJsonDeserializer extends JsonDeserializer<Object> implements ContextualDeserializer {
    private final EncryptionService service;
    private final JsonDeserializer<Object> baseDeserializer;
    private final BeanProperty property;

    public EncryptedJsonDeserializer(final EncryptionService service, final JsonDeserializer<Object> baseDeserializer) {
        this.service = service;
        this.baseDeserializer = baseDeserializer;
        this.property = null;
    }

    public EncryptedJsonDeserializer(final EncryptionService service, final JsonDeserializer<Object> wrapped, final BeanProperty property) {
        this.service = service;
        this.baseDeserializer = wrapped;
        this.property = property;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Encrypted field deserializer
     */
    @Override
    public Object deserialize(final JsonParser parser, final DeserializationContext context) throws JsonMappingException {
        JsonDeserializer<?> deserializer = baseDeserializer;
        if (deserializer instanceof ContextualDeserializer) {
            deserializer = ((ContextualDeserializer) deserializer).createContextual(context, property);
        }
        return service.decrypt(parser, deserializer, context, null == property ? null : property.getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext context, final BeanProperty property) {
        return new EncryptedJsonDeserializer(service, baseDeserializer, property);
    }
}
