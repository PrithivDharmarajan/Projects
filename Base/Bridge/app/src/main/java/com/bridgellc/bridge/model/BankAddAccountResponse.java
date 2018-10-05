package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.BankAddAccountEntity;

import java.io.Serializable;


public class BankAddAccountResponse implements Serializable {

    private String response_code;
    private String message;
    private BankAddAccountEntity result;

    public BankAddAccountEntity getResult() {
        return result;
    }

    public void setResult(BankAddAccountEntity result) {
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
