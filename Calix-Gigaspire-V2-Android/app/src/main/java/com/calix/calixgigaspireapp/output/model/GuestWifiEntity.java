package com.calix.calixgigaspireapp.output.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by user on 2/7/2018.
 */

public class GuestWifiEntity implements Serializable {

    private String eventId = "";
    private String eventName = "";
    private boolean isIndefinite = false;
    private DurationEntity duration = new DurationEntity();
    private String guestWifiName = "";
    private String guestWifiPassword = "";
    private String encryptionType = "";
    private String guestAvatarUrl = "";
    private int encryptType = 0;

    public String getGuestAvatarUrl() {
        return guestAvatarUrl;
    }

    public GuestWifiEntity setGuestAvatarUrl(String guestAvatarUrl) {
        this.guestAvatarUrl = guestAvatarUrl;
        return this;
    }

    public Bitmap getQrImage() {
        return qrImage;
    }

    public void setQrImage(Bitmap qrImage) {
        this.qrImage = qrImage;
    }

    private Bitmap qrImage;

    public String getEncryptionType() {
        return encryptionType == null ? "" : encryptionType;
    }

    public void setEncryptionType(String encryptionType) {
        this.encryptionType = encryptionType;
    }

    public int getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(int encryptType) {
        this.encryptType = encryptType;
    }

    public String getEventId() {
        return eventId == null ? "" : eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName == null ? "" : eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isIndefinite() {
        return isIndefinite;
    }

    public void setIndefinite(boolean indefinite) {
        isIndefinite = indefinite;
    }

    public DurationEntity getDuration() {
        return duration == null ? new DurationEntity() : duration;
    }

    public void setDuration(DurationEntity duration) {
        this.duration = duration;
    }

    public String getGuestWifiName() {
        return guestWifiName == null ? "" : guestWifiName;
    }

    public void setGuestWifiName(String guestWifiName) {
        this.guestWifiName = guestWifiName;
    }

    public String getGuestWifiPassword() {
        return guestWifiPassword == null ? "" : guestWifiPassword;
    }

    public void setGuestWifiPassword(String guestWifiPassword) {
        this.guestWifiPassword = guestWifiPassword;
    }


}
