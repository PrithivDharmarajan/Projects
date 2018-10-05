package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 3/5/2018.
 */

public class IOTRemoveDeviceEntity implements Serializable {

    private String ID;
    private String Name;
    private String FriendlyDeviceType;
    private String Type;
    private String Location;
    private String LastActiveEpoch;
    private String Model;
    private String Version;
    private String Manufacturer;
    private String DeviceState;
    private String DeviceONOFFState;

    public String getDeviceONOFFState() {
        return DeviceONOFFState == null ? "false" : DeviceONOFFState;
    }

    public void setDeviceONOFFState(String deviceONOFFState) {
        DeviceONOFFState = deviceONOFFState;
    }


    public String getID() {
        return ID == null ? "" : ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name == null ? "" : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFriendlyDeviceType() {
        return FriendlyDeviceType == null ? "" : FriendlyDeviceType;
    }

    public void setFriendlyDeviceType(String friendlyDeviceType) {
        FriendlyDeviceType = friendlyDeviceType;
    }

    public String getType() {
        return Type == null ? "" : Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getLocation() {
        return Location == null ? "" : Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLastActiveEpoch() {
        return LastActiveEpoch == null ? "" : LastActiveEpoch;
    }

    public void setLastActiveEpoch(String lastActiveEpoch) {
        LastActiveEpoch = lastActiveEpoch;
    }

    public String getModel() {
        return Model == null ? "" : Model;
    }

    public void setModel(String model) {
        Model = model;
    }

    public String getVersion() {
        return Version == null ? "" : Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getManufacturer() {
        return Manufacturer == null ? "" : Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getDeviceState() {
        return DeviceState == null ? "" : DeviceState;
    }

    public void setDeviceState(String deviceState) {
        DeviceState = deviceState;
    }


}
