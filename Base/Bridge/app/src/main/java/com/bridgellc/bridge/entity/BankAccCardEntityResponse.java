package com.bridgellc.bridge.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 5/11/2016.
 */
public class BankAccCardEntityResponse implements Serializable {

    private String paymenttype;
    private ArrayList<PaypalCardEntity> bank;
    private ArrayList<PaypalCardEntity> debit;
    private ArrayList<PaypalCardEntity> paypal;


    public ArrayList<PaypalCardEntity> getPaypal() {
        return paypal;
    }

    public void setPaypal(ArrayList<PaypalCardEntity> paypal) {
        this.paypal = paypal;
    }

    public ArrayList<PaypalCardEntity> getDebit() {
        return debit;
    }

    public void setDebit(ArrayList<PaypalCardEntity> debit) {
        this.debit = debit;
    }

    public ArrayList<PaypalCardEntity> getBank() {
        return bank;
    }

    public void setBank(ArrayList<PaypalCardEntity> bank) {
        this.bank = bank;
    }

    public String getPaymenttype() {
        return paymenttype;
    }

    public void setPaymenttype(String paymenttype) {
        this.paymenttype = paymenttype;
    }

}
