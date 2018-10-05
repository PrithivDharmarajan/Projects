package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.UpdatePhNumEntity;

import java.io.Serializable;

/**
 * Created by admin on 5/5/2016.
 */
public class ItemEditableResponse implements Serializable {


    private String response_code;
    private String message;
    private UpdatePhNumEntity result;

    public UpdatePhNumEntity getResult() {
        return result;
    }

    public void setResult(UpdatePhNumEntity result) {
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
