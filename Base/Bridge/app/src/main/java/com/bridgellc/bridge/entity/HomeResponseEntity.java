package com.bridgellc.bridge.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 3/25/2016.
 */
public class HomeResponseEntity implements Serializable {

    private ArrayList<HomeSingleItemEntity> goods;
    private ArrayList<HomeSingleItemEntity> services;
    private SignInEntity paymnet_details;
    private String paymnet_mode;
    private String payment_details;
    private String partner;


    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }


    public String getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(String payment_details) {
        this.payment_details = payment_details;
    }

    public String getPaymnet_mode() {
        return paymnet_mode;
    }

    public void setPaymnet_mode(String paymnet_mode) {
        this.paymnet_mode = paymnet_mode;
    }

    public ArrayList<HomeSingleItemEntity> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<HomeSingleItemEntity> goods) {
        this.goods = goods;
    }

    public ArrayList<HomeSingleItemEntity> getServices() {
        return services;
    }

    public void setServices(ArrayList<HomeSingleItemEntity> services) {
        this.services = services;
    }

    public SignInEntity getPaymnet_details() {
        return paymnet_details;
    }

    public void setPaymnet_details(SignInEntity paymnet_details) {
        this.paymnet_details = paymnet_details;
    }
}
