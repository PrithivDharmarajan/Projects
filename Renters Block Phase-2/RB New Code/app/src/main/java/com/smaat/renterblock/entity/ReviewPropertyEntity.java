package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 28-Aug-17.
 */

public class ReviewPropertyEntity implements Serializable {

    private ArrayList<ReviewEntity> review;
    private UserDetailsEntity user_details;

    public ArrayList<ReviewEntity> getReview() {
        return review == null ? review = new ArrayList<ReviewEntity>() : review;
    }

    public void setReview(ArrayList<ReviewEntity> review) {
        this.review = review;
    }

    public UserDetailsEntity getUser_details() {
        return user_details == null ? user_details = new UserDetailsEntity() : user_details;
    }

    public void setUser_details(UserDetailsEntity user_details) {
        this.user_details = user_details;
    }


}
