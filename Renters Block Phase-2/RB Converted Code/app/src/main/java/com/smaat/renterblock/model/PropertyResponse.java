package com.smaat.renterblock.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.renterblock.entity.PropertyEntity;

public class PropertyResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	public String error_code;
	public ArrayList<PropertyEntity> result;
	public String msg;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public ArrayList<PropertyEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<PropertyEntity> result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
