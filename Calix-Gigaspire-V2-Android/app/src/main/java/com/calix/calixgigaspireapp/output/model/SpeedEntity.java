package com.calix.calixgigaspireapp.output.model;

public class SpeedEntity {

    private double download;
    private double upload;
    private double ping;
    private boolean isAnimationBool = false;

    public boolean isAnimationBool() {
        return isAnimationBool;
    }

    public void setAnimationBool(boolean animationBool) {
        isAnimationBool = animationBool;
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

    public String getPing() {
        return String.valueOf(ping);
    }

    public void setPing(double ping) {
        this.ping = ping;
    }
}
