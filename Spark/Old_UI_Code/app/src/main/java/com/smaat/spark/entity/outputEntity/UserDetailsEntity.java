package com.smaat.spark.entity.outputEntity;


import java.io.Serializable;

public class UserDetailsEntity implements Serializable {

    private String user_id;
    private String username;
    private String email_id;
    private String password;
    private String lat;
    private String lon;
    private String gender;
    private String subject;
    private String interests;
    private String main_picture;
    private String device_type;
    private String device_token;
    private String status;
    private String connected_user_id;
    private String datetime;
    private String address;
    private String private_account;
    private String push_notifications;
    private String hide_location;
    private String delete_status;
    private String friend;
    private String notification_id;
    private String sender_id;
    private String receiver_id;
    private String request_id;
    private String message;
    private String date_time;
    private String type;
    private String block_count;
    private String read_status;
    private String more_mages;
    private String invite_btn_name;
    private String anonyomous;
    private String friendimage;

    public String getFriendimage() {
        if (friendimage == null) {
            friendimage = "";
        }
        return friendimage;
    }

    public void setFriendimage(String friendimage) {
        this.friendimage = friendimage;
    }


    public String getAnonyomous() {
        if (anonyomous == null) {
            anonyomous = "";
        }
        return anonyomous;
    }

    public void setAnonyomous(String anonyomous) {
        this.anonyomous = anonyomous;
    }


    public String getInvite_btn_name() {
        if (invite_btn_name == null) {
            invite_btn_name = "";
        }
        return invite_btn_name;
    }

    public void setInvite_btn_name(String invite_btn_name) {
        this.invite_btn_name = invite_btn_name;
    }


    public String getMore_mages() {
        if (more_mages == null) {
            more_mages = "";
        }
        return more_mages;
    }

    public void setMore_mages(String more_mages) {
        this.more_mages = more_mages;
    }


    public String getRead_status() {
        if (read_status == null) {
            read_status = "";
        }
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }


    public String getFriend() {
        if (friend == null) {
            friend = "";
        }
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getDelete_status() {
        if (delete_status == null) {
            delete_status = "";
        }
        return delete_status;
    }

    public void setDelete_status(String delete_status) {
        this.delete_status = delete_status;
    }

    public String getHide_location() {
        if (hide_location == null) {
            hide_location = "";
        }
        return hide_location;
    }

    public void setHide_location(String hide_location) {
        this.hide_location = hide_location;
    }

    public String getPush_notifications() {
        if (push_notifications == null) {
            push_notifications = "";
        }
        return push_notifications;
    }

    public void setPush_notifications(String push_notifications) {
        this.push_notifications = push_notifications;
    }

    public String getPrivate_account() {
        if (private_account == null) {
            private_account = "";
        }
        return private_account;
    }

    public void setPrivate_account(String private_account) {
        this.private_account = private_account;
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


    public String getDatetime() {
        if (datetime == null) {
            datetime = "";
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getConnected_user_id() {
        if (connected_user_id == null) {
            connected_user_id = "";
        }
        return connected_user_id;
    }

    public void setConnected_user_id(String connected_user_id) {
        this.connected_user_id = connected_user_id;
    }

    public String getStatus() {
        if (status == null) {
            status = "";
        }
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDevice_token() {
        if (device_token == null) {
            device_token = "";
        }
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_type() {
        if (device_type == null) {
            device_type = "";
        }
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getMain_picture() {
        if (main_picture == null) {
            main_picture = "";
        }
        return main_picture;
    }

    public void setMain_picture(String main_picture) {
        this.main_picture = main_picture;
    }

    public String getInterests() {
        if (interests == null) {
            interests = "";
        }
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getSubject() {
        if (subject == null) {
            subject = "";
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGender() {
        if (gender == null) {
            gender = "";
        }
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getPassword() {
        if (password == null) {
            password = "";
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getLon() {
        if (lon == null) {
            lon = "";
        }
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        if (lat == null) {
            lat = "";
        }
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getEmail_id() {
        if (email_id == null) {
            email_id = "";
        }
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getUsername() {
        if (username == null) {
            username = "";
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getBlock_count() {
        if (block_count == null) {
            block_count = "";
        }
        return block_count;
    }

    public void setBlock_count(String block_count) {
        this.block_count = block_count;
    }

    public String getType() {
        if (type == null) {
            type = "";
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate_time() {
        if (date_time == null) {
            date_time = "";
        }
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequest_id() {
        if (request_id == null) {
            request_id = "";
        }
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getReceiver_id() {
        if (receiver_id == null) {
            receiver_id = "";
        }
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        if (sender_id == null) {
            sender_id = "";
        }
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getNotification_id() {
        if (notification_id == null) {
            notification_id = "";
        }
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

}
