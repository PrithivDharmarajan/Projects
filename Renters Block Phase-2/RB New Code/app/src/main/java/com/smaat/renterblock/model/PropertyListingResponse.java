package com.smaat.renterblock.model;

import com.smaat.renterblock.entity.PropertyEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class PropertyListingResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String error_code;
	private String msg;
	private ArrayList<PropertyEntity> result;

	public ArrayList<PropertyEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<PropertyEntity> result) {
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
