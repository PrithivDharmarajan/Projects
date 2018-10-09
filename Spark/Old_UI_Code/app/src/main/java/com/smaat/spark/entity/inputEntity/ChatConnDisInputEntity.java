package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class ChatConnDisInputEntity implements Serializable {

    private String api_name;
    private String params;
    private String user_id;
    private String search_with_distance;
    private String subject;
    private String distance;
    private String lat;
    private String lon;
    private String random;
    private String anonymous;

    public ChatConnDisInputEntity(String api_name, String params, String user_id) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
    }

    public ChatConnDisInputEntity(String api_name, String params, String user_id, String anonymous) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.anonymous = anonymous;
    }

    public ChatConnDisInputEntity(String api_name, String params, String user_id, String search_with_distance, String subject, String distance, String lat, String lon, String random, String anonymous) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.search_with_distance = search_with_distance;
        this.subject = subject;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
        this.random = random;
        this.anonymous = anonymous;
    }


}
