package com.smaat.renterblock.entity;

import java.io.Serializable;

public class MyReviewUserDetails implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String first_name;
	private String user_pic;
	private String user_name;
	private String getreviewcount;
	private String getfriends;
	private String getproimagecount;
	
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getUser_pic() {
		return user_pic;
	}
	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}
	public String getGetreviewcount() {
		return getreviewcount;
	}
	public void setGetreviewcount(String getreviewcount) {
		this.getreviewcount = getreviewcount;
	}
	public String getGetfriends() {
		return getfriends;
	}
	public void setGetfriends(String getfriends) {
		this.getfriends = getfriends;
	}
	public String getGetproimagecount() {
		return getproimagecount;
	}
	public void setGetproimagecount(String getproimagecount) {
		this.getproimagecount = getproimagecount;
	}
	
	

}
