package com.fautus.fautusapp.entity;

import com.parse.ParseObject;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.ParseAPIConstants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sys on 22-Apr-17.
 */

public class MomentPhotoEntity implements Serializable {

    private ParseObject moment;
    private ArrayList<PhotoEntity> photo;
    private String skillLevel;
    private String purchasedPhotosCount;
    private String photo_uploaded;
    private String moment_header;

    public String getMoment_header() {
        return moment_header;
    }

    public void setMoment_header(String moment_header) {
        if (moment_header == null) {
            moment_header = "";
        }
        this.moment_header = moment_header;
    }


    public String getPhoto_uploaded() {
        return photo_uploaded;
    }

    public void setPhoto_uploaded(String photo_uploaded) {
        if (photo_uploaded == null) {
            photo_uploaded = AppConstants.FAILURE_CODE;
        }
        this.photo_uploaded = photo_uploaded;
    }


    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        if (skillLevel == null) {
            skillLevel = "";
        }
        this.skillLevel = skillLevel;
    }

    public String getPurchasedPhotosCount() {
        return purchasedPhotosCount;
    }

    public void setPurchasedPhotosCount(String purchasedPhotosCount) {
        if (purchasedPhotosCount == null) {
            purchasedPhotosCount = "";
        }
        this.purchasedPhotosCount = purchasedPhotosCount;
    }


    public ParseObject getMoment() {
        return moment;
    }

    public void setMoment(ParseObject moment) {
        if (moment == null) {
            moment = new ParseObject(ParseAPIConstants.Parse_Moment);
        }
        this.moment = moment;
    }

    public ArrayList<PhotoEntity> getPhoto() {
        return photo;
    }

    public void setPhoto(ArrayList<PhotoEntity> photo) {
        if (photo == null) {
            photo = new ArrayList<>();
        }
        this.photo = photo;
    }


}
