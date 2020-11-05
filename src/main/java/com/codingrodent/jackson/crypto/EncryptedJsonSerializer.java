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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Implementation of API used by {@link ObjectMapper}  for {@link JsonSerializer}s too) to serialize required objects
 */
public class EncryptedJsonSerializer extends JsonSerializer<Object> {
    private final JsonSerializer<Object> baseSerializer;
    private final EncryptionService encryptionService;

    public EncryptedJsonSerializer(final EncryptionService encryptionService, final JsonSerializer<Object> baseSerializer) {
        this.encryptionService = encryptionService;
        this.baseSerializer = baseSerializer;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Encrypted field serializer
     */
    @Override
    public void serialize(final Object object, final JsonGenerator generator, final SerializerProvider provider) throws IOException, EncryptionException {
        var writer = new StringWriter();
        // new generator to write the value whatever it is
        var nestedGenerator = generator.getCodec().getFactory().createGenerator(writer);
        if (null == baseSerializer)
            provider.defaultSerializeValue(object, nestedGenerator);
        else
            baseSerializer.serialize(object, nestedGenerator, provider);
        nestedGenerator.close();
        // now encrypt the output from the generator
        var encrypted = encryptionService.encrypt(writer.getBuffer().toString(), StandardCharsets.UTF_8.name());
        generator.writeObject(encrypted);
    }
}
