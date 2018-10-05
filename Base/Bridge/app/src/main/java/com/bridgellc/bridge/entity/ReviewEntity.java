package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by Karthi on 3/31/2016.
 */
public class ReviewEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String rating_id;
    private String user_id;
    private String rate_user_id;
    private String item_id;
    private String rating;
    private String comments;
    private String date;
    private String rate_user_first_name;

    public String getRate_user_last_name() {
        return rate_user_last_name;
    }

    public void setRate_user_last_name(String rate_user_last_name) {
        this.rate_user_last_name = rate_user_last_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRate_user_first_name() {
        return rate_user_first_name;
    }

    public void setRate_user_first_name(String rate_user_first_name) {
        this.rate_user_first_name = rate_user_first_name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getRate_user_id() {
        return rate_user_id;
    }

    public void setRate_user_id(String rate_user_id) {
        this.rate_user_id = rate_user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating_id() {
        return rating_id;
    }

    public void setRating_id(String rating_id) {
        this.rating_id = rating_id;
    }

    private String rate_user_last_name;
}
