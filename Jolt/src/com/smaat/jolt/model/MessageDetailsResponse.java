package com.smaat.jolt.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.jolt.entity.JoltMessageDetailsEntity;

public class MessageDetailsResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error_code;
	private String msg;
	private ArrayList<JoltMessageDetailsEntity> result;

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

	public ArrayList<JoltMessageDetailsEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<JoltMessageDetailsEntity> result) {
		this.result = result;
	}
}
