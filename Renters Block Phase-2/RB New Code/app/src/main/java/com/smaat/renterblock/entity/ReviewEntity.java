package com.smaat.renterblock.entity;

import com.smaat.renterblock.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 28-Aug-17.
 */

public class ReviewEntity implements Serializable {


    private String address;
    private String property_name;
    private String property_review_id;
    private String property_id;
    private String review_user_id;
    private String comments;
    private String rating;
    private String date_time;
    private String pro_img;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String review_count;
    private String description;
    private ArrayList<PropertyReviewCommentEntity> property_review_comment;

    public String getAddress() {

        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProperty_name() {
        return property_name == null ? "" : property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_review_id() {
        return property_review_id == null ? "" : property_review_id;
    }

    public void setProperty_review_id(String property_review_id) {
        this.property_review_id = property_review_id;
    }

    public String getProperty_id() {
        return property_id == null ? "" : property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getReview_user_id() {
        return review_user_id == null ? "" : review_user_id;
    }

    public void setReview_user_id(String review_user_id) {
        this.review_user_id = review_user_id;
    }

    public String getComments() {
        return comments == null ? "" : comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRating() {
        return rating == null ? AppConstants.FAILURE_CODE : rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate_time() {
        return date_time == null ? "" : date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPro_img() {
        return pro_img == null ? "" : pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getReview_count() {
        return review_count == null ? "" : review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public ArrayList<PropertyReviewCommentEntity> getProperty_review_comment() {
        return property_review_comment == null ? new ArrayList<PropertyReviewCommentEntity>() : property_review_comment;
    }

    public void setProperty_review_comment(ArrayList<PropertyReviewCommentEntity> property_review_comment) {
        this.property_review_comment = property_review_comment;
    }

}
