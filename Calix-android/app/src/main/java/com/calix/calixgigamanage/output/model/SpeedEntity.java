package com.calix.calixgigamanage.output.model;


import java.io.Serializable;

public class SpeedEntity implements Serializable {

    private double download = 0;
    private double upload = 0;
    private double ping = 0;
    private boolean isAnimationBool = false;

    public boolean isAnimationBool() {
        return isAnimationBool;
    }

    public void setAnimationBool(boolean animationBool) {
        isAnimationBool = animationBool;
    }

    public String getPing() {
        return String.valueOf(ping);
    }

    public void setPing(double ping) {
        this.ping = ping;
    }

    public String getDownload() {
        return String.valueOf(download);
    }

    public void setDownload(double download) {
        this.download = download;
    }

    public String getUpload() {
        return String.valueOf(upload);
    }

    public void setUpload(double upload) {
        this.upload = upload;
    }


}
