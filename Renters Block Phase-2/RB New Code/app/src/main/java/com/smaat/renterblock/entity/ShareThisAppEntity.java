package com.smaat.renterblock.entity;

import java.io.Serializable;

public class ShareThisAppEntity implements Serializable {

    private String app_content;
    private String app_link;
    private String property_id;
    private String property_address;
    private String sms_share;
    private String email_share_subject;
    private String email_share_text;
    private String facebook_share_title;
    private String facebook_share_description;
    private String facebook_share_link;
    private String share_property_id;

    public String getShare_property_id() {
        return share_property_id;
    }

    public void setShare_property_id(String share_property_id) {
        this.share_property_id = share_property_id;
    }





    public String getEmail_share_subject() {
        return email_share_subject;
    }

    public void setEmail_share_subject(String email_share_subject) {
        this.email_share_subject = email_share_subject;
    }

    public String getEmail_share_text() {
        return email_share_text;
    }

    public void setEmail_share_text(String email_share_text) {
        this.email_share_text = email_share_text;
    }

    public String getFacebook_share_link() {
        return facebook_share_link;
    }

    public void setFacebook_share_link(String facebook_share_link) {
        this.facebook_share_link = facebook_share_link;
    }

    public String getApp_content() {
        return app_content;
    }

    public void setApp_content(String app_content) {
        this.app_content = app_content;
    }

    public String getApp_link() {
        return app_link;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getProperty_address() {
        return property_address;
    }

    public void setProperty_address(String property_address) {
        this.property_address = property_address;
    }

    public String getSms_share() {
        return sms_share;
    }

    public void setSms_share(String sms_share) {
        this.sms_share = sms_share;
    }

    public String getFacebook_share_title() {
        return facebook_share_title;
    }

    public void setFacebook_share_title(String facebook_share_title) {
        this.facebook_share_title = facebook_share_title;
    }

    public String getFacebook_share_description() {
        return facebook_share_description;
    }

    public void setFacebook_share_description(String facebook_share_description) {
        this.facebook_share_description = facebook_share_description;
    }
}
