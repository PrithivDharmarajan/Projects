package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/7/2018.
 */

public class DurationEntity implements Serializable {

    private long startTime;
    private long endTime;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
