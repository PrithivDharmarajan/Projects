package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by USER on 4/4/2016.
 */
public class PurchaseBuyerDetailsEntity implements Serializable {

    private String user_status;
    private String user_verify;
    private String payment_id;
    private String item_id;
    private String amount_received;
    private String processing_fee;
    private String quantity;
    private String count;
    private String payment_details;

    public String getUser_verify() {
        return user_verify;
    }

    public void setUser_verify(String user_verify) {
        this.user_verify = user_verify;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }


    public String getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(String payment_details) {
        this.payment_details = payment_details;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getItem_cost() {
        return item_cost;
    }

    public void setItem_cost(String item_cost) {
        this.item_cost = item_cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProcessing_fee() {
        return processing_fee;
    }

    public void setProcessing_fee(String processing_fee) {
        this.processing_fee = processing_fee;
    }

    public String getAmount_received() {
        return amount_received;
    }

    public void setAmount_received(String amount_received) {
        this.amount_received = amount_received;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    private String item_cost;


}
