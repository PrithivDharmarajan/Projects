package com.bridgellc.bridge.entity;

import java.io.Serializable;

public class PayForItemEntity implements Serializable {

    private String unique_code;
    private String payment_id;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }


    public String getUnique_code() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = unique_code;
    }
}
