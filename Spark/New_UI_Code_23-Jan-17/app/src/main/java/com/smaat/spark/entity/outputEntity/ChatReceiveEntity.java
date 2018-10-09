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
    private String header_datetimeVisible;
    private String datetimeVisible;
    private String header_datetime;
    private String username;
    private String friendname;
    private String friendimage;
    private String friend;
    private String video_id;
    private String video_img;
    private String more_mages;
        private String message_type;
    private String friend_online_status;
    private String unread_messages;
    private String user_connect_anonymous;
    private String friend_connect_anonymous;
    private String friend_address;
    private String friend_interests;
    private String friend_location;
    private String header_key;
    private int header_id;
    private String is_attachement;
    private String attachement_url;

    private String attachement_title;
    private String read_status;
    private String message_flow;
    private String delete_status;


    public String getIs_attachement() {
        if (is_attachement == null) {
            is_attachement = AppConstants.FAILURE_CODE;
        }
        return is_attachement;
    }

    public void setIs_attachement(String is_attachement) {
        this.is_attachement = is_attachement;
    }

    public String getAttachement_url() {
        if (attachement_url == null) {
            attachement_url = AppConstants.FAILURE_CODE;
        }
        return attachement_url;
    }

    public void setAttachement_url(String attachement_url) {
        this.attachement_url = attachement_url;
    }

    public String getRead_status() {
        if (read_status == null) {
            read_status = "";
        }
        return read_status;
    }

    public String getAttachement_title() {
        if (attachement_title == null) {
            attachement_title = "";
        }
        return attachement_title;
    }

    public void setAttachement_title(String attachement_title) {
        this.attachement_title = attachement_title;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getMessage_flow() {
        if (message_flow == null) {
            message_flow = "";
        }
        return message_flow;
    }

    public void setMessage_flow(String message_flow) {
        this.message_flow = message_flow;
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


    public String getHeader_datetimeVisible() {
        if (header_datetimeVisible == null) {
            header_datetimeVisible = "";
        }
        return header_datetimeVisible;
    }

    public void setHeader_datetimeVisible(String header_datetimeVisible) {
        this.header_datetimeVisible = header_datetimeVisible;
    }


    public int getHeader_id() {
        return header_id;
    }

    public void setHeader_id(int headId) {
        this.header_id = headId;
    }


    public String getHeader_datetime() {
        if (header_datetime == null) {
            header_datetime = "";
        }
        return header_datetime;
    }

    public void setHeader_datetime(String header_datetime) {
        this.header_datetime = header_datetime;
    }

    public String getDatetimeVisible() {
        if (datetimeVisible == null) {
            datetimeVisible = "";
        }
        return datetimeVisible;
    }

    public void setDatetimeVisible(String datetimeVisible) {
        this.datetimeVisible = datetimeVisible;
    }

    public String getHeader_key() {
        if (header_key == null) {
            header_key = "";
        }
        return header_key;
    }

    public void setHeader_key(String header_key) {
        this.header_key = header_key;
    }


    public String getFriend_location() {
        if (friend_location == null) {
            friend_location = AppConstants.FAILURE_CODE;
        }
        return friend_location;
    }

    public void setFriend_location(String friend_location) {
        this.friend_location = friend_location;
    }

    public String getFriend_interests() {
        if (friend_interests == null) {
            friend_interests = "";
        }
        return friend_interests;
    }

    public void setFriend_interests(String friend_interests) {
        this.friend_interests = friend_interests;
    }

    public String getFriend_address() {
        if (friend_address == null) {
            friend_address = "";
        }
        return friend_address;
    }

    public void setFriend_address(String friend_address) {
        this.friend_address = friend_address;
    }


    public String getFriend_connect_anonymous() {
        if (friend_connect_anonymous == null) {
            friend_connect_anonymous = AppConstants.FAILURE_CODE;
        }
        return friend_connect_anonymous;
    }

    public void setFriend_connect_anonymous(String friend_connect_anonymous) {
        this.friend_connect_anonymous = friend_connect_anonymous;
    }


    public String getUser_connect_anonymous() {
        if (user_connect_anonymous == null) {
            user_connect_anonymous = AppConstants.FAILURE_CODE;
        }
        return user_connect_anonymous;
    }

    public void setUser_connect_anonymous(String user_connect_anonymous) {
        this.user_connect_anonymous = user_connect_anonymous;
    }


    public String getUnread_messages() {
        if (unread_messages == null) {
            unread_messages = AppConstants.FAILURE_CODE;
        }
        return unread_messages;
    }

    public void setUnread_messages(String unread_messages) {
        this.unread_messages = unread_messages;
    }


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
