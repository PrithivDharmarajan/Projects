package com.smaat.renterblock.entity;

import java.io.Serializable;

public class UpdateViewResponse implements Serializable{
    private String photo_video_id;
    private String user_id;
    private String file_type;
    private String file;
    private String photo_description;
    private String file_order;
    private String datetime;

    public String getPhoto_video_id() {
        if (photo_video_id == null){
            photo_video_id = "";
        }
        return photo_video_id;
    }

    public void setPhoto_video_id(String photo_video_id) {
        if (photo_video_id == null){
            photo_video_id = "";
        }
        this.photo_video_id = photo_video_id;
    }

    public String getUser_id() {
        if (user_id == null){
            user_id = "";
        }
        return user_id;
    }

    public void setUser_id(String user_id) {
        if (user_id == null){
            user_id = "";
        }
        this.user_id = user_id;
    }

    public String getFile_type() {
        if (file_type == null){
            file_type = "";
        }
        return file_type;
    }

    public void setFile_type(String file_type) {
        if (file_type == null){
            file_type = "";
        }
        this.file_type = file_type;
    }

    public String getFile() {
        if (file == null){
            file = "";
        }
        return file;
    }

    public void setFile(String file) {
        if (file == null){
            file = "";
        }
        this.file = file;
    }

    public String getPhoto_description() {
        if (photo_description == null){
            photo_description  = "";
        }
        return photo_description;
    }

    public void setPhoto_description(String photo_description) {
        if (photo_description == null){
            photo_description = "";
        }
        this.photo_description = photo_description;
    }

    public String getFile_order() {
        if (file_order == null){
            file_order = "";
        }
        return file_order;
    }

    public void setFile_order(String file_order) {
        if (file_order == null){
            file_order = "";
        }
        this.file_order = file_order;
    }

    public String getDatetime() {
        if (datetime == null){
            datetime = "";
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        if (datetime == null){
            datetime = "";
        }
        this.datetime = datetime;
    }


}
