package com.fautus.fautusapp.model;

import java.io.Serializable;


public class EmailValidationResponse implements Serializable {

    private String address;
    private boolean is_valid;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean is_valid() {
        return is_valid;
    }

    public void setIs_valid(boolean is_valid) {
        this.is_valid = is_valid;
    }


}
