package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

public class ChartDetailsEntity implements Serializable {

    private double download;
    private double upload;
    private long time;

    public double getDownload() {
        return download;
    }

    public void setDownload(double download) {
        this.download = download;
    }

    public double getUpload() {
        return upload;
    }

    public void setUpload(double upload) {
        this.upload = upload;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
