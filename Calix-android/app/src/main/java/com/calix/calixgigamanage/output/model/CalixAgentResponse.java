package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class CalixAgentResponse implements Serializable {

    private  String appName="";
    private  String token="";
    private  String clientId="";

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }




}
