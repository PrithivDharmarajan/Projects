package com.smaat.renterblock.entity;

import java.io.Serializable;



public class AlertFilterObjectEntity implements Serializable {
    private FilterEntity Sale;
    private FilterEntity Rent;

    public FilterEntity getSale() {
        return Sale;
    }

    public void setSale(FilterEntity sale) {
        Sale = sale;
    }

    public FilterEntity getRent() {
        return Rent;
    }

    public void setRent(FilterEntity rent) {
        Rent = rent;
    }
}
