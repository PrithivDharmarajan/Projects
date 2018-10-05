package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.AcceptBiddingResponseEntity;

import java.io.Serializable;

/**
 * Created by admin on 4/27/2016.
 */
public class AcceptBiddingResponse implements Serializable {

    private String response_code;
    private String message;
    private AcceptBiddingResponseEntity result;

    public AcceptBiddingResponseEntity getResult() {
        return result;
    }

    public void setResult(AcceptBiddingResponseEntity result) {
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
