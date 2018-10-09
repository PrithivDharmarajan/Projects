package com.smaat.renterblock.entity;

import java.io.Serializable;

public class UserPropertyPicsEntity implements Serializable {

    private String photo_video_id;
    private String user_id;
    private String property_id;
    private String file_type;
    private String file;
    private String description;
    private String datetime;
    private String first_name;
    private String user_pic;
    private String user_name;
    private String friends;
    private String Review;
    private String Photos;
    private String picture_id;
    private String property_name;
    private String property_type;
    private String rb_block;
    private String open_house;
    private String address;

    public String getPicture_id() {
        if (picture_id == null) {
            picture_id = "";
        }
        return picture_id;
    }

    public void setPicture_id(String picture_id) {
        this.picture_id = picture_id;
    }

    public String getProperty_name() {
        if (property_name == null) {
            property_name = "";
        }
        return property_name;
    }

    public void setProperty_name(String property_name) {
        this.property_name = property_name;
    }

    public String getProperty_type() {
        if (property_type == null) {
            property_type = "";
        }
        return property_type;
    }

    public void setProperty_type(String property_type) {
        this.property_type = property_type;
    }

    public String getRb_block() {
        if (rb_block == null) {
            rb_block = "";
        }
        return rb_block;
    }

    public void setRb_block(String rb_block) {
        this.rb_block = rb_block;
    }

    public String getOpen_house() {
        if (open_house == null) {
            open_house = "";
        }
        return open_house;
    }

    public void setOpen_house(String open_house) {
        this.open_house = open_house;
    }

    public String getAddress() {
        if (address == null) {
            address = "";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getPhoto_video_id() {
        if (photo_video_id == null) {
            photo_video_id = "";
        }
        return photo_video_id;
    }

    public void setPhoto_video_id(String photo_video_id) {
        this.photo_video_id = photo_video_id;
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

    public String getProperty_id() {
        if (property_id == null) {
            property_id = "";
        }
        return property_id;
    }

    public void setProperty_id(String property_id) {
        this.property_id = property_id;
    }

    public String getFile_type() {
        if (file_type == null) {
            file_type = "";
        }
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile() {
        if (file == null) {
            file = "";
        }
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDescription() {
        if (description == null) {
            description = "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        if (datetime == null) {
            datetime = "";
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFirst_name() {
        if (first_name == null) {
            first_name = "";
        }
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUser_pic() {
        if (user_pic == null) {
            user_pic = "";
        }
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUser_name() {
        if (user_name == null) {
            user_name = "";
        }
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFriends() {
        if (friends == null) {
            friends = "";
        }
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getReview() {
        if (Review == null) {
            Review = "";
        }
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public String getPhotos() {
        if (Photos == null) {
            Photos = "";
        }
        return Photos;
    }

    public void setPhotos(String photos) {
        Photos = photos;
    }
}
