package com.smaat.renterblock.model;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;


public class CreateGroupChatResponse implements Serializable {

    private String error_code;
    private String msg;
    private String result;
    private String username;
    private String enhanced_profile;
    private String ownner_id;
    private String friend_id;

    public String getOwnner_id() {
        return ownner_id==null?"":ownner_id;
    }

    public void setOwnner_id(String ownner_id) {
        this.ownner_id = ownner_id;
    }

    public String getFriend_id() {
        return friend_id==null?"":friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }



    public String getError_code() {
        return error_code==null? AppConstants.FAILURE_CODE:error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg==null?"":msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResult() {
        return result==null?"":result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUsername() {
        return username==null?"":username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnhanced_profile() {
        return enhanced_profile==null?"":enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

}
