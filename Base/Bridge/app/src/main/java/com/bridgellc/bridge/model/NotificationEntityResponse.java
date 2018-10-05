package com.bridgellc.bridge.model;

import java.io.Serializable;

/**
 * Created by admin on 4/30/2016.
 */
public class NotificationEntityResponse implements Serializable {

    private String response_code;
    private String message;
    private NotificationEntity result;

    public NotificationEntity getResult() {
        return result;
    }

    public void setResult(NotificationEntity result) {
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
