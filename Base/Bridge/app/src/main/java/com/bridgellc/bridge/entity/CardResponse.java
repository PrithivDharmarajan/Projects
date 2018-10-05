package com.bridgellc.bridge.entity;

import com.bridgellc.bridge.model.CardEntityResponse;

import java.io.Serializable;


public class CardResponse implements Serializable {

    private String response_code;
    private String message;
    private CardEntityResponse result;

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

    public CardEntityResponse getResult() {
        return result;
    }

    public void setResult(CardEntityResponse result) {
        this.result = result;
    }


}
