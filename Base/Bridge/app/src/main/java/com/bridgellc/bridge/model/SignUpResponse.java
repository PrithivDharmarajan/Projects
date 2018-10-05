package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.SignInEntity;

public class SignUpResponse extends CommonModelResponse {

    private SignInEntity result;


    public SignInEntity getResult() {
        return result;
    }

    public void setResult(SignInEntity result) {
        this.result = result;
    }
}
