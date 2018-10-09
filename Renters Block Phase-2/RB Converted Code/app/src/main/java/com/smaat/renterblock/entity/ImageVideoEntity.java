package com.smaat.renterblock.entity;

import java.io.Serializable;

public class ImageVideoEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String image_file;
	private String video_file;
	private String is_image;
	private String is_video;
	
	public String getImage_file() {
		return image_file;
	}
	public void setImage_file(String image_file) {
		this.image_file = image_file;
	}
	public String getVideo_file() {
		return video_file;
	}
	public void setVideo_file(String video_file) {
		this.video_file = video_file;
	}
	public String getIs_image() {
		return is_image;
	}
	public void setIs_image(String is_image) {
		this.is_image = is_image;
	}
	public String getIs_video() {
		return is_video;
	}
	public void setIs_video(String is_video) {
		this.is_video = is_video;
	}
}
