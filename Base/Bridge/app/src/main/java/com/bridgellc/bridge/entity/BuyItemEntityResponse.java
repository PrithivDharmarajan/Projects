package com.bridgellc.bridge.entity;

import java.io.Serializable;

/**
 * Created by admin on 6/3/2016.
 */
public class BuyItemEntityResponse implements Serializable {

    private String item_name;
    private String item_cost;
    private String item_quantity;
    private String total_cost;
    private String process_fee;


    public String getProcess_fee() {
        return process_fee;
    }

    public void setProcess_fee(String process_fee) {
        this.process_fee = process_fee;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_cost() {
        return item_cost;
    }

    public void setItem_cost(String item_cost) {
        this.item_cost = item_cost;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }


}
