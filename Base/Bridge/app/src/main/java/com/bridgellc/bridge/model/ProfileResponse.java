package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.ProfileEntity;


public class ProfileResponse extends CommonModelResponse {

    private ProfileEntity result;

    public ProfileEntity getResult() {
        return result;
    }

    public void setResult(ProfileEntity result) {
        this.result = result;
    }


}
