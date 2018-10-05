package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by admin on 8/16/2016.
 */

public class SaveStripCardEntityResponse implements Serializable {
    private String message;
    private String user_id;
    private String stripe_id;
    private String card_id;

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getStripe_id() {
        return stripe_id;
    }

    public void setStripe_id(String stripe_id) {
        this.stripe_id = stripe_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
