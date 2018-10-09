package com.smaat.renterblock.friends.entity;

import java.io.Serializable;
import java.util.Comparator;

import com.smaat.renterblock.entity.PropertyEntity;

public class FriendMainScreenEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String user_friend_id;
	private String first_name;
	private String last_name;
	private String user_pic;
	private String status;
	private String isnew;
	private String friends;
	private String Review;
	private String Photos;
	private String rating;
	private String id;
	private String user_name;
	private String enhanced_profile;
	private String accept;

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getEnhanced_profile() {
		return enhanced_profile;
	}

	public void setEnhanced_profile(String enhanced_profile) {
		this.enhanced_profile = enhanced_profile;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUser_friend_id() {
		return user_friend_id;
	}

	public void setUser_friend_id(String user_friend_id) {
		this.user_friend_id = user_friend_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsnew() {
		return isnew;
	}

	public void setIsnew(String isnew) {
		this.isnew = isnew;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getReview() {
		return Review;
	}

	public void setReview(String review) {
		Review = review;
	}

	public String getPhotos() {
		return Photos;
	}

	public void setPhotos(String photos) {
		Photos = photos;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public static Comparator<FriendMainScreenEntity> FRIEND_IN_ALPHABET_ORDER = new Comparator<FriendMainScreenEntity>() {

		@Override
		public int compare(FriendMainScreenEntity arg0,
				FriendMainScreenEntity arg1) {
			String user_name1 = arg0.getUser_name().toUpperCase();
			String user_name2 = arg1.getUser_name().toUpperCase();

			return user_name1.compareTo(user_name2);
		}

	};
}
