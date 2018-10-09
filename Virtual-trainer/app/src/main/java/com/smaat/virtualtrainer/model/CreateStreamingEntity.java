package com.smaat.virtualtrainer.model;

import com.smaat.virtualtrainer.entity.CreateStreamingEntityRes;

import java.io.Serializable;


public class CreateStreamingEntity implements Serializable {
    private String response_code;
    private String message;
    private CreateStreamingEntityRes result;

    public CreateStreamingEntityRes getResult() {
        return result;
    }

    public void setResult(CreateStreamingEntityRes result) {
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
