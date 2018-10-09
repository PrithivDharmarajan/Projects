package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class NotificationInputEntity implements Serializable {
    private String api_name;
    private String params;
    private String user_id;
    private String notification_id;
    private String friend_id;
    private String chat_id;
    private String username;

    public NotificationInputEntity(String api_name, String params, String user_id, String notification_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.notification_id = notification_id;
    }
    public NotificationInputEntity(String api_name, String params,String username) {
        this.api_name = api_name;
        this.params = params;
        this.username = username;
    }

    public NotificationInputEntity(String api_name, String params, String user_id, String friend_id, String chat_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.friend_id = friend_id;
        this.chat_id = chat_id;
    }
}
