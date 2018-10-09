package com.smaat.spark.entity.outputEntity;


import com.smaat.spark.utils.AppConstants;

import java.io.Serializable;

public class ChatConnectEntity implements Serializable {

    private String user_id;
    private String subject;
    private String status;
    private String username;
    private String connected_user_id;
    private String user_connect_anonymous;
    private String friend;
    private String anonymous;
    private String friend_connect_anonymous;
    private String friend_address;
    private String friend_interests;
    private String friend_location;
    private String friend_main_picture;

    public String getFriend_more_mages() {
        if(friend_more_mages==null){
            friend_more_mages="";
        }
        return friend_more_mages;
    }

    public void setFriend_more_mages(String friend_more_mages) {
        this.friend_more_mages = friend_more_mages;
    }

    public String getFriend_main_picture() {
        if(friend_main_picture==null){
            friend_main_picture="";
        }
        return friend_main_picture;
    }

    public void setFriend_main_picture(String friend_main_picture) {
        this.friend_main_picture = friend_main_picture;
    }

    public String getFriend_location() {
        if(friend_location==null){
            friend_location= AppConstants.FAILURE_CODE;
        }
        return friend_location;
    }

    public void setFriend_location(String friend_location) {
        this.friend_location = friend_location;
    }

    public String getFriend_interests() {
        if(friend_interests==null){
            friend_interests= "";
        }
        return friend_interests;
    }

    public void setFriend_interests(String friend_interests) {
        this.friend_interests = friend_interests;
    }

    public String getFriend_address() {
        if(friend_address==null){
            friend_address= "";
        }
        return friend_address;
    }

    public void setFriend_address(String friend_address) {
        this.friend_address = friend_address;
    }

    private String friend_more_mages;


    public String getUser_connect_anonymous() {
        if(user_connect_anonymous==null){
            user_connect_anonymous="";
        }
        return user_connect_anonymous;
    }

    public void setUser_connect_anonymous(String user_connect_anonymous) {
        this.user_connect_anonymous = user_connect_anonymous;
    }

    public String getFriend_connect_anonymous() {
        if(friend_connect_anonymous==null){
            friend_connect_anonymous="";
        }
        return friend_connect_anonymous;
    }

    public void setFriend_connect_anonymous(String friend_connect_anonymous) {
        this.friend_connect_anonymous = friend_connect_anonymous;
    }

    public String getAnonymous() {
        if(anonymous==null){
            anonymous="";
        }
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }


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
