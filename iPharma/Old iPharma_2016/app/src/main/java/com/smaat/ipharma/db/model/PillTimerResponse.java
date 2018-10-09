package com.smaat.ipharma.db.model;

import java.io.Serializable;

/**
 * Created by admin on 9/19/2016.
 */

public class PillTimerResponse implements Serializable {

    private int id;
    private String tablet_name;
    private String duration_type;
    private String duration_time;
    private String pill_timing;

    public String getPill_timing() {
        return pill_timing;
    }

    public void setPill_timing(String pill_timing) {
        this.pill_timing = pill_timing;
    }

    public String getDuration_time() {
        return duration_time;
    }

    public void setDuration_time(String duration_time) {
        this.duration_time = duration_time;
    }

    public String getDuration_type() {
        return duration_type;
    }

    public void setDuration_type(String duration_type) {
        this.duration_type = duration_type;
    }

    public String getTablet_name() {
        return tablet_name;
    }

    public void setTablet_name(String tablet_name) {
        this.tablet_name = tablet_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
