package com.smaat.renterblock.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.renterblock.entity.BoardsDetails;

public class BoardsResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	public String error_code;
	public String msg;
	public ArrayList<BoardsDetails> result;

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

	public ArrayList<BoardsDetails> getResult() {
		return result;
	}

	public void setResult(ArrayList<BoardsDetails> result) {
		this.result = result;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
