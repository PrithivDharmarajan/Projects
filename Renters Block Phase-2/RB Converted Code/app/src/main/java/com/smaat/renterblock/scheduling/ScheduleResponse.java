package com.smaat.renterblock.scheduling;

import java.io.Serializable;

public class ScheduleResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String error_code;
	private String msg;
	private ScheduleResultEntity result;

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

	public ScheduleResultEntity getResult() {
		return result;
	}

	public void setResult(ScheduleResultEntity result) {
		this.result = result;
	}

}
