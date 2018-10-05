package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.PayForItemEntity;

import java.io.Serializable;

/**
 * Created by USER on 3/24/2016.
 */

public class PayForItemResponse implements Serializable {

    private String response_code;
    private String message;

    private PayForItemEntity result;


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

    public PayForItemEntity getResult() {
        return result;
    }

    public void setResult(PayForItemEntity result) {
        this.result = result;
    }
}

