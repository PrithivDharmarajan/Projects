package com.smaat.renterblock.entity;

import java.io.Serializable;

public class ProfileModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String error_code;
	private String msg;
	private ProfileEntity result;
	
	public ProfileEntity getResult() {
		return result;
	}
	public void setResult(ProfileEntity result) {
		this.result = result;
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
}
