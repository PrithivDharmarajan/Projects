package com.bridgellc.bridge.entity;

import java.io.Serializable;


public class BankAddAccountEntity implements Serializable {

    private String message;
    private String cardcount;

    public String getCardcount() {
        return cardcount;
    }

    public void setCardcount(String cardcount) {
        this.cardcount = cardcount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
