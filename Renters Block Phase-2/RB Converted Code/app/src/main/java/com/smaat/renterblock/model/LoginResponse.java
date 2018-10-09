package com.smaat.renterblock.model;

import java.io.Serializable;

import com.smaat.renterblock.entity.Login;

public class LoginResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String error_code;
	public Login result;
	public String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public Login getResult() {
		return result;
	}

	public void setResult(Login result) {
		this.result = result;
	}

}
