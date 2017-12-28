package com.codingrodent.jackson.crypto.pojos;

import com.codingrodent.jackson.crypto.Encrypt;
import com.fasterxml.jackson.annotation.*;

public class InsecurePojo {

    private String critical;

    @JsonCreator
    public InsecurePojo(@JsonProperty("critical") String critical) {
        this.critical = critical;
    }

    public String getCritical() {
        return this.critical;
    }

//    public void setCritical(String critical) {
//        this.critical = critical;
//    }
}
