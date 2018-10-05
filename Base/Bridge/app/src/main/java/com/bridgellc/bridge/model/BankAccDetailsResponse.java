package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.BankAccCardEntityResponse;

/**
 * Created by USER on 4/13/2016.
 */
public class BankAccDetailsResponse extends CommonModelResponse {


    private BankAccCardEntityResponse result;

    public BankAccCardEntityResponse getResult() {
        return result;
    }

    public void setResult(BankAccCardEntityResponse result) {
        this.result = result;
    }



}
