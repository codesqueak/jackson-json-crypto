package com.codingrodent.jackson.crypto.pojos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InsecurePojo {

    private final String critical;

    @JsonCreator
    public InsecurePojo(@JsonProperty("critical") String critical) {
        this.critical = critical;
    }

    public String getCritical() {
        return this.critical;
    }

}
