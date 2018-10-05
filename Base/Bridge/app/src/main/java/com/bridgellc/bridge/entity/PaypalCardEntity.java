package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by admin on 6/30/2016.
 */
public class PaypalCardEntity implements Serializable {


    private String card_id;
    private String user_id;
    private String paypalCardFlag;
    private String card_number;
    private String exp_month;
    private String exp_year;
    private String cvc;
    private String original_card_number;
    private String priority;
    private String user_name;
    private String card_brand;
    private String first_name;
    private String message;
    private String unique_code;
    private String paymnet_id;
    private String payment_id;
    private String paypal_id;
    private String paypal_email;
    private String last_name;


    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getPaypal_id() {
        return paypal_id;
    }

    public void setPaypal_id(String paypal_id) {
        this.paypal_id = paypal_id;
    }


    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCard_brand() {
        return card_brand;
    }

    public void setCard_brand(String card_brand) {
        this.card_brand = card_brand;
    }


    public String getOriginal_card_number() {
        return original_card_number;
    }

    public void setOriginal_card_number(String original_card_number) {
        this.original_card_number = original_card_number;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public String getPaypalCardFlag() {
        return paypalCardFlag;
    }

    public void setPaypalCardFlag(String paypalCardFlag) {
        this.paypalCardFlag = paypalCardFlag;
    }


}
