package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by admin on 4/27/2016.
 */
public class AcceptBiddingResponseEntity implements Serializable {

    private String message;
    private String unique_code;

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
