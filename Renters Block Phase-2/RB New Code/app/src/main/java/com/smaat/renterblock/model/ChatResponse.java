package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.ChatEntity;
import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;


public class ChatResponse implements Serializable {

    private String error_code;
    private String msg;
    private String group_name;
    private String group_user_name;
    private String group_chat_id;
    private ArrayList<ChatEntity> result;

    public String getGroup_chat_id() {
        return group_chat_id == null ? "" : group_chat_id;
    }

    public void setGroup_chat_id(String group_chat_id) {
        this.group_chat_id = group_chat_id;
    }

    public String getError_code() {
        return error_code == null ? AppConstants.FAILURE_CODE : error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getGroup_name() {
        return group_name == null ? "" : group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_user_name() {
        return group_user_name == null ? "" : group_user_name;
    }

    public void setGroup_user_name(String group_user_name) {
        this.group_user_name = group_user_name;
    }

    public void setResult(ArrayList<ChatEntity> result) {
        this.result = result;
    }

    public ArrayList<ChatEntity> getResult() {
        return result == null ? new ArrayList<ChatEntity>() : result;
    }

}
