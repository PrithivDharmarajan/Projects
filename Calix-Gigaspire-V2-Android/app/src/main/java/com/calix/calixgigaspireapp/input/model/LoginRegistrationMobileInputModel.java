package com.calix.calixgigaspireapp.input.model;

import java.io.Serializable;


public class LoginRegistrationMobileInputModel implements Serializable {

    private String notificationToken;
    private String os;
    private String msisdn;
    private String locale;

    public String getNotificationToken() {
        return notificationToken == null ? "" : notificationToken;
    }

    public void setNotificationToken(String notificationToken) {
        this.notificationToken = notificationToken;
    }

    public String getOs() {
        return os == null ? "" : os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getMsisdn() {
        return msisdn == null ? "" : msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getLocale() {
        return locale == null ? "" : locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
