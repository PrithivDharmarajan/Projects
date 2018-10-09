package com.smaat.renterblock.entity;

import java.io.Serializable;


public class PropertyDialogEntity implements Serializable {
    private String type;
    private boolean is_selected;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean is_selected() {
        return is_selected;
    }

    public void setIs_selected(boolean is_selected) {
        this.is_selected = is_selected;
    }
}
