package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class LoginRegResetInputEntity implements Serializable {

    private String api_name;
    private String params;
    private String email_id;
    private String password;
    private String username;
    private String lattitude;
    private String longitude;
    private String device_type;
    private String device_token;
    private String user_id;
    private String address;
    private String interests;
    private String gender;
    private String mainPicture;
    private String otherPictures;

    public LoginRegResetInputEntity(String api_name, String params, String email_id, String password, String username, String lattitude, String longitude, String address, String device_type, String device_token) {
        this.api_name = api_name;
        this.params = params;
        this.email_id = email_id;
        this.password = password;
        this.username = username;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.address = address;
        this.device_type = device_type;
        this.device_token = device_token;
    }

    public LoginRegResetInputEntity(String api_name, String params, String username, String password, String lattitude, String longitude, String address, String device_type, String device_token) {
        this.api_name = api_name;
        this.params = params;
        this.username = username;
        this.password = password;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.address = address;
        this.device_type = device_type;
        this.device_token = device_token;
    }

    public LoginRegResetInputEntity(String api_name, String params, String email_id) {
        this.api_name = api_name;
        this.params = params;
        this.email_id = email_id;
    }

    public LoginRegResetInputEntity(String api_name, String params, String user_id, String email_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.email_id = email_id;
    }

    public LoginRegResetInputEntity(String api_name, String params, String user_id, String username, String password) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.username = username;
        this.password = password;
    }

    public LoginRegResetInputEntity(String api_name, String params, String user_id, String email_id, String password, String username, String lattitude, String longitude,
                                    String address, String interests, String gender, String mainPicture, String otherPictures) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.email_id = email_id;
        this.password = password;
        this.username = username;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.address = address;
        this.interests = interests;
        this.gender = gender;
        this.mainPicture = mainPicture;
        this.otherPictures = otherPictures;
    }

}
