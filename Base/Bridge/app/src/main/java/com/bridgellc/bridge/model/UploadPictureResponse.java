package com.bridgellc.bridge.model;


import com.bridgellc.bridge.entity.PicturePhotoEntity;

public class UploadPictureResponse extends CommonResponse {

    private PicturePhotoEntity result;

    public PicturePhotoEntity getResult() {
        return result;
    }

    public void setResult(PicturePhotoEntity result) {
        this.result = result;
    }
}
