package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class ChatSendReceiveInputEntity implements Serializable {

    private String api_name;
    private String params;
    private String user_id;
    private String friend_id;
    private String subject;
    private String max_chat_id;
    private String message;
    private String status;
    private String request_id;

    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id, String subject, String max_chat_id, String message) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.subject = subject;
        this.max_chat_id = max_chat_id;
        this.message = message;
    }


    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id, String message, String subject) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.message = message;
        this.subject = subject;
    }

    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
    }
    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id,String status) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
    }
}
