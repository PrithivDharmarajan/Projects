package com.smaat.renterblock.entity;

import java.io.Serializable;

public class ChatEntity implements Serializable {


    private String groupchat_id;
    private String group_id;
    private String schedule_id;
    private String user_id;
    private String message;
    private String file;
    private String file_type;
    private String read;
    private String type;
    private String send_time;
    private String hotleadsmessage;
    private String user_name;
    private String username;
    private String userimage;

    public String getGroupchat_id() {
        return groupchat_id == null ? "" : groupchat_id;
    }

    public void setGroupchat_id(String groupchat_id) {
        this.groupchat_id = groupchat_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getSchedule_id() {
        return schedule_id == null ? "" : schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getUser_id() {
        return user_id == null ? "" : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file == null ? "" : file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFile_type() {
        return file_type == null ? "" : file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getRead() {
        return read == null ? "" : read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSend_time() {
        return send_time == null ? "" : send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getHotleadsmessage() {
        return hotleadsmessage == null ? "" : hotleadsmessage;
    }

    public void setHotleadsmessage(String hotleadsmessage) {
        this.hotleadsmessage = hotleadsmessage;
    }

    public String getUser_name() {
        return user_name == null ? "" : user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUsername() {
        return username == null ? "" : username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage == null ? "" : userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

}
