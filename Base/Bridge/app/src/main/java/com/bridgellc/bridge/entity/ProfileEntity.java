package com.bridgellc.bridge.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileEntity implements Serializable {



    private String user_id;
    private String first_name;
    private String last_name;
    private String email_id;
    private String user_rating;
    private String total_listings;
    private String total_count;
    private String sell_count;
    private String buy_count;
    private String request_count;
    private String bid_count;
    private String university_name;
    private String paymnet_count;
    private String phone_number;
    private String password;
    private String university_id;
    private String dob;
    private String gender;
    private String login_type;
    private String social_id;
    private String payment_mode;
    private String last_login_datetime;
    private String created_datetime;
    private String modified_datetime;
    private String delete_flag;
    private String edu_email_verify;
    private String mobile_verify;
    private String partner;
    private String user_verified;
    private String owner_verified;
    private String seller_partner;
    private ArrayList<HomeSingleItemEntity> user_listing;

    public String getSeller_partner() {
        return seller_partner;
    }

    public void setSeller_partner(String seller_partner) {
        this.seller_partner = seller_partner;
    }

    public String getOwner_verified() {
        return owner_verified;
    }

    public void setOwner_verified(String owner_verified) {
        this.owner_verified = owner_verified;
    }

    public String getUser_verified() {
        return user_verified;
    }

    public void setUser_verified(String user_verified) {
        this.user_verified = user_verified;
    }



    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getMobile_verify() {
        return mobile_verify;
    }

    public void setMobile_verify(String mobile_verify) {
        this.mobile_verify = mobile_verify;
    }

    public String getEdu_email_verify() {
        return edu_email_verify;
    }

    public void setEdu_email_verify(String edu_email_verify) {
        this.edu_email_verify = edu_email_verify;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getLast_login_datetime() {
        return last_login_datetime;
    }

    public void setLast_login_datetime(String last_login_datetime) {
        this.last_login_datetime = last_login_datetime;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }


    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }


    public ArrayList<HomeSingleItemEntity> getUser_listing() {
        return user_listing;
    }

    public void setUser_listing(ArrayList<HomeSingleItemEntity> user_listing) {
        this.user_listing = user_listing;
    }

    public String getPaymnet_count() {
        return paymnet_count;
    }

    public void setPaymnet_count(String paymnet_count) {
        this.paymnet_count = paymnet_count;
    }

    public String getBid_count() {
        return bid_count;
    }

    public void setBid_count(String bid_count) {
        this.bid_count = bid_count;
    }

    public String getRequest_count() {
        return request_count;
    }

    public void setRequest_count(String request_count) {
        this.request_count = request_count;
    }

    public String getBuy_count() {
        return buy_count;
    }

    public void setBuy_count(String buy_count) {
        this.buy_count = buy_count;
    }

    public String getSell_count() {
        return sell_count;
    }

    public void setSell_count(String sell_count) {
        this.sell_count = sell_count;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public String getTotal_listings() {
        return total_listings;
    }

    public void setTotal_listings(String total_listings) {
        this.total_listings = total_listings;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
