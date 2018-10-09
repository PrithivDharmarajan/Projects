package com.smaat.ipharma.entity;


public class CommonResponse {
	String error_code;
	String status;
	String msg;
	RegisterationResponse result;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RegisterationResponse getResult() {
		return result;
	}

	public void setResult(RegisterationResponse result) {
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
