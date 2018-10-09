package com.smaat.renterblock.findagent;

import java.io.Serializable;

public class AgentUserDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user_id;
	private String user_profileImage;
	private String user_avg_rating;
	private String friends_count;
	private String reviews_count;
	private String photos_count;
	private String name;
	private String comments;
	private String licence;
	private String business_name;
	private String user_type;
	private String property_listing;
	private String description;
	private String last_name;
	private String email_id;
	private String rb_user;
	
	public String getRb_user() {
		return rb_user;
	}

	public void setRb_user(String rb_user) {
		this.rb_user = rb_user;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail_id() {
		return email_id;
	}

	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProperty_listing() {
		return property_listing;
	}

	public void setProperty_listing(String property_listing) {
		this.property_listing = property_listing;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getLicence() {
		return licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public String getBusiness_name() {
		return business_name;
	}

	public void setBusiness_name(String business_name) {
		this.business_name = business_name;
	}

	public String getUser_type() {
		return user_type;
	}

	public void setUser_type(String user_type) {
		this.user_type = user_type;
	}

}
