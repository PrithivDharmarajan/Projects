package com.smaat.ipharma.model;

import java.io.Serializable;

import com.smaat.ipharma.entity.Register;

public class ProfileResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	String error_code;
	String msg;
	Register result;

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

	public Register getResult() {
		return result;
	}

	public void setResult(Register result) {
		this.result = result;
	}

}
