package com.smaat.spark.entity.outputEntity;


import java.io.Serializable;

public class ChatConnectEntity implements Serializable {

    private String user_id;
    private String subject;
    private String status;
    private String username;
    private String connected_user_id;

    private String friend;

    public String getFriend() {
        if (friend == null) {
            friend = "";
        }
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getConnected_user_id() {
        if (connected_user_id == null) {
            connected_user_id = "";
        }
        return connected_user_id;
    }

    public void setConnected_user_id(String connected_user_id) {
        this.connected_user_id = connected_user_id;
    }

    public String getUsername() {
        if (username == null) {
            username = "";
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        if (status == null) {
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        if (subject == null) {
            subject = "";
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser_id() {
        if (user_id == null) {
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
