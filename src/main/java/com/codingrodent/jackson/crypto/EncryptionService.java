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

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.slf4j.*;

import javax.validation.*;
import java.util.Set;

/**
 * Encryption / decryption functionality to handle the processing of {@link Encrypt} marked fields to/from JSON
 */
public class EncryptionService {
    private static final Logger logger = LoggerFactory.getLogger(EncryptionService.class);
    private final ObjectMapper mapper;
    private final Validator validator;
    private final ICryptoContext cryptoContext;

    /**
     * Construct crypto service
     *
     * @param objectMapper  Object objectMapper to use
     * @param validator     Validator to use
     * @param cryptoContext Crypto to use
     * @throws EncryptionException Thrown on any error
     */
    public EncryptionService(final ObjectMapper objectMapper, final Validator validator, final ICryptoContext cryptoContext) throws EncryptionException {
        this.mapper = objectMapper;
        this.validator = validator;
        this.cryptoContext = cryptoContext;
        objectMapper.registerModule(new CryptoModule(this));
    }

    /**
     * Construct crypto service using a default validator
     *
     * @param mapper        Object mapper to use
     * @param cryptoContext Crypto to use
     * @throws EncryptionException Thrown on any error
     */
    public EncryptionService(final ObjectMapper mapper, final ICryptoContext cryptoContext) throws EncryptionException {
        this(mapper, Validation.buildDefaultValidatorFactory().getValidator(), cryptoContext);
    }

    /**
     * Encrypt a byte array as a JSON message
     *
     * @param data Byte array to encrypt
     * @return JSON message containing the encrypted byte array
     * @throws EncryptionException Thrown on any error
     */
    public EncryptedJson encrypt(final byte[] data) throws EncryptionException {
        EncryptedJson result = new EncryptedJson();
        result.setIv(cryptoContext.getIv());
        result.setSalt(cryptoContext.getSalt());
        result.setValue(cryptoContext.encrypt(data));
        return result;
    }

    /**
     * Encrypt a string
     *
     * @param text     String to encrypt
     * @param encoding String encoding, e.g. UTF-8
     * @return JSON message containing the encrypted byte array
     * @throws EncryptionException Thrown on any error
     */
    public EncryptedJson encrypt(final String text, final String encoding) throws EncryptionException {
        try {
            return encrypt(text.getBytes(encoding));
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

    /**
     * Decrypt an encrypted byte array
     *
     * @param value Pojo derived from JSON
     * @return Decrypted byte array
     */
    public byte[] decrypt(EncryptedJson value) {
        validate(value);
        return cryptoContext.decrypt(value);
    }

    /**
     * Custom decrypt for EncryptedJSON class
     *
     * @param parser       JSON parser being used by Jackson
     * @param deserializer Base deserializer being used by
     * @param context      Context for the process of deserialization a single root-level value
     * @param type         Declared type of target
     * @return Decrypted object
     */
    public Object decrypt(final JsonParser parser, final JsonDeserializer<?> deserializer, final DeserializationContext context, final JavaType type) {
        try {
            return null == deserializer ? mapper.readValue(decrypt(mapper.readValue(parser, EncryptedJson.class)), type) : deserializer.deserialize(mapper.getFactory().createParser(decrypt(mapper.readValue(parser, EncryptedJson.class))), context);
        } catch (Exception e) {
            throw new EncryptionException("Unable to decrypt document", e);
        }
    }

    private void validate(EncryptedJson encrypted) throws EncryptionException {
        if (encrypted == null) {
            throw new EncryptionException("null encrypted value encountered");
        } else {
            Set<ConstraintViolation<EncryptedJson>> violations = validator.validate(encrypted);
            if (!violations.isEmpty()) {
                String message = String.format("invalid encrypted value%n%s", validationErrorMessage(encrypted, violations));
                logger.warn(message);
                throw new EncryptionException(message);
            }
        }
    }

    private String validationErrorMessage(final EncryptedJson encrypted, final Set<ConstraintViolation<EncryptedJson>> violations) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("value:").append(mapper.writeValueAsString(encrypted)).append("\n");
        } catch (JsonProcessingException e) {
            sb.append(e.getMessage()).append("\n");
        }
        sb.append("violations:\n");
        for (final ConstraintViolation violation : violations) {
            sb.append("- ").append(violation.getPropertyPath().toString()).append(" ").append(violation.getMessage()).append("\n");
        }
        return sb.toString();
    }

}
