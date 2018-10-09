package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ProfileMyFeedsEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String property_review_id;
    private String property_id;
    private String review_user_id;
    private String comments;
    private String rating;
    private String date_time;
    private String user_id;
    private String file;
    private String file_type;
    private String picture_id;
    private String datetime;
    private String property_name;
    private String friends_count;
    private String reviews_count;
    private String photos_Count;
    private String user_name;
    private String user_profile_image;
    private String user_rating;
    private String address;
    private String description;
    private String post_id;
    private String message;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(String user_rating) {
        this.user_rating = user_rating;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(String friends_count) {
        this.friends_count = friends_count;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getPhotos_Count() {
        return photos_Count;
    }

    public void setPhotos_Count(String photos_Count) {
        this.photos_Count = photos_Count;
    }

    public String getProperty_review_id() {
        return property_review_id;
    }

    public void setProperty_review_id(String property_review_id) {
        this.property_review_id = property_review_id;
    }

    public String getProperty_id() {
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getReview_user_id() {
        return review_user_id;
    }

    public void setReview_user_id(String review_user_id) {
        this.review_user_id = review_user_id;
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

    public String getDate_time() {
        return date_time == null || date_time.equalsIgnoreCase("0000-00-00 00:00:00") ? "" : date_time;

    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public String getDatetime() {
        return datetime == null || datetime.equalsIgnoreCase("0000-00-00 00:00:00") ? "" : datetime;

    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getProperty_name() {
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public static Comparator<ProfileMyFeedsEntity> DATE_SORT = new Comparator<ProfileMyFeedsEntity>() {

        @Override
        public int compare(ProfileMyFeedsEntity arg0, ProfileMyFeedsEntity arg1) {
            Date date1 = getDate(arg0.getDatetime());
            Date date2 = getDate(arg1.getDatetime());

            return date2.compareTo(date1);
        }

    };

    private static Date getDate(String dateString) {
        SimpleDateFormat df;
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = null;
        try {

            date = df.parse(dateString);
            return date;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }
}
