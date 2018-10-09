package com.smaat.virtualtrainer.model;

import com.smaat.virtualtrainer.entity.UserDetailsEntityRes;

import java.io.Serializable;
import java.util.ArrayList;



public class UserListEntity implements Serializable {

    private String response_code;
    private String message;
    private ArrayList<UserDetailsEntityRes> result;

    public ArrayList<UserDetailsEntityRes> getResult() {
        return result;
    }

    public void setResult(ArrayList<UserDetailsEntityRes> result) {
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
