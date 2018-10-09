package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;


public class LeadsViewEntity implements Serializable {
    private ArrayList<LeadsEntity>active;
    private ArrayList<LeadsEntity>passive;

    public ArrayList<LeadsEntity> getActive() {
        return active;
    }

    public void setActive(ArrayList<LeadsEntity> active) {
        this.active = active;
    }

    public ArrayList<LeadsEntity> getPassive() {
        return passive;
    }

    public void setPassive(ArrayList<LeadsEntity> passive) {
        this.passive = passive;
    }
}
