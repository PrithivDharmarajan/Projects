package com.smaat.renterblock.entity;


import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class FindAgentDetailReviewResultEntity implements Serializable {
    private String user_id;
    private String user_profileImage;
    private String user_name;
    private String name;
    private String comments;
    private String licence;
    private String enhanced_profile;
    private String business_name;
    private String user_type;
    private String buyers;
    private String sellers;
    private String rating;
    private String user_avg_rating;
    private String friends_count;
    private String reviews_count;
    private String photos_count;
    private String is_friend;

    public String getUser_id() {
        if (user_id == null){
            user_id = AppConstants.FAILURE_CODE;
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_profileImage() {
        if (user_profileImage == null){
            user_profileImage = AppConstants.FAILURE_CODE;
        }
        return user_profileImage;
    }

    public void setUser_profileImage(String user_profileImage) {
        this.user_profileImage = user_profileImage;
    }

    public String getUser_name() {
        if (user_name == null){
            user_name = "";
        }
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getName() {
        if (name == null){
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        if (comments == null){
            comments = "";
        }
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getLicence() {
        if (licence == null){
            licence = "";
        }
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getEnhanced_profile() {
        if (enhanced_profile == null){
            enhanced_profile = "";
        }
        return enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getBusiness_name() {
        if (business_name == null){
            business_name = "";
        }
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getUser_type() {
        if (user_type == null){
            user_type = "";
        }
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getBuyers() {
        if (buyers == null){
            buyers = "";
        }
        return buyers;
    }

    public void setBuyers(String buyers) {
        this.buyers = buyers;
    }

    public String getSellers() {
        if (sellers == null){
            sellers = "";
        }
        return sellers;
    }

    public void setSellers(String sellers) {
        this.sellers = sellers;
    }

    public String getRating() {
        if (rating == null){
            rating = AppConstants.FAILURE_CODE;
        }
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUser_avg_rating() {
        if (user_avg_rating == null){
            user_avg_rating = AppConstants.FAILURE_CODE;
        }
        return user_avg_rating;
    }

    public void setUser_avg_rating(String user_avg_rating) {
        this.user_avg_rating = user_avg_rating;
    }

    public String getFriends_count() {
        if (friends_count == null){
            friends_count = AppConstants.FAILURE_CODE;
        }
        return friends_count;
    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getReviews_count() {
        if (reviews_count == null){
            reviews_count = AppConstants.FAILURE_CODE;
        }
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPhotos_count() {
        if (photos_count == null){
            photos_count = AppConstants.FAILURE_CODE;
        }
        return photos_count;
    }

    public void setPhotos_count(String photos_count) {
        this.photos_count = photos_count;
    }

    public String getIs_friend() {
        if (is_friend == null){
            is_friend = AppConstants.FAILURE_CODE;
        }
        return is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }



}