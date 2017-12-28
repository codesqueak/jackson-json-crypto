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

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import java.util.Arrays;

@JsonInclude(Include.NON_NULL)
public class EncryptedJson {
    @JsonProperty("id")
    private byte[] id;
    @JsonProperty("salt")
    private byte[] salt;
    @JsonProperty("iv")
    private byte[] iv;
    @JsonProperty("value")
    private byte[] value;

    public EncryptedJson() {
    }

    public byte[] getId() {
        return null == this.id ? null : Arrays.copyOf(this.id, this.id.length);
    }

    public void setId(final byte[] id) {
        this.id = Arrays.copyOf(id, id.length);
    }

    public byte[] getSalt() {
        return null == this.salt ? null : Arrays.copyOf(this.salt, this.salt.length);
    }

    public void setSalt(final byte[] salt) {
        this.salt = Arrays.copyOf(salt, salt.length);
    }

    public byte[] getIv() {
        return Arrays.copyOf(this.iv, this.iv.length);
    }

    public void setIv(final byte[] iv) {
        this.iv = Arrays.copyOf(iv, iv.length);
    }

    public byte[] getValue() {
        return null == this.value ? null : Arrays.copyOf(this.value, this.value.length);
    }

    public void setValue(final byte[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }
}
