package com.bridgellc.bridge.entity;

import java.io.Serializable;

public class UniversityEntity implements Serializable {

    private String university_id;
    private String university_name;

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }
}
