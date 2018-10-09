package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.UserDetailsEntity;

import java.io.Serializable;

/**
 * Created by user on 8/18/2017.
 */

public class LoginResponse implements Serializable {
    private String error_code;
    private String msg;
    private UserDetailsEntity result;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public UserDetailsEntity getResult() {
        return result;
    }

    public void setResult(UserDetailsEntity result) {
        this.result = result;
    }
}