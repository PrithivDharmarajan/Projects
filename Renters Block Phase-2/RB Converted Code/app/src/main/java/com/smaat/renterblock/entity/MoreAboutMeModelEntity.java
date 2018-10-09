package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MoreAboutMeModelEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String msg;
	private ArrayList<MoreAboutMeEntity> result;
	private String about;
	
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code) {
		this.error_code = error_code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public ArrayList<MoreAboutMeEntity> getResult() {
		return result;
	}
	public void setResult(ArrayList<MoreAboutMeEntity> result) {
		this.result = result;
	}

}
