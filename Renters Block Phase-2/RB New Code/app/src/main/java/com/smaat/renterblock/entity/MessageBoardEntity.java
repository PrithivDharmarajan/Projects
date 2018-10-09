package com.smaat.renterblock.entity;

/**
 * Created by sys on 10/6/2017.
 */

public class MessageBoardEntity {


    private String post_id;
    private String user_id;
    private String property_id;
    private String message;
    private String date_time;
    private String property_name;
    private String address;
    private String property_type;
    private String open_house;
    private String rb_block;
    private String user_name;
    private String username;
    private String enhanced_profile;
    private String user_profileImage;
    private String user_avg_rating;
    private String friends_count;
    private String reviews_count;
    private String photos_count;

    public String getPost_id() {
        return post_id == null ? post_id = "" : post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id == null ? user_id = "" : user_id;

    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProperty_id() {
        return property_id == null ? property_id = "" : property_id;

    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getMessage() {
        return message == null ? message = "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_time() {
        return date_time == null ? date_time = "" : date_time;

    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getProperty_name() {
        return property_name == null ? property_name = "" : property_name;

    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getAddress() {
        return address == null ? address = "" : address;

    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProperty_type() {
        return property_type == null ? property_type = "" : property_type;

    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getOpen_house() {
        return open_house == null ? open_house = "" : open_house;

    }

    public void setOpen_house(String open_house) {
        this.open_house = open_house;
    }

    public String getRb_block() {
        return rb_block == null ? rb_block = "" : rb_block;

    }

    public void setRb_block(String rb_block) {
        this.rb_block = rb_block;
    }

    public String getUser_name() {
        return user_name == null ? user_name = "" : user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEnhanced_profile() {
        return enhanced_profile == null ? enhanced_profile = "" : enhanced_profile;

    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getUser_profileImage() {
        return user_profileImage == null ? user_profileImage = "" : user_profileImage;

    }

    public void setUser_profileImage(String user_profileImage) {
        this.user_profileImage = user_profileImage;
    }

    public String getUser_avg_rating() {
        return user_avg_rating == null ? user_avg_rating = "" : user_avg_rating;
    }


    public void setUser_avg_rating(String user_avg_rating) {
        this.user_avg_rating = user_avg_rating;
    }

    public String getFriends_count() {
        return friends_count == null ? friends_count = "" : friends_count;

    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getReviews_count() {
        return reviews_count == null ? reviews_count = "" : reviews_count;

    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPhotos_count() {
        return photos_count == null ? photos_count = "" : photos_count;

    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getIs_friend() {
        return is_friend == null ? is_friend = "" : is_friend;

    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    private String is_friend;
}
