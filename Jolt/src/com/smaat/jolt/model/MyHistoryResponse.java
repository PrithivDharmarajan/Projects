package com.smaat.jolt.model;

import java.io.Serializable;
import java.util.ArrayList;
import com.smaat.jolt.entity.HistoryDetailsEntity;

public class MyHistoryResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String error_code;
	private String msg;
	private ArrayList<HistoryDetailsEntity> result;

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

	public ArrayList<HistoryDetailsEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<HistoryDetailsEntity> result) {
		this.result = result;
	}
}
