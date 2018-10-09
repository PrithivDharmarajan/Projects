package com.smaat.spark.entity.outputEntity;


import com.smaat.spark.utils.AppConstants;

import java.io.Serializable;

public class ChatReceiveEntity implements Serializable {

    private String chat_id;
    private String user_id;
    private String friend_id;
    private String message;
    private String subject;
    private String datetime;
    private String datetimeVisible;
    private String username;
    private String friendname;
    private String friendimage;
    private String friend;
    private String video_id;
    private String more_mages;
    private String message_type;
    private String video_img;
    private String friend_online_status;

    public String getMsg_sent_user() {
        if (msg_sent_user == null) {
            msg_sent_user = "";
        }
        return msg_sent_user;
    }

    public void setMsg_sent_user(String msg_sent_user) {
        this.msg_sent_user = msg_sent_user;
    }

    private String msg_sent_user;


    public String getUserimage() {
        if (userimage == null) {
            userimage = "";
        }
        return userimage;
    }

    public String getDatetimeVisible() {
        if (datetimeVisible == null) {
            datetimeVisible = AppConstants.FAILURE_CODE;
        }
        return datetimeVisible;
    }

    public void setDatetimeVisible(String datetimeVisible) {
        this.datetimeVisible = datetimeVisible;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    private String userimage;


    public String getFriend_online_status() {
        if (friend_online_status == null) {
            friend_online_status = "";
        }
        return friend_online_status;
    }

    public void setFriend_online_status(String friend_online_status) {
        this.friend_online_status = friend_online_status;
    }


    public String getVideo_img() {
        if (video_img == null) {
            video_img = "";
        }
        return video_img;
    }

    public void setVideo_img(String video_img) {
        this.video_img = video_img;
    }


    public String getMessage_type() {
        if (message_type == null) {
            message_type = "";
        }
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }


    public String getVideo_id() {
        if (video_id == null) {
            video_id = "";
        }
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
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

    public String getFriend() {
        if (friend == null) {
            friend = "";
        }
        return friend;
    }

    public void setFriend(String friend) {

        this.friend = friend;
    }

    public String getFriendimage() {
        if (friendimage == null) {
            friendimage = "";
        }
        return friendimage;
    }

    public void setFriendimage(String friendimage) {
        this.friendimage = friendimage;
    }


    public String getFriendname() {
        if (friendname == null) {
            friendname = "";
        }
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
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

    public String getDatetime() {
        if (datetime == null) {
            datetime = "";
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
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

    public String getMessage() {
        if (message == null) {
            message = "";
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFriend_id() {
        if (friend_id == null) {
            friend_id = "";
        }
        return friend_id;
    }

    public void setFriend_id(String friend_id) {
        this.friend_id = friend_id;
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

    public String getChat_id() {
        if (chat_id == null) {
            chat_id = "";
        }
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

}
