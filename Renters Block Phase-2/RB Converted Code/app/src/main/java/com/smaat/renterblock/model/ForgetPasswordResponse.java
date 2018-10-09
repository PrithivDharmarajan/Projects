package com.smaat.renterblock.model;

import java.io.Serializable;

public class ForgetPasswordResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public Result result;
	public String error_code;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
