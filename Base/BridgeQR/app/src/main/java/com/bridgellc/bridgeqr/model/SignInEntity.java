package com.bridgellc.bridgeqr.model;

import java.io.Serializable;

public class SignInEntity implements Serializable {


    private String user_id;
    private String first_name;
    private String last_name;
    private String email_id;
    private String phone_number;
    private String password;
    private String university_id;
    private String dob;
    private String gender;
    private String login_type;
    private String social_id;
    private String last_login_datetime;
    private String created_datetime;
    private String modified_datetime;
    private String delete_flag;
    private String university_name;
    private String is_new_user;
    private String edu_email_verify;
    private String mobile_verify;


    public String getEdu_email_verify() {
        return edu_email_verify;
    }

    public void setEdu_email_verify(String edu_email_verify) {
        this.edu_email_verify = edu_email_verify;
    }

    public String getMobile_verify() {
        return mobile_verify;
    }

    public void setMobile_verify(String mobile_verify) {
        this.mobile_verify = mobile_verify;
    }


    public String getIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(String is_new_user) {
        this.is_new_user = is_new_user;
    }


    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getModified_datetime() {
        return modified_datetime;
    }

    public void setModified_datetime(String modified_datetime) {
        this.modified_datetime = modified_datetime;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public String getLast_login_datetime() {
        return last_login_datetime;
    }

    public void setLast_login_datetime(String last_login_datetime) {
        this.last_login_datetime = last_login_datetime;
    }

    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getUniversity_id() {
        return university_id;
    }

    public void setUniversity_id(String university_id) {
        this.university_id = university_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


}
