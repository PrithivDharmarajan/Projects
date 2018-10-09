package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class PostPropertyUserDetails implements Serializable {

    private static final long serialVersionUID = 1L;
    private String user_id;
    private String first_name;
    private String last_name;
    private String user_pic;
    private String facebook_id;
    private String google_id;
    private String email_id;
    private String password;
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
    private String date;
    private String business_name;
    private String user_name;
    private String rb_user;
    private String is_request_already_sent;

    //Added in API

    public String getRb_user() {
        return rb_user == null || rb_user.isEmpty() ? AppConstants.FAILURE_CODE : rb_user;
    }

    public String getIs_request_already_sent() {
        if (is_request_already_sent == null) {
            is_request_already_sent = "0";
        }
        return is_request_already_sent;
    }

    public void setIs_request_already_sent(String is_request_already_sent) {
        this.is_request_already_sent = is_request_already_sent;
    }

    public void setRb_user(String rb_user) {
        this.rb_user = rb_user;
    }

    private String is_friend;


    public String getIs_friend() {
        if (is_friend == null) {
            is_friend = "";
        }
        return is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public String getUser_name() {
        if (user_name == null) {
            user_name = "";
        }
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBusiness_name() {
        return business_name == null ? "" : business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getUser_id() {
        if (user_id == null) {
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        if (first_name == null) {
            first_name = "";
        }
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        if (last_name == null) {
            last_name = "";
        }
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUser_pic() {
        if (user_pic == null) {
            user_pic = "";
        }
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getFacebook_id() {
        if (facebook_id == null) {
            facebook_id = "";
        }
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getGoogle_id() {
        if (google_id == null) {
            google_id = "";
        }
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getEmail_id() {
        if (email_id == null) {
            email_id = "";
        }
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        if (password == null) {
            password = "";
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZip() {
        if (zip == null) {
            zip = "";
        }
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        if (phone == null) {
            phone = "";
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress1() {
        if (address1 == null) {
            address1 = "";
        }
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        if (address2 == null) {
            address2 = "";
        }
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        if (city == null) {
            city = "";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        if (state == null) {
            state = "";
        }
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHome_owner() {
        if (home_owner == null) {
            home_owner = "";
        }
        return home_owner;
    }

    public void setHome_owner(String home_owner) {
        this.home_owner = home_owner;
    }

    public String getBroker() {
        if (broker == null) {
            broker = "";
        }
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }

    public String getEnhanced_profile() {
        if (enhanced_profile == null) {
            enhanced_profile = "";
        }
        return enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getStandard_profile() {
        if (standard_profile == null) {
            standard_profile = "";
        }
        return standard_profile;
    }

    public void setStandard_profile(String standard_profile) {
        this.standard_profile = standard_profile;
    }

    public String getAccount_manager() {
        if (account_manager == null) {
            account_manager = "";
        }
        return account_manager;
    }

    public void setAccount_manager(String account_manager) {
        this.account_manager = account_manager;
    }

    public String getLogin_type() {
        if (login_type == null) {
            login_type = "";
        }
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getUser_type() {
        if (user_type == null) {
            user_type = "";
        }
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getStatus() {
        if (status == null) {
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        if (date == null) {
            date = "";
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}
