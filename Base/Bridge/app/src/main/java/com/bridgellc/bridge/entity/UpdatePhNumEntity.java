package com.bridgellc.bridge.entity;

import java.io.Serializable;

public class UpdatePhNumEntity implements Serializable {

    private String OTP_code;
    private String number;
    private String otp;
    private String result;
    private String editable;

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String getOTP_code() {
        return OTP_code;
    }

    public void setOTP_code(String OTP_code) {
        this.OTP_code = OTP_code;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

}
