package com.smaat.renterblock.entity;

import java.io.Serializable;

public class UserPropertyPics implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String photo_video_id;
	private String user_id;
	private String property_id;
	private String file_type;
	private String file;
	private String description;
	private String datetime;
	private String friends;
	private String Photos;
	private String Review;
	private String user_pic;
	private String first_name;
	private String user_name;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getPhotos() {
		return Photos;
	}

	public void setPhotos(String photos) {
		Photos = photos;
	}

	public String getReview() {
		return Review;
	}

	public void setReview(String review) {
		Review = review;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getPhoto_video_id() {
		return photo_video_id;
	}

	public void setPhoto_video_id(String photo_video_id) {
		this.photo_video_id = photo_video_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getProperty_id() {
		return property_id;
	}

	public void setProperty_id(String property_id) {
		this.property_id = property_id;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

}
