package com.smaat.renterblock.friends.entity;

import java.io.Serializable;

public class ChatEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String groupchat_id;
	private String group_id;
	private String user_id;
	private String message;
	private String file;
	private String file_type;
	private String read;
	private String type;
	private String send_time;
	private String username;
	private boolean isLocal;
	private String hotleadsmessage;

	public String getHotleadsmessage() {
		return hotleadsmessage;
	}

	public void setHotleadsmessage(String hotleadsmessage) {
		this.hotleadsmessage = hotleadsmessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSend_time() {
		return send_time;
	}

	public void setSend_time(String send_time) {
		this.send_time = send_time;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getGroupchat_id() {
		return groupchat_id;
	}

	public void setGroupchat_id(String groupchat_id) {
		this.groupchat_id = groupchat_id;
	}

	public String getRead() {
		return read;
	}

	public void setRead(String read) {
		this.read = read;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isLocal() {
		return isLocal;
	}

	public void setLocal(boolean isLocal) {
		this.isLocal = isLocal;
	}

}
