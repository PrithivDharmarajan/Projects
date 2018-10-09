package com.smaat.renterblock.entity;

import java.io.Serializable;

/**
 * Created by sys on 28-Aug-17.
 */

public class PropertyReviewCommentEntity implements Serializable {

    private String property_review_comment_id;
    private String property_id;
    private String review_user_id;
    private String review_comments;
    private String review_rating;
    private String review_date_time;
    private String review_header_txt;
    private String property_review_id;

    public String getProperty_review_id() {
        if (property_review_id == null) {
            property_review_id = "";
        }
        return property_review_id;
    }

    public void setProperty_review_id(String property_review_id) {
        this.property_review_id = property_review_id;
    }
    public String getReview_header_txt() {
        return review_header_txt == null ? "" : review_header_txt;
    }

    public void setReview_header_txt(String review_header_txt) {
        this.review_header_txt = review_header_txt;
    }

    public String getProperty_review_comment_id() {
        if (property_review_comment_id == null) {
            property_review_comment_id = "";
        }
        return property_review_comment_id;
    }

    public void setProperty_review_comment_id(String property_review_comment_id) {
        this.property_review_comment_id = property_review_comment_id;
    }

    public String getProperty_id() {
        if (property_id == null) {
            property_id = "";
        }
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getReview_user_id() {
        if (review_user_id == null) {
            review_user_id = "";
        }
        return review_user_id;
    }

    public void setReview_user_id(String review_user_id) {
        this.review_user_id = review_user_id;
    }

    public String getReview_comments() {
        if (review_comments == null) {
            review_comments = "";
        }
        return review_comments;
    }

    public void setReview_comments(String review_comments) {
        this.review_comments = review_comments;
    }

    public String getReview_rating() {
        if (review_rating == null) {
            review_rating = "";
        }
        return review_rating;
    }

    public void setReview_rating(String review_rating) {
        this.review_rating = review_rating;
    }

    public String getReview_date_time() {
        if (review_date_time == null) {
            review_date_time = "";
        }
        return review_date_time;
    }

    public void setReview_date_time(String review_date_time) {
        this.review_date_time = review_date_time;
    }

}
