package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by USER on 4/13/2016.
 */
public class BankAccDetailsEntity implements Serializable {

    private String user_id;
    private String account_number;
    private String routing_number;
    private String user_name;
    private String bank_name;
    private String original_account_number;



    public String getOriginal_account_number() {
        return original_account_number;
    }

    public void setOriginal_account_number(String original_account_number) {
        this.original_account_number = original_account_number;
    }


    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getRouting_number() {
        return routing_number;
    }

    public void setRouting_number(String routing_number) {
        this.routing_number = routing_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }



}
