package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 1/24/2018.
 */

public class SpeedTestResponse implements Serializable {

    private String routerId = "";
    private double downloadRate = 0;
    private double uploadRate = 0;
    private double ping = 0;
    private boolean isFinal;

    public double getPing() {
        return ping;
    }

    public void setPing(double ping) {
        this.ping = ping;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getDownloadRate() {
        return String.valueOf(downloadRate);
    }

    public void setDownloadRate(double downloadRate) {
        this.downloadRate = downloadRate;
    }

    public String getUploadRate() {
        return String.valueOf(uploadRate);
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


}
