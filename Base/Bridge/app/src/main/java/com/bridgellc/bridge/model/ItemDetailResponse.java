package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.HomeSingleItemEntity;

import java.io.Serializable;

/**
 * Created by Dell on 4/20/2016.
 */
public class ItemDetailResponse implements Serializable {

    private String response_code;
    private String message;
    private HomeSingleItemEntity result;

    public HomeSingleItemEntity getResult() {
        return result;
    }

    public void setResult(HomeSingleItemEntity result) {
        this.result = result;
    }

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

}
