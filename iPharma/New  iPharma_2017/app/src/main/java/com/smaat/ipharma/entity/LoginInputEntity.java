package com.smaat.ipharma.entity;

/**
 * Created by sys on 11/16/2016.
 */

public class LoginInputEntity {

    private String mode;
    private String api_name;
    private String parameter;
    private String email_id;
    private String password;
    private String login_mode;
    private String device_type;
    private String device_token;


    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getApi_name() {
        return api_name;
    }

    public void setApi_name(String api_name) {
        this.api_name = api_name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin_mode() {
        return login_mode;
    }

    public void setLogin_mode(String login_mode) {
        this.login_mode = login_mode;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }


}
