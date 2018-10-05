package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by sys on 3/25/2016.
 */
public class ChatResponseEntity implements Serializable{


    private String sender_id;
    private String sender_first_name;
    private String sender_last_name;
    private String chat_message;
    private String chat_date_time;
    private String chat_read_status;

    private String Headerakey;
    private int headId;

    public int getHeadId() {
        return headId;
    }

    public void setHeadId(int headId) {
        this.headId = headId;
    }

    public String getHeaderakey() {
        return Headerakey;
    }

    public void setHeaderakey(String headerakey) {
        Headerakey = headerakey;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getSender_first_name() {
        return sender_first_name;
    }

    public void setSender_first_name(String sender_first_name) {
        this.sender_first_name = sender_first_name;
    }

    public String getSender_last_name() {
        return sender_last_name;
    }

    public void setSender_last_name(String sender_last_name) {
        this.sender_last_name = sender_last_name;
    }

    public String getChat_message() {
        return chat_message;
    }

    public void setChat_message(String chat_message) {
        this.chat_message = chat_message;
    }

    public String getChat_date_time() {
        return chat_date_time;
    }

    public void setChat_date_time(String chat_date_time) {
        this.chat_date_time = chat_date_time;
    }

    public String getChat_read_status() {
        return chat_read_status;
    }

    public void setChat_read_status(String chat_read_status) {
        this.chat_read_status = chat_read_status;
    }
}
