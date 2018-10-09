package com.smaat.renterblock.model;

import java.io.Serializable;

import com.smaat.renterblock.entity.MyReviewsResult;

public class MyReviewResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String error_code;
	private String msg;
	private MyReviewsResult result;

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

	public MyReviewsResult getResult() {
		return result;
	}

	public void setResult(MyReviewsResult result) {
		this.result = result;
	}

}
