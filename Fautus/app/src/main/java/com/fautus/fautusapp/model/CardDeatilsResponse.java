package com.fautus.fautusapp.model;

import java.io.Serializable;

/**
 * Created by sys on 25-May-17.
 */

public class CardDeatilsResponse implements Serializable {
    private String id;
    private String object;
    private String address_city;
    private String address_country;
    private String address_line1;
    private String address_line1_check;
    private String address_line2;
    private String address_state;
    private String address_zip;
    private String address_zip_check;
    private String brand;
    private String country;
    private String customer;
    private String cvc_check;
    private String dynamic_last4;
    private String exp_month;
    private String exp_year;
    private String fingerprint;
    private String funding;
    private String last4;
    private String name;
    private String tokenization_method;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getAddress_city() {
        return address_city;
    }

    public void setAddress_city(String address_city) {
        this.address_city = address_city;
    }

    public String getAddress_country() {
        return address_country;
    }

    public void setAddress_country(String address_country) {
        this.address_country = address_country;
    }

    public String getAddress_line1() {
        return address_line1;
    }

    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    public String getAddress_line1_check() {
        return address_line1_check;
    }

    public void setAddress_line1_check(String address_line1_check) {
        this.address_line1_check = address_line1_check;
    }

    public String getAddress_line2() {
        return address_line2;
    }

    public void setAddress_line2(String address_line2) {
        this.address_line2 = address_line2;
    }

    public String getAddress_state() {
        return address_state;
    }

    public void setAddress_state(String address_state) {
        this.address_state = address_state;
    }

    public String getAddress_zip() {
        return address_zip;
    }

    public void setAddress_zip(String address_zip) {
        this.address_zip = address_zip;
    }

    public String getAddress_zip_check() {
        return address_zip_check;
    }

    public void setAddress_zip_check(String address_zip_check) {
        this.address_zip_check = address_zip_check;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCvc_check() {
        return cvc_check;
    }

    public void setCvc_check(String cvc_check) {
        this.cvc_check = cvc_check;
    }

    public String getDynamic_last4() {
        return dynamic_last4;
    }

    public void setDynamic_last4(String dynamic_last4) {
        this.dynamic_last4 = dynamic_last4;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTokenization_method() {
        return tokenization_method;
    }

    public void setTokenization_method(String tokenization_method) {
        this.tokenization_method = tokenization_method;
    }

}
