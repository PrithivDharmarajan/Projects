package com.bridgellc.bridge.model;

import java.io.Serializable;

/**
 * Created by admin on 6/2/2016.
 */
public class PaypalEntityResponse implements Serializable {



    private String priority;
    private String paypal_id;
    private String user_id;
    private String paypal_email;
    private String first_name;
    private String last_name;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPaypal_id() {
        return paypal_id;
    }

    public void setPaypal_id(String paypal_id) {
        this.paypal_id = paypal_id;
    }


}
