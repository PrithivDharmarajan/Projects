package com.smaat.spark.model;


import com.smaat.spark.entity.outputEntity.UserDetailsEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDetailsResponse implements Serializable {
    private String api_name;
    private String response_code;
    private String message;
    private ArrayList<UserDetailsEntity> result;

    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse_code() {
        if (response_code == null) {
            response_code = "";
        }
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getApi_name() {
        if (api_name == null) {
            api_name = "";
        }
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public ArrayList<UserDetailsEntity> getResult() {
        if (result == null) {
            result = new ArrayList<>();
        }
        return result;
    }

    public void setResult(ArrayList<UserDetailsEntity> result) {
        this.result = result;
    }

}
