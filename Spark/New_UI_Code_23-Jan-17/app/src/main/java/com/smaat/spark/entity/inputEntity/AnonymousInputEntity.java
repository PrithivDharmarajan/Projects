package com.smaat.spark.entity.inputEntity;

import java.io.Serializable;


public class AnonymousInputEntity implements Serializable {
    private String api_name;
    private String params;
    private String user_id;
    private String anonymous;

    public AnonymousInputEntity(String api_name, String params, String user_id, String anonymous) {
        this.api_name = api_name;
        this.params = params;
        this.user_id = user_id;
        this.anonymous = anonymous;
    }

}
