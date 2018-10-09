package com.smaat.renterblock.entity;


import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class NotificationEntity implements Serializable{

    public String notification_id;
    public String user_id;
    public String type_of_notification;
    public String type_id;
    public String message;
    public String status;

    public String getNotification_id() {
        if (notification_id == null){
            notification_id = AppConstants.FAILURE_CODE;
        }
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getUser_id() {
        if (user_id == null){
            user_id = AppConstants.FAILURE_CODE;
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType_of_notification() {
        if (type_of_notification == null){
            type_of_notification = "";
        }
        return type_of_notification;
    }

    public void setType_of_notification(String type_of_notification) {
        this.type_of_notification = type_of_notification;
    }

    public String getType_id() {
        if (type_id == null){
            type_id = AppConstants.FAILURE_CODE;
        }
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getMessage() {
        if (message == null){
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        if (status == null){
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
