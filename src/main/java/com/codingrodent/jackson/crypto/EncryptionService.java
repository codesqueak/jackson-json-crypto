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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Convenience method to make a preconfigured ObjectMapper
     *
     * @param password Password to use
     * @return Configured ObjectMapper
     */
    public static ObjectMapper getInstance(final String password) {
        var objectMapper = new ObjectMapper();
        var encryptionService = new EncryptionService(objectMapper, new PasswordCryptoContext(password));
        objectMapper.registerModule(new CryptoModule().addEncryptionService(encryptionService));
        return objectMapper;
    }

    /**
     * Construct crypto service
     *
     * @param objectMapper  Object objectMapper to use
     * @param validator     Validator to use
     * @param cryptoContext Crypto to use
     * @throws EncryptionException Thrown on any error
     */
    public EncryptionService(final ObjectMapper objectMapper, final Validator validator, final ICryptoContext cryptoContext) throws EncryptionException {
        if (null == objectMapper)
            throw new EncryptionException("Object mapper cannot be null");
        if (null == validator)
            throw new EncryptionException("Validator cannot be null");
        if (null == cryptoContext)
            throw new EncryptionException("Crypto Context cannot be null");
        //
        this.mapper = objectMapper;
        this.validator = validator;
        this.cryptoContext = cryptoContext;
    }

    /**
     * Construct crypto service using a default validator
     *
     * @param objectMapper  Object mapper to use
     * @param cryptoContext Crypto to use
     * @throws EncryptionException Thrown on any error
     */
    public EncryptionService(final ObjectMapper objectMapper, final ICryptoContext cryptoContext) throws EncryptionException {
        this(objectMapper, Validation.buildDefaultValidatorFactory().getValidator(), cryptoContext);
    }

    /**
     * Encrypt a byte array as a JSON message
     *
     * @param data Byte array to encrypt
     * @return JSON message containing the encrypted byte array
     * @throws EncryptionException Thrown on any error
     */
    public EncryptedJson encrypt(final byte[] data) throws EncryptionException {
        var result = new EncryptedJson();
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
    public byte[] decrypt(final EncryptedJson value) {
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
            return null == deserializer ? mapper.readValue(decrypt(mapper.readValue(parser, EncryptedJson.class)), type) : deserializer.deserialize(mapper.getFactory()
                    .createParser(decrypt(mapper.readValue(parser, EncryptedJson.class))), context);
        } catch (Exception e) {
            throw new EncryptionException("Unable to decrypt document", e);
        }
    }

    /**
     * Run the recovered encrypted json through the supplied validator and log any errors
     *
     * @param encrypted Deserialized encrypted json
     * @throws EncryptionException Throws in any violation generated from the validator
     */
    private void validate(final EncryptedJson encrypted) throws EncryptionException {
        final Set<ConstraintViolation<EncryptedJson>> violations = validator.validate(encrypted);
        if (!violations.isEmpty()) {
            String message = "Encrypted JSON is invalid" + getErrors(violations);
            logger.error(message);
            throw new EncryptionException(message);
        }
    }

    /**
     * Build an error message list of all validation errors found
     *
     * @param violations Input from validator
     * @return Error message body
     */
    private String getErrors(final Set<ConstraintViolation<EncryptedJson>> violations) {
        var sb = new StringBuilder();
        violations.forEach(violation -> sb.append(" - ").append(violation.getPropertyPath()).append(" ").append(violation.getMessage()));
        return sb.toString();
    }

}
