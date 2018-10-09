package com.smaat.virtualtrainer.entity;

import java.io.Serializable;


public class UserDetailsEntityRes implements Serializable {

    private String svt_user_id;
    private String email_id;
    private String user_name;
    private String password;
    private String facebook_id;
    private String google_id;
    private String linkedin_id;
    private String login_type;
    private String status;
    private String user_account_type;
    private String user_type;
    private String device_platform;
    private String device_token;
    private String date_time;


    public String getUser_account_type() {
        return user_account_type;
    }

    public void setUser_account_type(String user_account_type) {
        this.user_account_type = user_account_type;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_platform() {
        return device_platform;
    }

    public void setDevice_platform(String device_platform) {
        this.device_platform = device_platform;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getLinkedin_id() {
        return linkedin_id;
    }

    public void setLinkedin_id(String linkedin_id) {
        this.linkedin_id = linkedin_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getSvt_user_id() {
        return svt_user_id;
    }

    public void setSvt_user_id(String svt_user_id) {
        this.svt_user_id = svt_user_id;
    }

}
