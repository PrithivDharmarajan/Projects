package com.fautus.fautusapp.entity;

import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by sys on 22-Apr-17.
 */

public class PhotoEntity implements Serializable {


    private ParseObject photoObj;
    private ParseFile photo;
    private String photo_path;
    private String photo_purchased;
    private String photo_selected;


    public ParseObject getPhotoObj() {
        return photoObj;
    }

    public void setPhotoObj(ParseObject photoObj) {
        if (photoObj == null) {
            photoObj = new ParseObject(ParseAPIConstants.photo);
        }
        this.photoObj = photoObj;
    }
    public String getPhoto_path() {
        return photo_path;
    }

    public void setPhoto_path(String photo_path) {
        if (photo_path == null) {
            photo_path = AppConstants.FAILURE_CODE;
        }
        this.photo_path = photo_path;
    }

    public String getPhoto_purchased() {
        return photo_purchased;
    }

    public void setPhoto_purchased(String photo_purchased) {
        if (photo_purchased == null) {
            photo_purchased = AppConstants.FAILURE_CODE;
        }
        this.photo_purchased = photo_purchased;
    }

    public String getPhoto_selected() {
        return photo_selected;
    }

    public void setPhoto_selected(String photo_selected) {
        if (photo_selected == null) {
            photo_selected = AppConstants.SUCCESS_CODE;
        }
        this.photo_selected = photo_selected;
    }

    public ParseFile getPhoto() {
        return photo;
    }

    public void setPhoto(ParseFile photo) {
        this.photo = photo;
    }


}
