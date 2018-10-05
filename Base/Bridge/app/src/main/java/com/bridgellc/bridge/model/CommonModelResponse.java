package com.bridgellc.bridge.model;

import java.io.Serializable;

/**
 * Created by USER on 3/24/2016.
 */

public class CommonModelResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private String response_code;
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }



}

