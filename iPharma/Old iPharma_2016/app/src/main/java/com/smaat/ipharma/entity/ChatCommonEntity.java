package com.smaat.ipharma.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatCommonEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;
	private String msg;
	private ArrayList<ChatReceiverEntity> result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<ChatReceiverEntity> getResult() {
		return result;
	}

	public void setResult(ArrayList<ChatReceiverEntity> result) {
		this.result = result;
	}

}
