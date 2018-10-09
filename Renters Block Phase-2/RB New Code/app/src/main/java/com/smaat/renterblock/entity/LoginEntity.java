package com.smaat.renterblock.entity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.smaat.renterblock.utils.AppConstants;

import java.util.ArrayList;

public class LoginEntity {

    private String user_id;
    private String business_name;
    private String first_name;
    private String last_name;
    private String user_name;
    private String user_pic;
    private String facebook_id;
    private String google_id;
    private String email_id;
    private String password;
    private String description;
    private String zip;
    private String phone;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String home_owner;
    private String broker;
    private String enhanced_profile;
    private String standard_profile;
    private String account_manager;
    private String login_type;
    private String user_type;
    private String status;
    private String licence;
    private String date;
    private String latitude;
    private String longitude;
    private String buyers;
    private String sellers;
    private String url;
    private String is_purchased;
    private String setting;
    private String rb_user;
    private String email_verified;
    private String online_user_time;
    private String last_purhase_date;
    private String isnewly_registered;
    private String filter_object;
    private String filter_type;
    private ArrayList<SavedSearchEntity> savedsearch;

    public ArrayList<SavedSearchEntity> getSavedsearch() {
        return savedsearch;
    }

    public void setSavedsearch(ArrayList<SavedSearchEntity> savedsearch) {
        this.savedsearch = savedsearch;
    }

    public String getType() {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        if (device_id == null || device_id.isEmpty()) {
            device_id = FirebaseInstanceId.getInstance().getToken();
        }
        this.device_id = device_id;
    }

    public String getDevice() {
        if (device == null || device.isEmpty()) {
            device = AppConstants.DEVICE;
        }
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    private String type;
    private String device_id;
    private String device;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHome_owner() {
        return home_owner;
    }

    public void setHome_owner(String home_owner) {
        this.home_owner = home_owner;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getEnhanced_profile() {
        return enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getStandard_profile() {
        return standard_profile;
    }

    public void setStandard_profile(String standard_profile) {
        this.standard_profile = standard_profile;
    }

    public String getAccount_manager() {
        return account_manager;
    }

    public void setAccount_manager(String account_manager) {
        this.account_manager = account_manager;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getBuyers() {
        return buyers;
    }

    public void setBuyers(String buyers) {
        this.buyers = buyers;
    }

    public String getSellers() {
        return sellers;
    }

    public void setSellers(String sellers) {
        this.sellers = sellers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIs_purchased() {
        return is_purchased;
    }

    public void setIs_purchased(String is_purchased) {
        this.is_purchased = is_purchased;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public String getRb_user() {
        return rb_user;
    }

    public void setRb_user(String rb_user) {
        this.rb_user = rb_user;
    }

    public String getEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(String email_verified) {
        this.email_verified = email_verified;
    }

    public String getOnline_user_time() {
        return online_user_time;
    }

    public void setOnline_user_time(String online_user_time) {
        this.online_user_time = online_user_time;
    }

    public String getLast_purhase_date() {
        return last_purhase_date;
    }

    public void setLast_purhase_date(String last_purhase_date) {
        this.last_purhase_date = last_purhase_date;
    }

    public String getIsnewly_registered() {
        return isnewly_registered;
    }

    public void setIsnewly_registered(String isnewly_registered) {
        this.isnewly_registered = isnewly_registered;
    }

    public String getFilter_object() {
        return filter_object;
    }

    public void setFilter_object(String filter_object) {
        this.filter_object = filter_object;
    }

    public String getFilter_type() {
        return filter_type;
    }

    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
    }

}
