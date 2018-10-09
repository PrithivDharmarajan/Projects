package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

public class IOTRemoveDeviceEntity implements Serializable {

    private String ID = "";
    private String Name = "";
    private String FriendlyDeviceType = "";
    private String Type = "";
    private String Location = "";
    private String LastActiveEpoch = "";
    private String Model = "";
    private String Version = "";
    private String Manufacturer = "";
    private String DeviceState = "";
    private String DeviceONOFFState = "false";

    public String getDeviceONOFFState() {
        return DeviceONOFFState;
    }

    public void setDeviceONOFFState(String deviceONOFFState) {
        DeviceONOFFState = deviceONOFFState;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFriendlyDeviceType() {
        return FriendlyDeviceType;
    }

    public void setFriendlyDeviceType(String friendlyDeviceType) {
        FriendlyDeviceType = friendlyDeviceType;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLastActiveEpoch() {
        return LastActiveEpoch;
    }

    public void setLastActiveEpoch(String lastActiveEpoch) {
        LastActiveEpoch = lastActiveEpoch;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getDeviceState() {
        return DeviceState;
    }

    public void setDeviceState(String deviceState) {
        DeviceState = deviceState;
    }


}
