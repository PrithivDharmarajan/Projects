package com.smaat.renterblock.entity;

import java.io.Serializable;

/**
 * Created by Prithiv on 10/31/2017.
 */

public class ChatInputEntity implements Serializable {


    private String user_id;
    private String schedule_id;
    private String message;
    private String file_type;
    private String chat_file;
    private String imagename;
    private String type;
    private String file_format;
    private String ownner_id;
    private String friend_id;
    private String group_name;


    public String getOwnner_id() {
        return ownner_id==null?"":ownner_id;
    }

    public void setOwnner_id(String ownner_id) {
        this.ownner_id = ownner_id;
    }

    public String getFriend_id() {
        return friend_id==null?"":friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
    }


    public String getUser_id() {
        return user_id == null ? "" : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSchedule_id() {
        return schedule_id == null ? "" : schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile_type() {
        return file_type == null ? "" : file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getChat_file() {
        return chat_file == null ? "" : chat_file;
    }

    public void setChat_file(String chat_file) {
        this.chat_file = chat_file;
    }

    public String getImagename() {
        return imagename == null ? "" : imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFile_format() {
        return file_format == null ? "" : file_format;
    }

    public void setFile_format(String file_format) {
        this.file_format = file_format;
    }

    public String getGroup_name() {
        return group_name==null?"":group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
