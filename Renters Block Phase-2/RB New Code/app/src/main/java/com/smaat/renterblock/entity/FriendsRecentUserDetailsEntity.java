package com.smaat.renterblock.entity;

import java.io.Serializable;

public class FriendsRecentUserDetailsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String userimage;
	private String user_name;
	private String enhanced_profile;
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserimage() {
		return userimage;
	}

	public void setUserimage(String userimage) {
		this.userimage = userimage;
	}

}
