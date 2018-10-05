package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.NegotiateResponseEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class NegotiateResponse implements Serializable {

    private String response_code;
    private String message;
    private ArrayList<NegotiateResponseEntity> result;

    public ArrayList<NegotiateResponseEntity> getResult() {
        return result;
    }

    public void setResult(ArrayList<NegotiateResponseEntity> result) {
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
