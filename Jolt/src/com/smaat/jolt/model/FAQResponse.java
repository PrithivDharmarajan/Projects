package com.smaat.jolt.model;

import java.util.ArrayList;

import com.smaat.jolt.entity.FAQEntity;

public class FAQResponse {
	private String error_code;
	private String msg;
	private ArrayList<FAQEntity> result;

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

	public ArrayList<FAQEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<FAQEntity> result) {
		this.result = result;
	}

}