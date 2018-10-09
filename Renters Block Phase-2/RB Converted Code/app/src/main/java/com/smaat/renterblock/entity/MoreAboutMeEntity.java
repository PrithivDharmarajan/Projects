package com.smaat.renterblock.entity;

import java.io.Serializable;

public class MoreAboutMeEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String photo_video_id;
	private String user_id;
	private String file_type;
	private String file;
	private String file_order;
	private String photo_description;
	private String datetime;
	
	public String getPhoto_description() {
		return photo_description;
	}
	public void setPhoto_description(String photo_description) {
		this.photo_description = photo_description;
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
	public String getFile_order() {
		return file_order;
	}
	public void setFile_order(String file_order) {
		this.file_order = file_order;
	}

}
