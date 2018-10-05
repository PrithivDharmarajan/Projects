package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.BuyItemEntityResponse;

import java.io.Serializable;

/**
 * Created by admin on 6/3/2016.
 */
public class BuyItemResponse implements Serializable {


    private String response_code;
    private String message;
    private BuyItemEntityResponse result;

    public BuyItemEntityResponse getResult() {
        return result;
    }

    public void setResult(BuyItemEntityResponse result) {
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
