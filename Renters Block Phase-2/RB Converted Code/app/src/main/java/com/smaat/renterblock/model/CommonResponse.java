package com.smaat.renterblock.model;

import java.io.Serializable;

public class CommonResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	public String result;
	public String error_code;
	public String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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
