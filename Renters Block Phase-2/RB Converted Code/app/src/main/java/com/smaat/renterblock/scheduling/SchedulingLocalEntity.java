package com.smaat.renterblock.scheduling;

import java.io.Serializable;

public class SchedulingLocalEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String schedulr_user_id;
	private String schedule_user_name;
	private String is_Selected;
	public String getSchedulr_user_id() {
		return schedulr_user_id;
	}
	public void setSchedulr_user_id(String schedulr_user_id) {
		this.schedulr_user_id = schedulr_user_id;
	}
	public String getSchedule_user_name() {
		return schedule_user_name;
	}
	public void setSchedule_user_name(String schedule_user_name) {
		this.schedule_user_name = schedule_user_name;
	}
	public String getIs_Selected() {
		return is_Selected;
	}
	public void setIs_Selected(String is_Selected) {
		this.is_Selected = is_Selected;
	}
}
