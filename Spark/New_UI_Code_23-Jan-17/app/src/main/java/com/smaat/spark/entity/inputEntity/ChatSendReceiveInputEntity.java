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
    private String message_flow;
    private String is_attachement;
    private String attachement_url;
    private String type;





    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id, String subject, String max_chat_id, String message) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.subject = subject;
        this.max_chat_id = max_chat_id;
        this.message = message;
    }


    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id, String message, String subject, String message_flow,String is_attachement,String attachement_url) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.message = message;
        this.subject = subject;
        this.message_flow=message_flow;
        this.is_attachement=is_attachement;
        this.attachement_url=attachement_url;

    }

    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
    }
    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id,String type,String temp) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.type = type;
    }
    public ChatSendReceiveInputEntity(String api_name, String params, String user_id, String friend_id,String status) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.status = status;
    }
}
