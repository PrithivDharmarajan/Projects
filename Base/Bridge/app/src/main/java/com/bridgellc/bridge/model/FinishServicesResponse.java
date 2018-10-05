package com.bridgellc.bridge.model;

import java.io.Serializable;

/**
 * Created by USER on 4/18/2016.
 */
public class FinishServicesResponse implements Serializable {

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
