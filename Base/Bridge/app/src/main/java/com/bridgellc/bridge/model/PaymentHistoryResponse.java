package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.PaymentHistoryEntity;

import java.io.Serializable;

/**
 * Created by USER on 4/5/2016.
 */
public class PaymentHistoryResponse implements Serializable {

    private String response_code;
    private String message;
    private PaymentHistoryEntity result;


    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PaymentHistoryEntity getResult() {
        return result;
    }

    public void setResult(PaymentHistoryEntity result) {
        this.result = result;
    }


}
