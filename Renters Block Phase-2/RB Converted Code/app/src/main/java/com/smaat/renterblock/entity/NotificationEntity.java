package com.smaat.renterblock.entity;

import java.io.Serializable;

public class NotificationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String notification_id;
	private String user_id;
	private String type_of_notification;
	private String type_id;
	private String message;
	private String status;

	public String getNotification_id() {
		return notification_id;
	}

	public void setNotification_id(String notification_id) {
		this.notification_id = notification_id;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getType_of_notification() {
		return type_of_notification;
	}

	public void setType_of_notification(String type_of_notification) {
		this.type_of_notification = type_of_notification;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
