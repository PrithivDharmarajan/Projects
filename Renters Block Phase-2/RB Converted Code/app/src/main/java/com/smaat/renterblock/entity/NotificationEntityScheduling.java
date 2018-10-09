package com.smaat.renterblock.entity;

import java.io.Serializable;

public class NotificationEntityScheduling implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String datetime;
	private String sch_id;
	private String meeting_subject;

	public String getMeeting_subject() {
		return meeting_subject;
	}

	public void setMeeting_subject(String meeting_subject) {
		this.meeting_subject = meeting_subject;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getSch_id() {
		return sch_id;
	}

	public void setSch_id(String sch_id) {
		this.sch_id = sch_id;
	}

}
