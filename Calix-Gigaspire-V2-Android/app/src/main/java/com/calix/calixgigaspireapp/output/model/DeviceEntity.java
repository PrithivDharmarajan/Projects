package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;


public class DeviceEntity implements Serializable {


    private String deviceId = "";
    private String name = "";
    private RouterEntity router = new RouterEntity();
    private double signalStrength = 0;
    private SpeedEntity speed = new SpeedEntity();
    private SpeedEntity networkUsage = new SpeedEntity();
    private int type = 0;
    private int subType = 0;
    private boolean connected2network;
    private String ipAddress = "";
    private String band = "";
    private int channel;
    private String connectionStatus = "";
    private String status = "";
    private String ifType = "";
    private String location;

    public boolean isConnected2network() {
        return connected2network;
    }

    public void setConnected2network(boolean connected2network) {
        this.connected2network = connected2network;
    }

    public String getIfType() {
        return ifType;
    }

    public void setIfType(String ifType) {
        this.ifType = ifType;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RouterEntity getRouter() {
        return router;
    }

    public void setRouter(RouterEntity router) {
        this.router = router;
    }

    public String getSignalStrength() {
        return String.valueOf(signalStrength);
    }

    public void setSignalStrength(double signalStrength) {
        this.signalStrength = signalStrength;
    }

    public SpeedEntity getSpeed() {
        return speed == null ? new SpeedEntity() : speed;
    }

    public void setSpeed(SpeedEntity speed) {
        this.speed = speed;
    }

    public SpeedEntity getNetworkUsage() {
        return networkUsage;
    }

    public void setNetworkUsage(SpeedEntity networkUsage) {
        this.networkUsage = networkUsage;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getChannel() {
        return String.valueOf(channel);
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }


}
