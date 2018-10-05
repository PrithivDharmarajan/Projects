package com.smaat.jolt.model;

import java.io.Serializable;

import com.smaat.jolt.entity.TermsEntity;

public class TermsResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error_code;
	private String msg;
	private TermsEntity result;

	public TermsEntity getResult() {
		return result;
	}

	public void setResult(TermsEntity result) {
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
