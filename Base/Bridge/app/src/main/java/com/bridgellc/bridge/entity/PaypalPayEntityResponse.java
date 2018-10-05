package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by admin on 6/3/2016.
 */
public class PaypalPayEntityResponse implements Serializable {

    private String message;
    private String unique_code;
    private String paymnet_id;
    private String payment_id;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPaymnet_id() {
        return paymnet_id;
    }

    public void setPaymnet_id(String paymnet_id) {
        this.paymnet_id = paymnet_id;
    }

    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
