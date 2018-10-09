package com.smaat.renterblock.friends.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendsRecentsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

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
	private String groupname;
	private String meeting_subject;
	private ArrayList<FriendsRecentUserDetailsEntity> userdetails;
	private String hotleadsmessage;
	private String user_name;
	
	public String getHotleadsmessage() {
		return hotleadsmessage;
	}

	public void setHotleadsmessage(String hotleadsmessage) {
		this.hotleadsmessage = hotleadsmessage;
	}
	
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
	public String getMeeting_subject() {
		return meeting_subject;
	}

	public void setMeeting_subject(String meeting_subject) {
		this.meeting_subject = meeting_subject;
	}

	public String getGroupchat_id() {
		return groupchat_id;
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
		return schedule_id;
	}

	public void setSchedule_id(String schedule_id) {
		this.schedule_id = schedule_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}

	public ArrayList<FriendsRecentUserDetailsEntity> getUserdetails() {
		return userdetails;
	}

	public void setUserdetails(
			ArrayList<FriendsRecentUserDetailsEntity> userdetails) {
		this.userdetails = userdetails;
	}

}
