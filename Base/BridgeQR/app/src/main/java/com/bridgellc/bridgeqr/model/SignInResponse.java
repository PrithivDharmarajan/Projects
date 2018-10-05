package com.bridgellc.bridgeqr.model;


public class SignInResponse extends CommonResponse {

    private SignInEntity result;

    public SignInEntity getResult() {
        return result;
    }

    public void setResult(SignInEntity result) {
        this.result = result;
    }

}
