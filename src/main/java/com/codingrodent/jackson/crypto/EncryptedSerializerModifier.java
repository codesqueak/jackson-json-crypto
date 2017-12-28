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

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.*;

import java.util.*;

public class EncryptedSerializerModifier extends BeanSerializerModifier {
    public EncryptedSerializerModifier() {
    }

    public List<BeanPropertyWriter> changeProperties(final SerializationConfig config, final BeanDescription beanDescription, final List<BeanPropertyWriter> beanProperties) {
        List<BeanPropertyWriter> newWriters = new ArrayList<>();

        for (final BeanPropertyWriter writer : beanProperties) {
            if (null == writer.getAnnotation(Encrypt.class)) {
                newWriters.add(writer);
            } else {
                try {
                    JsonSerializer<Object> encryptSer = new EncryptedJsonSerializer(EncryptionService.getInstance(), writer.getSerializer());
                    newWriters.add(new EncryptedPropertyWriter(writer, encryptSer));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return newWriters;
    }

    static class EncryptedPropertyWriter extends BeanPropertyWriter {
        public EncryptedPropertyWriter(final BeanPropertyWriter base, final JsonSerializer<Object> deserializer) {
            super(base);
            this._serializer = deserializer;
        }
    }
}
