package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;
import java.util.ArrayList;

public class DashboardResponse implements Serializable {

    private int notifUnreadCount = 0;
    private int deviceCount = 0;
    private SpeedEntity speed = new SpeedEntity();
    private UserEntity user = new UserEntity();
    private ArrayList<CategoryEntity> categories = new ArrayList<>();

    public int getNotifUnreadCount() {
        return notifUnreadCount;
    }

    public void setNotifUnreadCount(int notifUnreadCount) {
        this.notifUnreadCount = notifUnreadCount;
    }

    public int getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(int deviceCount) {
        this.deviceCount = deviceCount;
    }

    public SpeedEntity getSpeed() {
        return speed;
    }

    public void setSpeed(SpeedEntity speed) {
        this.speed = speed;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ArrayList<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryEntity> categories) {
        this.categories = categories;
    }
}
