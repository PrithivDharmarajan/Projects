package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;


public class LeadsEntity implements Serializable {
    private String userId;
    private String datetime;
    private String count;
    private String propertyId;
    private String user_name;
    private String email_id;
    private String rb_user;
    private String enhanced_profile;
    private String block;
    private String user_profileImage;
    private String user_avg_rating;
    private String friends_count;
    private String reviews_count;
    private String photos_count;
    private String is_friend;
    private String isfavourite;

    public String getUserId() {
        return userId==null? AppConstants.FAILURE_CODE:userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDatetime() {
        return datetime==null? "":datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCount() {
        return count==null? AppConstants.FAILURE_CODE:count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPropertyId() {
        return propertyId==null? AppConstants.FAILURE_CODE:propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getUser_name() {
        return user_name==null?"":user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail_id() {
        return email_id==null?"":email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getRb_user() {
        return rb_user==null? AppConstants.FAILURE_CODE:rb_user;
    }

    public void setRb_user(String rb_user) {
        this.rb_user = rb_user;
    }

    public String getEnhanced_profile() {
        return enhanced_profile==null? AppConstants.FAILURE_CODE:enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getBlock() {
        return block==null? AppConstants.FAILURE_CODE:block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getUser_profileImage() {
        return user_profileImage==null? "":user_profileImage;
    }

    public void setUser_profileImage(String user_profileImage) {
        this.user_profileImage = user_profileImage;
    }

    public String getUser_avg_rating() {
        return user_avg_rating==null? AppConstants.FAILURE_CODE:user_avg_rating;
    }

    public void setUser_avg_rating(String user_avg_rating) {
        this.user_avg_rating = user_avg_rating;
    }

    public String getFriends_count() {
        return friends_count==null? AppConstants.FAILURE_CODE:friends_count;
    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getReviews_count() {
        return reviews_count==null? AppConstants.FAILURE_CODE:reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPhotos_count() {
        return photos_count==null? AppConstants.FAILURE_CODE:photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getIs_friend() {
        return is_friend==null? AppConstants.FAILURE_CODE:is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public String getIsfavourite() {
        return isfavourite==null? AppConstants.FAILURE_CODE:isfavourite;
    }

    public void setIsfavourite(String isfavourite) {
        this.isfavourite = isfavourite;
    }
}
