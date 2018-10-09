package com.smaat.ipharma.entity;

import java.util.ArrayList;

/**
 * Created by Smaat on 1/27/2017.
 */

public class LocalTimeEntitiy  {

    private String title;
    private String morningtime;
    private String afternoontime;
    private String eveningtime;
    private String nighttime;
    private int Alarmuniqueid;
    private ArrayList<Integer> unique_id;

    public ArrayList<Integer> getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(ArrayList<Integer> unique_id) {
        this.unique_id = unique_id;
    }



    public int getAlarmuniqueid() {
        return Alarmuniqueid;
    }

    public void setAlarmuniqueid(int alarmuniqueid) {
        Alarmuniqueid = alarmuniqueid;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMorningtime() {
        return morningtime;
    }

    public void setMorningtime(String morningtime) {
        this.morningtime = morningtime;
    }

    public String getAfternoontime() {
        return afternoontime;
    }

    public void setAfternoontime(String afternoontime) {
        this.afternoontime = afternoontime;
    }

    public String getEveningtime() {
        return eveningtime;
    }

    public void setEveningtime(String eveningtime) {
        this.eveningtime = eveningtime;
    }

    public String getNighttime() {
        return nighttime;
    }

    public void setNighttime(String nighttime) {
        this.nighttime = nighttime;
    }

}
