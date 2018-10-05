package com.smaat.jolt.model;

import com.smaat.jolt.entity.UserDetailsEntity;

public class ProfileResponse {

	private String error_code;
	private String msg;
	private UserDetailsEntity profile;

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

	public UserDetailsEntity getProfile() {
		return profile;
	}

	public void setProfile(UserDetailsEntity result) {
		this.profile = result;
	}

}
