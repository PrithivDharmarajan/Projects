package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.SaveStripCardEntityResponse;

import java.io.Serializable;

/**
 * Created by admin on 8/16/2016.
 */

public class SaveStripCardResponse implements Serializable {

    private String response_code;
    private String message;
    private SaveStripCardEntityResponse result;

    public SaveStripCardEntityResponse getResult() {
        return result;
    }

    public void setResult(SaveStripCardEntityResponse result) {
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
