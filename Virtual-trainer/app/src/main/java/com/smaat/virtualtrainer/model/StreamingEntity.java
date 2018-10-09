package com.smaat.virtualtrainer.model;

import com.smaat.virtualtrainer.entity.StreamingEntityRes;

import java.io.Serializable;



public class StreamingEntity implements Serializable {

    private String response_code;
    private String message;
    private StreamingEntityRes result;

    public StreamingEntityRes getResult() {
        return result;
    }

    public void setResult(StreamingEntityRes result) {
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
