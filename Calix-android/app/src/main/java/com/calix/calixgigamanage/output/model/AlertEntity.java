package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

/**
 * Created by sibaprasad on 2/7/2018.
 */

public class AlertEntity implements Serializable {


    private String notifId="";
    private String type;
    private String text;
    private boolean isRead;
    private long created=0;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }




}
