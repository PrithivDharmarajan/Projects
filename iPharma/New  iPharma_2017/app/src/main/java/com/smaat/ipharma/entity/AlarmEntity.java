package com.smaat.ipharma.entity;

/**
 * Created by admin on 2/8/2017.
 */

public class AlarmEntity {

    public int getAlarm_image() {
        return alarm_image;
    }

    public void setAlarm_image(int alarm_image) {
        this.alarm_image = alarm_image;
    }

    public String getAlarm_string() {
        return alarm_string;
    }

    public void setAlarm_string(String alarm_string) {
        this.alarm_string = alarm_string;
    }

    public String getAlarm_time() {
        return alarm_time;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public int alarm_image;
    public String alarm_string;
    public String alarm_time;
    public boolean IsChecked;

    public int getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(int uniqueId) {
        UniqueId = uniqueId;
    }

    public int  UniqueId;

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }
}
