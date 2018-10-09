package com.calix.calixgigaspireapp.input.model;

import java.io.Serializable;

public class ScannerInputModel implements Serializable {
    private int id;
    private String mac_Address;
    private String serial_Number;
    private String scanned_details;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac_Address() {
        return mac_Address == null ? "" : mac_Address;
    }

    public void setMac_Address(String mac_Address) {
        this.mac_Address = mac_Address;
    }

    public String getSerial_Number() {
        return serial_Number == null ? "" : serial_Number;
    }

    public void setSerial_Number(String serial_Number) {
        this.serial_Number = serial_Number;
    }

    public String getScanned_details() {
        return scanned_details == null ? "" : scanned_details;
    }

    public void setScanned_details(String scanned_details) {
        this.scanned_details = scanned_details;
    }


}
