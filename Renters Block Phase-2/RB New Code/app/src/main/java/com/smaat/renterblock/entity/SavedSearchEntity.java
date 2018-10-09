package com.smaat.renterblock.entity;

import java.io.Serializable;

public class SavedSearchEntity implements Serializable {

    private String save_search_id;
    private String user_id;
    private String filter_object;
    private String filter_type;
    private String date;
    private String email_notification;
    private String inquiry;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getSave_search_id() {
        return save_search_id;
    }

    public void setSave_search_id(String save_search_id) {
        this.save_search_id = save_search_id;
    }

    public String getUser_id() {
        if (user_id == null) {
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFilter_object() {
        if (filter_object == null) {
            filter_object = "";
        }
        return filter_object;
    }

//    public FilterEntity getFilter_object1() {
//        return new Gson().fromJson(filter_object, FilterEntity.class);
//    }

    public void setFilter_object(String filter_object) {
        this.filter_object = filter_object;
    }

    public String getFilter_type() {
        if (filter_type == null) {
            filter_type = "";
        }
        return filter_type;
    }

    public void setFilter_type(String filter_type) {
        this.filter_type = filter_type;
    }

    public String getDate() {

        if (date == null) {
            date = "";
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail_notification() {
        if (email_notification == null) {
            email_notification = "";
        }
        return email_notification;
    }

    public void setEmail_notification(String email_notification) {
        this.email_notification = email_notification;
    }

    public String getInquiry() {

        if (inquiry == null) {
            inquiry = "";
        }
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }
}
