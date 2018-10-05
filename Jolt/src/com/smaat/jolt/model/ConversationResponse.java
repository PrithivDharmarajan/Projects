package com.smaat.jolt.model;

import java.io.Serializable;

public class ConversationResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String error_code;
	String msg;
	String result;

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}