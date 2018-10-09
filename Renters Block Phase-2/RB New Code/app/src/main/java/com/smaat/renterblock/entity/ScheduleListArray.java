package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleListArray implements Serializable{

    private String schedule_id;
    private String scheduler_user_id;
    private String meeting_subject;
    private String description;
    private String date;
    private String time;
    private String venue;
    private String status;
    private String isonline;
    private String first_name;
    private String user_pic;
    private String meeting_friends_id;
    private String user_id;
    private String user_name;
    private String is_my_schedule;
    private String is_friends_schedule;
    private String is_Accepted_schedule;
    private ArrayList<ScheduleListFriendsArray> friends;
    private boolean isSelected = false;

    public String getIs_friends_schedule() {
        if (is_friends_schedule == null){
            is_friends_schedule = "";
        }
        return is_friends_schedule;
    }

    public void setIs_friends_schedule(String is_friends_schedule) {
        this.is_friends_schedule = is_friends_schedule;
    }



    public String getIs_my_schedule() {
        if (is_my_schedule == null){
            is_my_schedule = "";
        }
        return is_my_schedule;
    }

    public void setIs_my_schedule(String is_my_schedule) {
        this.is_my_schedule = is_my_schedule;
    }

    public String getIs_Accepted_schedule() {
        if (is_Accepted_schedule == null){
            is_Accepted_schedule = "";
        }
        return is_Accepted_schedule;
    }

    public void setIs_Accepted_schedule(String is_Accepted_schedule) {
        this.is_Accepted_schedule = is_Accepted_schedule;
    }



    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSchedule_id() {
        if (schedule_id == null){
            schedule_id = "";
        }
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getScheduler_user_id() {
        if (scheduler_user_id == null){
            scheduler_user_id = "";
        }
        return scheduler_user_id;
    }

    public void setScheduler_user_id(String schedule_user_id) {
        this.scheduler_user_id = schedule_user_id;
    }

    public String getMeeting_subject() {
        if (meeting_subject == null){
            meeting_subject = "";
        }
        return meeting_subject;
    }

    public void setMeeting_subject(String meeting_subject) {
        this.meeting_subject = meeting_subject;
    }

    public String getDescription() {
        if (description == null){
            description = "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        if (date == null){
            date = "";
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        if (time == null){
            time = "";
        }
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVenue() {
        if (venue == null){
            venue = "";
        }
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getStatus() {
        if (status == null){
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsonline() {
        if (isonline == null){
            isonline = "";
        }
        return isonline;
    }

    public void setIsonline(String isonline) {
        this.isonline = isonline;
    }

    public String getFirst_name() {
        if (first_name == null){
            first_name = "";
        }
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_pic() {
        if (user_pic == null){
            user_pic = "";
        }
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getMeeting_friends_id() {
        if (meeting_friends_id == null){
            meeting_friends_id = "";
        }
        return meeting_friends_id;
    }

    public void setMeeting_friends_id(String meeting_friends_id) {
        this.meeting_friends_id = meeting_friends_id;
    }

    public String getUser_id() {
        if (user_id == null){
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        if (user_name == null){
            user_name = "";
        }
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public ArrayList<ScheduleListFriendsArray> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<ScheduleListFriendsArray> friends) {
        this.friends = friends;
    }
}
