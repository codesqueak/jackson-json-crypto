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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * All fields marked as {@link Encrypt} are converted to this data structure in the JSON tree
 */
@JsonInclude(Include.NON_NULL)
public class EncryptedJson {

    @JsonProperty(value = "salt", required = true)
    @NotNull
    private byte[] salt;
    @JsonProperty(value = "iv", required = true)
    @NotNull
    private byte[] iv;
    @JsonProperty(value = "value", required = true)
    @NotNull
    private byte[] value;

    public EncryptedJson() {
    }

    public byte[] getSalt() {
        return null == this.salt ? null : Arrays.copyOf(this.salt, this.salt.length);
    }

    public void setSalt(final byte[] salt) {
        this.salt = salt != null ? Arrays.copyOf(salt, salt.length) : null;
    }

    public byte[] getIv() {
        return Arrays.copyOf(this.iv, this.iv.length);
    }

    public void setIv(final byte[] iv) {
        this.iv = null == iv ? null : Arrays.copyOf(iv, iv.length);
    }

    public byte[] getValue() {
        return null == this.value ? null : Arrays.copyOf(this.value, this.value.length);
    }

    public void setValue(final byte[] value) {
        this.value = null == value ? null : Arrays.copyOf(value, value.length);
    }
}
