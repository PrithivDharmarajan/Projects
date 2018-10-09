package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 19-Sep-17.
 */

public class BoardsEntity implements Serializable {
    private String board_id;
    private String enhanced_profile;
    private String user_id;
    private String property_id;
    private String date_time;
    private String review_count;
    private String property_rating;
    private ArrayList<PropertyEntity> property_details;
    private ArrayList<PropertyPictures> property_pics;

    public String getBoard_id() {
        return board_id == null ? AppConstants.FAILURE_CODE : board_id;
    }

    public void setBoard_id(String favorite_id) {
        this.board_id = favorite_id;
    }

    public String getUser_id() {
        return user_id == null ? AppConstants.FAILURE_CODE : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getProperty_id() {
        return property_id == null ? AppConstants.FAILURE_CODE : property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getDate_time() {
        return date_time == null ? "" : date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getReview_count() {
        return review_count == null ? AppConstants.FAILURE_CODE : review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getProperty_rating() {
        return property_rating == null ? AppConstants.FAILURE_CODE : property_rating;
    }

    public void setProperty_rating(String property_rating) {
        this.property_rating = property_rating;
    }

    public String getEnhanced_profile() {
        return enhanced_profile == null ? AppConstants.FAILURE_CODE : enhanced_profile;
    }

    public void setEnhanced_profile(String isfavourite) {
        this.enhanced_profile = isfavourite;
    }


    public ArrayList<PropertyEntity> getProperty_details() {
        return property_details == null ? new ArrayList<PropertyEntity>() : property_details;
    }

    public void setProperty_details(ArrayList<PropertyEntity> property_details) {
        this.property_details = property_details;
    }

    public ArrayList<PropertyPictures> getProperty_pics() {
        return property_pics == null ? new ArrayList<PropertyPictures>() : property_pics;
    }

    public void setProperty_pics(ArrayList<PropertyPictures> property_pics) {
        this.property_pics = property_pics;
    }

}
