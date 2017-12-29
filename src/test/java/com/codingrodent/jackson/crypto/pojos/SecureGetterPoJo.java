package com.codingrodent.jackson.crypto.pojos;

import com.codingrodent.jackson.crypto.Encrypt;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SecureGetterPoJo {

    private String critical;

    @JsonProperty
    @Encrypt
    public String getCritical() {
        return this.critical;
    }

    public void setCritical(String critical) {
        this.critical = critical;
    }
}
