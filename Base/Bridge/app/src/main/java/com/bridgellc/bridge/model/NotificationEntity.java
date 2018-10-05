package com.bridgellc.bridge.model;

import com.bridgellc.bridge.entity.HomeSingleItemEntity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 7/15/2016.
 */
public class NotificationEntity implements Serializable {
    private String count;
    private ArrayList<HomeSingleItemEntity> notification;

    public ArrayList<HomeSingleItemEntity> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<HomeSingleItemEntity> notification) {
        this.notification = notification;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

}
