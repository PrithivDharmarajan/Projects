package com.calix.calixgigamanage.output.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by user on 1/24/2018.
 */

public class DashboardResponse implements Serializable {

    private int notifUnreadCount=0;
    private int deviceCount=0;
    private SpeedEntity speed=new SpeedEntity();
    private UserDetailsEntity user=new UserDetailsEntity();
    private ArrayList<CategoriesEntity> categories;

    public String getNotifUnreadCount() {
        return String.valueOf(notifUnreadCount);
    }

    public void setNotifUnreadCount(int notifUnreadCount) {
        this.notifUnreadCount = notifUnreadCount;
    }

    public String getDeviceCount() {
        return String.valueOf(deviceCount);
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public SpeedEntity getSpeed() {
        return speed == null ? new SpeedEntity() : speed;
    }

    public void setSpeed(SpeedEntity speed) {
        this.speed = speed;
    }

    public UserDetailsEntity getUser() {
        return user == null ? new UserDetailsEntity() : user;
    }

    public void setUser(UserDetailsEntity user) {
        this.user = user;
    }

    public ArrayList<CategoriesEntity> getCategories() {
        return categories == null ? new ArrayList<CategoriesEntity>() : categories;
    }

    public void setCategories(ArrayList<CategoriesEntity> categories) {
        this.categories = categories;
    }


}
