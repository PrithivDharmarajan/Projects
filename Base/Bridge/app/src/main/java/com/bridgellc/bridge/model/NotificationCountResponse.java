package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.PurchaseBuyerDetailsEntity;

import java.io.Serializable;

/**
 * Created by admin on 5/9/2016.
 */
public class NotificationCountResponse implements Serializable {

    private String response_code;
    private String message;
    private PurchaseBuyerDetailsEntity result;

    public PurchaseBuyerDetailsEntity getResult() {
        return result;
    }

    public void setResult(PurchaseBuyerDetailsEntity result) {
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
