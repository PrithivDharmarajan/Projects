package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by sibaprasad on 2/7/2018.
 */

public class EncryptionTypeEntity implements Serializable {


    private String description = "";
    private int value;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


}
