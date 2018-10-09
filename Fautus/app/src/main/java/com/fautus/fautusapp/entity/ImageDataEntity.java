package com.fautus.fautusapp.entity;

import android.graphics.Bitmap;

import com.fautus.fautusapp.utils.AppConstants;

/**
 * Created by sys on 6/1/2017.
 */

public class ImageDataEntity {

    private Bitmap mThumbImageList;
    private String mSelCheckbox = "";

    public Bitmap getmThumbImageList() {
        return mThumbImageList;
    }

    public void setmThumbImageList(Bitmap mThumbImageList) {
        this.mThumbImageList = mThumbImageList;
    }

    public String getmSelCheckbox() {
        if (mSelCheckbox == null) {
            mSelCheckbox = AppConstants.FAILURE_CODE;
        }
        return mSelCheckbox;
    }

    public void setmSelCheckbox(String mSelCheckbox) {
        this.mSelCheckbox = mSelCheckbox;
    }

}
