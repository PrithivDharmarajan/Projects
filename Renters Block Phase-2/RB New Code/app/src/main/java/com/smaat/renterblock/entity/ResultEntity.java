package com.smaat.renterblock.entity;

import java.util.ArrayList;

/**
 * Created by sys on 10/6/2017.
 */

public class ResultEntity {

    private String friendscount;
    private String photocount;
    private String is_friend;
    private String accept;
    private String reviewscount;
    private String rating;

    private ArrayList<UserDetailsEntity> user;
    private ArrayList<UserPropertyPicsEntity> photoandvideo;
    private ArrayList<MessageBoardEntity> messageboard;
    private ArrayList<ReviewEntity> reviews;

    public ArrayList<ReviewEntity> getReviews() {
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    public void setReviews(ArrayList<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public String getFriendscount() {

        if (friendscount == null) {
            friendscount = "";
        }
        return friendscount;
    }

    public void setFriendscount(String friendscount) {
        this.friendscount = friendscount;
    }

    public String getPhotocount() {
        if (photocount == null) {
            photocount = "";
        }
        return photocount;
    }

    public void setPhotocount(String photocount) {
        this.photocount = photocount;
    }

    public String getIs_friend() {
        if (is_friend == null) {
            is_friend = "";
        }
        return is_friend;
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public String getAccept() {
        if (accept == null) {
            accept = "";
        }
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getReviewscount() {
        if (reviewscount == null) {
            reviewscount = "";
        }
        return reviewscount;
    }

    public void setReviewscount(String reviewscount) {
        this.reviewscount = reviewscount;
    }

    public String getRating() {
        if (rating == null) {
            rating = "";
        }
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<UserDetailsEntity> getUser() {
        if (user == null) {
            user = new ArrayList<>();
        }
        return user;
    }

    public void setUser(ArrayList<UserDetailsEntity> user) {
        this.user = user;
    }

    public ArrayList<UserPropertyPicsEntity> getPhotoandvideo() {
        if (photoandvideo == null) {
            photoandvideo = new ArrayList<>();
        }
        return photoandvideo;
    }

    public void setPhotoandvideo(ArrayList<UserPropertyPicsEntity> photoandvideo) {
        this.photoandvideo = photoandvideo;
    }

    public ArrayList<MessageBoardEntity> getMessageboard() {
        if (messageboard == null) {
            messageboard = new ArrayList<>();
        }
        return messageboard;
    }

    public void setMessageboard(ArrayList<MessageBoardEntity> messageboard) {
        this.messageboard = messageboard;
    }
}
