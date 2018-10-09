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
