package com.smaat.ipharma.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.ipharma.entity.Reviews;

public class ReviewsResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	String error_code;
	String msg;
	ArrayList<Reviews> result;

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

	public ArrayList<Reviews> getResult() {
		return result;
	}

	public void setResult(ArrayList<Reviews> result) {
		this.result = result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
