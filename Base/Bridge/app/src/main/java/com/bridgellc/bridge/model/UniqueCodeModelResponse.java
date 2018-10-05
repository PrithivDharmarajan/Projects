package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.PaypalPayEntityResponse;

import java.io.Serializable;

/**
 * Created by admin on 6/15/2016.
 */
public class UniqueCodeModelResponse implements Serializable {

    private String response_code;
    private String message;
    private PaypalPayEntityResponse result;

    public PaypalPayEntityResponse getResult() {
        return result;
    }

    public void setResult(PaypalPayEntityResponse result) {
        this.result = result;
    }

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
