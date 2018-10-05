package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class UserDetailsEntity implements Serializable {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String hash;
    private String phoneNumber;
    private boolean verified;
    private int created;
    private int updated;
    private String mobileDevices;
    private String avatarURL;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getHash() {
        return hash == null ? "" : hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPhoneNumber() {
        return phoneNumber == null ? "" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getCreated() {
        return String.valueOf(created);
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public String getUpdated() {
        return String.valueOf(updated);
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public String getMobileDevices() {
        return mobileDevices == null ? "" : mobileDevices;
    }

    public void setMobileDevices(String mobileDevices) {
        this.mobileDevices = mobileDevices;
    }

    public String getAvatarURL() {
        return avatarURL == null ? "" : avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

}
