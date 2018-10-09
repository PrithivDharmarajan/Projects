package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

public class SpeedTestResponse implements Serializable {

    private String routerId = "";
    private double downloadRate = 0;
    private double uploadRate = 0;
    private boolean isFinal;
    private String dateTime = "";
    private double ping =0;

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public double getDownloadRate() {
        return downloadRate;
    }

    public void setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
    }

    public double getUploadRate() {
        return uploadRate;
    }

    public void setUploadRate(double uploadRate) {
        this.uploadRate = uploadRate;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getPing() {
        return ping;
    }

    public void setPing(double ping) {
        this.ping = ping;
    }
}
