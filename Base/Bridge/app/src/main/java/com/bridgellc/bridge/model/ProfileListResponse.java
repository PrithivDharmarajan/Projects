package com.bridgellc.bridge.model;


import com.bridgellc.bridge.entity.ProfileListEntity;

public class ProfileListResponse extends CommonModelResponse {

    private ProfileListEntity result;

    public ProfileListEntity getResult() {
        return result;
    }

    public void setResult(ProfileListEntity result) {
        this.result = result;
    }


}
