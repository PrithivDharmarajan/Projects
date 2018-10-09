package com.smaat.renterblock.entity;


import java.util.ArrayList;

public class ScheduleListObject {

    private ArrayList<ScheduleListArray> friend_schedule;
    private ArrayList<ScheduleListArray> myschedule;
    private ArrayList<ScheduleListArray> accepted_schedule;

    public ArrayList<ScheduleListArray> getFriend_schedule() {
        return friend_schedule;
    }

    public void setFriend_schedule(ArrayList<ScheduleListArray> friend_schedule) {
        this.friend_schedule = friend_schedule;
    }

    public ArrayList<ScheduleListArray> getMyschedule() {
        return myschedule;
    }

    public void setMyschedule(ArrayList<ScheduleListArray> myschedule) {
        this.myschedule = myschedule;
    }

    public ArrayList<ScheduleListArray> getAccepted_schedule() {
        return accepted_schedule;
    }

    public void setAccepted_schedule(ArrayList<ScheduleListArray> accepted_schedule) {
        this.accepted_schedule = accepted_schedule;
    }
}
