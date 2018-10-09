package com.fautus.fautusapp.entity;

import java.io.Serializable;

/**
 * Created by sys on 02-Jun-17.
 */

public class ConChatDetailsEntity implements Serializable {

    private String momentObjIdStr;
    private String momentAdHocCodeStr;
    private String photographerObjIdStr;


    public String getMomentObjIdStr() {
        return momentObjIdStr;
    }

    public void setMomentObjIdStr(String momentObjIdStr) {
        this.momentObjIdStr = momentObjIdStr;
    }

    public String getMomentAdHocCodeStr() {
        return momentAdHocCodeStr;
    }

    public void setMomentAdHocCodeStr(String momentAdHocCodeStr) {
        this.momentAdHocCodeStr = momentAdHocCodeStr;
    }

    public String getPhotographerObjIdStr() {
        return photographerObjIdStr;
    }

    public void setPhotographerObjIdStr(String photographerObjIdStr) {
        this.photographerObjIdStr = photographerObjIdStr;
    }


}
