package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;

public class PropertyPictures implements Serializable {


    private static final long serialVersionUID = 1L;
    private String picture_id;
    private String pro_id;
    private String file;
    private String property_id;
    private String user_id;
    private String file_type;
    private String description;
    private String file_order;
    private String datetime;
    private String first_name;
    private String user_name;
    private String user_pic;
    private String friends;
    private String Review;
    private String photo_video_id;
    private String Photos;

    public String getPhoto_video_id() {
        return photo_video_id == null ? AppConstants.FAILURE_CODE : photo_video_id;
    }

    public void setPhoto_video_id(String photo_video_id) {
        this.photo_video_id = photo_video_id;
    }

    public String getFirst_name() {
        return first_name == null ? "" : first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_name() {
        return user_name == null ? "" : user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic() {
        return user_pic == null ? "" : user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getFriends() {
        return friends == null ? AppConstants.FAILURE_CODE : friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getReview() {
        return Review == null ? AppConstants.FAILURE_CODE : Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getPhotos() {
        return Photos == null ? AppConstants.FAILURE_CODE : Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }


    public String getFile_order() {
        return file_order == null ? AppConstants.FAILURE_CODE : file_order;
    }

    public void setFile_order(String file_order) {
        this.file_order = file_order;
    }

    public String getDatetime() {
        return datetime == null ? "" : datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperty_id() {
        return property_id == null ? AppConstants.FAILURE_CODE : property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getUser_id() {
        return user_id == null ? AppConstants.FAILURE_CODE : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFile_type() {
        return file_type == null ? "" : file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getPicture_id() {
        return picture_id == null ? AppConstants.FAILURE_CODE : picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public String getPro_id() {
        return pro_id == null ? AppConstants.FAILURE_CODE : pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getFile() {
        return file == null ? "" : file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
