package com.smaat.renterblock.entity;

import java.io.Serializable;

public class FriendDetailsEntity implements Serializable {


    private String user_friend_id;
    private String first_name;
    private String last_name;
    private String user_pic;
    private String status;
    private String isnew;
    private String friends;
    private String Review;
    private String Photos;
    private String rating;
    private String ispending;
    private String id;
    private String user_name;
    private String enhanced_profile;
    private String isRemove;
    private String accept;
    private boolean isSelect;

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getIsRemove() {
        return isRemove;
    }

    public void setIsRemove(String isRemove) {
        this.isRemove = isRemove;
    }

    public String getEnhanced_profile() {
        return enhanced_profile;
    }

    public void setEnhanced_profile(String enhanced_profile) {
        this.enhanced_profile = enhanced_profile;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIspending() {
        return ispending == null ? "" : ispending;
    }

    public void setIspending(String ispending) {
        this.ispending = ispending;
    }

    public String getUser_friend_id() {
        return user_friend_id;
    }

    public void setUser_friend_id(String user_friend_id) {
        this.user_friend_id = user_friend_id;
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

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsnew() {
        return isnew;
    }

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getPhotos() {
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
