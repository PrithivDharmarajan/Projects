package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class SettingsInputEntity implements Serializable {

    private String api_name;
    private String params;
    private String user_id;
    private String private_account;
    private String push_notifications;
    private String hide_location;
    private String anonymous;

    public SettingsInputEntity(String api_name, String params, String user_id, String private_account, String push_notifications, String hide_location, String anonymous) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.private_account = private_account;
        this.push_notifications = push_notifications;
        this.hide_location = hide_location;
        this.anonymous = anonymous;
    }


}
