package com.smaat.ipharma.entity;

import java.util.ArrayList;

/**
 * Created by admin on 1/24/2017.
 */

public class RecentOrderDetailList {
    ArrayList<RecentOrderEntity> OrderResult;
    String OrderCount;

    public String getOrderCount() {
        return OrderCount;
    }

    public ArrayList<RecentOrderEntity> getOrderResult() {
        return OrderResult;
    }

    public void setOrderResult(ArrayList<RecentOrderEntity> orderResult) {
        OrderResult = orderResult;
    }

    public void setOrderCount(String orderCount) {
        OrderCount = orderCount;
    }


}
