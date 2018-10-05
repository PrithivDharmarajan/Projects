package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/21/2018.
 */

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
