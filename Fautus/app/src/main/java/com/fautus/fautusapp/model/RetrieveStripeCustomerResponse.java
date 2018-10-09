package com.fautus.fautusapp.model;

import com.fautus.fautusapp.entity.CardDataEntity;

import java.io.Serializable;

/**
 * Created by sys on 25-May-17.
 */

public class RetrieveStripeCustomerResponse implements Serializable {

    private String id;
    private String object;
    private Double account_balance;
    private Double created;
    private String currency;
    private String default_source;
    private boolean delinquent;
    private String description;
    private String discount;
    private String email;
    private boolean livemode;
    private String shipping;
    private CardDataEntity sources;

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

    public Double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }

    public Double getCreated() {
        return created;
    }

    public void setCreated(Double created) {
        this.created = created;
    }

//    public String getCurrency() {
//        return currency;
//    }
//
//    public void setCurrency(String currency) {
//        this.currency = currency;
//    }

    public String getDefault_source() {
        return default_source;
    }

    public void setDefault_source(String default_source) {
        this.default_source = default_source;
    }

    public boolean getDelinquent() {
        return delinquent;
    }

    public void setDelinquent(boolean delinquent) {
        this.delinquent = delinquent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public String getDiscount() {
//        return discount;
//    }
//
//    public void setDiscount(String discount) {
//        this.discount = discount;
//    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getLivemode() {
        return livemode;
    }

    public void setLivemode(boolean livemode) {
        this.livemode = livemode;
    }

//    public String getShipping() {
//        return shipping;
//    }
//
//    public void setShipping(String shipping) {
//        this.shipping = shipping;
//    }

    public CardDataEntity getSources() {
        return sources;
    }

    public void setSources(CardDataEntity sources) {
        this.sources = sources;
    }

}
