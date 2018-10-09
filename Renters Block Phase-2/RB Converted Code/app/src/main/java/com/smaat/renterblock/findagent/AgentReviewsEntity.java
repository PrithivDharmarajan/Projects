package com.smaat.renterblock.findagent;

import java.io.Serializable;

public class AgentReviewsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String user_id;
	private String user_profileImage;
	private String user_avg_rating;
	private String friends_count;
	private String reviews_count;
	private String photos_count;
	private String name;
	private String comments;
	private String rating;
	
	//Add key in service
	private String is_friend;
	private String enhanced_profile;

	public String getIs_friend() {
		return is_friend;
	}

	public void setIs_friend(String is_friend) {
		this.is_friend = is_friend;
	}

	public String getEnhanced_profile() {
		return enhanced_profile;
	}

	public void setEnhanced_profile(String enhanced_profile) {
		this.enhanced_profile = enhanced_profile;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

}
