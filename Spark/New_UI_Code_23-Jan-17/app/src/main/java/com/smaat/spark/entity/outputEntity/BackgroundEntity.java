package com.smaat.spark.entity.outputEntity;

import com.smaat.spark.utils.AppConstants;

import java.io.Serializable;


public class BackgroundEntity implements Serializable {

    private String notifications_count;
    private String blocks_count;
    private String unread_notifications_count;
    private String email_id;
    private String online_friend_count;
    private String unread_messages;
    private String connect_unread_messages;
    private String connect_friend_id;
    private String connected_status;

    private String subject;
    private String user_anonymous;
    private String friend_anonymous;

    public String getFriend_anonymous() {
        if(friend_anonymous==null){
            friend_anonymous=AppConstants.FAILURE_CODE;
        }
        return friend_anonymous;
    }

    public void setFriend_anonymous(String friend_anonymous) {
        this.friend_anonymous = friend_anonymous;
    }

    public String getUser_anonymous() {
        if(user_anonymous==null){
            user_anonymous=AppConstants.FAILURE_CODE;
        }
        return user_anonymous;
    }

    public void setUser_anonymous(String user_anonymous) {
        this.user_anonymous = user_anonymous;
    }

    public String getSubject() {
        if(subject==null){
            subject="";
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }



    public String getConnect_unread_messages() {
        if (connect_unread_messages == null) {
            connect_unread_messages = AppConstants.FAILURE_CODE;
        }
        return connect_unread_messages;
    }

    public void setConnect_unread_messages(String connect_unread_messages) {
        this.connect_unread_messages = connect_unread_messages;
    }

    public String getConnect_friend_id() {
        if (connect_friend_id == null) {
            connect_friend_id = AppConstants.FAILURE_CODE;
        }
        return connect_friend_id;
    }

    public void setConnect_friend_id(String connect_friend_id) {
        this.connect_friend_id = connect_friend_id;
    }

    public String getConnected_status() {
        if (connected_status == null) {
            connected_status = AppConstants.FAILURE_CODE;
        }
        return connected_status;
    }

    public void setConnected_status(String connected_status) {
        this.connected_status = connected_status;
    }

    public String getUnread_messages() {
        if (unread_messages == null) {
            unread_messages = AppConstants.FAILURE_CODE;
        }
        return unread_messages;
    }

    public void setUnread_messages(String unread_messages) {
        this.unread_messages = unread_messages;
    }


    public String getOnline_friend_count() {
        if (online_friend_count == null) {
            online_friend_count = AppConstants.FAILURE_CODE;
        }
        return online_friend_count;
    }

    public void setOnline_friend_count(String online_friend_count) {
        this.online_friend_count = online_friend_count;
    }


    public String getUnread_notifications_count() {
        if (unread_notifications_count == null) {
            unread_notifications_count = AppConstants.FAILURE_CODE;
        }
        return unread_notifications_count;
    }

    public void setUnread_notifications_count(String unread_notifications_count) {
        this.unread_notifications_count = unread_notifications_count;
    }

    public String getBlocks_count() {
        if (blocks_count == null) {
            blocks_count = AppConstants.FAILURE_CODE;
        }
        return blocks_count;
    }

    public void setBlocks_count(String blocks_count) {

        this.blocks_count = blocks_count;
    }

    public String getNotifications_count() {
        if (notifications_count == null) {
            notifications_count = AppConstants.FAILURE_CODE;
        }
        return notifications_count;
    }

    public void setNotifications_count(String notifications_count) {
        this.notifications_count = notifications_count;
    }

    public String getEmail_id() {
        if (email_id == null) {
            email_id = "";
        }
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }
}
