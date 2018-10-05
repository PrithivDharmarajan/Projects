package com.bridgellc.bridge.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by USER on 4/5/2016.
 */
public class PaymentHistoryEntity implements Serializable {

    private ArrayList<PaymentBuySellEntity> buy;
    private ArrayList<PaymentBuySellEntity> escrow;
    private ArrayList<PaymentBuySellEntity> sell;
    private String payment_sent;
    private String payment_received;
    private String payment_to_be_sent;
    private String payment_to_be_received;


    public ArrayList<PaymentBuySellEntity> getEscrow() {
        return escrow;
    }

    public void setEscrow(ArrayList<PaymentBuySellEntity> escrow) {
        this.escrow = escrow;
    }


    public ArrayList<PaymentBuySellEntity> getSell() {
        return sell;
    }

    public void setSell(ArrayList<PaymentBuySellEntity> sell) {
        this.sell = sell;
    }

    public ArrayList<PaymentBuySellEntity> getBuy() {
        return buy;
    }

    public void setBuy(ArrayList<PaymentBuySellEntity> buy) {
        this.buy = buy;
    }

    public String getPayment_to_be_received() {
        return payment_to_be_received;
    }

    public void setPayment_to_be_received(String payment_to_be_received) {
        this.payment_to_be_received = payment_to_be_received;
    }

    public String getPayment_to_be_sent() {
        return payment_to_be_sent;
    }

    public void setPayment_to_be_sent(String payment_to_be_sent) {
        this.payment_to_be_sent = payment_to_be_sent;
    }

    public String getPayment_received() {
        return payment_received;
    }

    public void setPayment_received(String payment_received) {
        this.payment_received = payment_received;
    }

    public String getPayment_sent() {
        return payment_sent;
    }

    public void setPayment_sent(String payment_sent) {
        this.payment_sent = payment_sent;
    }
}
