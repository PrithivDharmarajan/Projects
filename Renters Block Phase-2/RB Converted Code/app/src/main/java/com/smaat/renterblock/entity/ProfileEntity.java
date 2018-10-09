package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.renterblock.profile.Entity.ProfileMessageBoardEntity;
import com.smaat.renterblock.profile.Entity.ProfileUserEntity;
import com.smaat.renterblock.profile.Entity.ProfileUserReviews;
import com.smaat.renterblock.profile.Entity.Profilephotoandvideos;

public class ProfileEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String friendscount;
	private String photocount;
	private String reviewscount;
	private String rating;
	private String is_friend;
	private String accept;
	
	public String getAccept() {
		return accept;
	}
	public void setAccept(String accept) {
		this.accept = accept;
	}
	private ArrayList<ProfileUserEntity> user;
	private ArrayList<ProfileUserReviews> reviews;
	private ArrayList<Profilephotoandvideos> photoandvideo;
	private ArrayList<ProfileMessageBoardEntity> messageboard;
	
	public ArrayList<ProfileMessageBoardEntity> getMessageboard() {
		return messageboard;
	}
	public void setMessageboard(ArrayList<ProfileMessageBoardEntity> messageboard) {
		this.messageboard = messageboard;
	}
	public String getIs_friend() {
		return is_friend;
	}
	public void setIs_friend(String is_friend) {
		this.is_friend = is_friend;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getFriendscount() {
		return friendscount;
	}
	public void setFriendscount(String friendscount) {
		this.friendscount = friendscount;
	}
	public String getPhotocount() {
		return photocount;
	}
	public void setPhotocount(String photocount) {
		this.photocount = photocount;
	}
	public String getReviewscount() {
		return reviewscount;
	}
	public void setReviewscount(String reviewscount) {
		this.reviewscount = reviewscount;
	}
	public ArrayList<ProfileUserEntity> getUser() {
		return user;
	}
	public void setUser(ArrayList<ProfileUserEntity> user) {
		this.user = user;
	}
	public ArrayList<ProfileUserReviews> getReviews() {
		return reviews;
	}
	public void setReviews(ArrayList<ProfileUserReviews> reviews) {
		this.reviews = reviews;
	}
	public ArrayList<Profilephotoandvideos> getPhotoandvideo() {
		return photoandvideo;
	}
	public void setPhotoandvideo(ArrayList<Profilephotoandvideos> photoandvideo) {
		this.photoandvideo = photoandvideo;
	}

}
