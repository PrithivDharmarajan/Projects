package com.smaat.ipharma.entity;




import java.io.Serializable;
import java.util.ArrayList;


public class SignInEntity implements Serializable {

    private String response_code;
    private String message;
    private String mode;
    /*private UserDetailsEntityRes result;

    public UserDetailsEntityRes getResult() {
        return result;
    }

    public void setResult(UserDetailsEntityRes result) {
        this.result = result;
    }*/






    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
