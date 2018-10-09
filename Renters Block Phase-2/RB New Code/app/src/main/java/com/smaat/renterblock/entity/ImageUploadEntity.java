package com.smaat.renterblock.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by user on 9/27/2017.
 */

public class ImageUploadEntity implements Serializable {

    private String type;
    private String filepath;
    private Bitmap bitmap;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
