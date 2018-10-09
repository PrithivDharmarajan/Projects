package com.smaat.renterblock.entity;


import java.io.Serializable;

public class FilterAPIEntity implements Serializable{

    private String Sale;
    private String Rent;

    public String getSale() {
        return Sale;
    }

    public void setSale(String sale) {
        Sale = sale;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String rent) {
        Rent = rent;
    }
}

