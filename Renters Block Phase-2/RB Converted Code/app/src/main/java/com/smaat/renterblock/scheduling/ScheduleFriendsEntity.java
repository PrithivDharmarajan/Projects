package com.smaat.renterblock.scheduling;

import java.io.Serializable;

public class ScheduleFriendsEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private String meeting_id;
	private String scheduler_user_id;
	private String schedule_id;
	private String user_id;
	private String status;
	private String first_name;
	private String user_pic;
	private String user_name;

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getMeeting_id() {
		return meeting_id;
	}

	public void setMeeting_id(String meeting_id) {
		this.meeting_id = meeting_id;
	}

	public String getScheduler_user_id() {
		return scheduler_user_id;
	}

	public void setScheduler_user_id(String scheduler_user_id) {
		this.scheduler_user_id = scheduler_user_id;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

}
