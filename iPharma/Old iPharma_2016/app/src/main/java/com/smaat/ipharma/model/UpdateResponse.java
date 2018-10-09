package com.smaat.ipharma.model;


import com.smaat.ipharma.entity.UserDetailsResponse;

public class UpdateResponse {
	private String status;
	private String msg;
	private UserDetailsResponse result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UserDetailsResponse getResult() {
		return result;
	}

	public void setResult(UserDetailsResponse result) {
		this.result = result;
	}

}
