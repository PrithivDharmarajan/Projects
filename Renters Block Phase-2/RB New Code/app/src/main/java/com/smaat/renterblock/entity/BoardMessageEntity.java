package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;


public class BoardMessageEntity implements Serializable {
    private String post_id;
    private String user_id;
    private String property_id;
    private String message;
    private String date_time;
    private String user_name;
    private String username;
    private String enhanced_profile;
    private String user_profileImage;
    private String user_avg_rating;
    private String friends_count;
    private String reviews_count;
    private String photos_count;
    private String is_friend;


    public String getPost_id() {
        return post_id==null? AppConstants.FAILURE_CODE:post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id==null?AppConstants.FAILURE_CODE:user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProperty_id() {
        return property_id==null?AppConstants.FAILURE_CODE:property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getMessage() {
        return message==null?"":message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time==null?AppConstants.FAILURE_CODE:date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUser_name() {
        return user_name==null?"":user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsername() {
        return username==null?"":username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnhanced_profile() {
        return enhanced_profile==null?AppConstants.FAILURE_CODE:enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getUser_profileImage() {
        return user_profileImage==null?"":user_profileImage;
    }

    public void setUser_profileImage(String user_profileImage) {
        this.user_profileImage = user_profileImage;
    }

    public String getUser_avg_rating() {
        return user_avg_rating==null?AppConstants.FAILURE_CODE:user_avg_rating;
    }

    public void setUser_avg_rating(String user_avg_rating) {
        this.user_avg_rating = user_avg_rating;
    }

    public String getFriends_count() {
        return friends_count==null?AppConstants.FAILURE_CODE:friends_count;
    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getReviews_count() {
        return reviews_count==null?AppConstants.FAILURE_CODE:reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPhotos_count() {
        return photos_count==null?AppConstants.FAILURE_CODE:photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getIs_friend() {
        return is_friend==null?AppConstants.FAILURE_CODE:is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }
}
