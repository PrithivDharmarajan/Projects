package com.smaat.renterblock.model;

import java.io.Serializable;
import java.util.ArrayList;

import com.smaat.renterblock.entity.FavouriteDetails;

public class FavouriteReponse implements Serializable {

	private static final long serialVersionUID = 1L;
	public String error_code;
	public ArrayList<FavouriteDetails> result;
	public String msg;

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	public ArrayList<FavouriteDetails> getResult() {
		return result;
	}

	public void setResult(ArrayList<FavouriteDetails> result) {
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
