package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class AlexaRefreshTokenResponse implements Serializable {


    private String refresh_token="";

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }



}
