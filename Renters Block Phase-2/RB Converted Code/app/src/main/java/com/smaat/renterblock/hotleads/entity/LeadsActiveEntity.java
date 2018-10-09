package com.smaat.renterblock.hotleads.entity;

import java.io.Serializable;

public class LeadsActiveEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	private String datetime;
	private String username;
	private String user_name;
	private String user_profileImage;
	private String user_avg_rating;
	private String friends_count;
	private String reviews_count;
	private String photos_count;
	private String is_friend;
	private String count;
	private String rb_user;
	private String isfavourite;
	private String isActive;

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getRb_user() {
		return rb_user;
	}

	public void setRb_user(String rb_user) {
		this.rb_user = rb_user;
	}

	public String getIsfavourite() {
		return isfavourite;
	}

	public void setIsfavourite(String isfavourite) {
		this.isfavourite = isfavourite;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getIs_friend() {
		return is_friend;
	}

	public void setIs_friend(String is_friend) {
		this.is_friend = is_friend;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_profileImage() {
		return user_profileImage;
	}

	public void setUser_profileImage(String user_profileImage) {
		this.user_profileImage = user_profileImage;
	}

	public String getUser_avg_rating() {
		return user_avg_rating;
	}

	public void setUser_avg_rating(String user_avg_rating) {
		this.user_avg_rating = user_avg_rating;
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

	public String getPhotos_count() {
		return photos_count;
	}

	public void setPhotos_count(String photos_count) {
		this.photos_count = photos_count;
	}
}
