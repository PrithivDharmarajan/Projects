package com.smaat.jolt.model;

import com.smaat.jolt.entity.JoltPlansDetailsEntity;

public class JoltPlansResponse {
	private String error_code;
	private String msg;
	private JoltPlansDetailsEntity result;

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

	public JoltPlansDetailsEntity getResult() {
		return result;
	}

	public void setResult(JoltPlansDetailsEntity result) {
		this.result = result;
	}

}
