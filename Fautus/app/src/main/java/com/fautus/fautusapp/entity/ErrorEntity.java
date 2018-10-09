package com.fautus.fautusapp.entity;


import java.io.Serializable;

public class ErrorEntity implements Serializable {
    private String type;
    private String message;
    private String param;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

}
