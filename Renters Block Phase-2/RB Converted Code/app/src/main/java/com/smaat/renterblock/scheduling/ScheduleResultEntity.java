package com.smaat.renterblock.scheduling;

import java.io.Serializable;
import java.util.ArrayList;

public class ScheduleResultEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<ScheduleListEntity> friend_schedule;
	private ArrayList<ScheduleListEntity> myschedule;
	private ArrayList<ScheduleListEntity> accepted_schedule;
	public ArrayList<ScheduleListEntity> getFriend_schedule() {
		return friend_schedule;
	}
	public void setFriend_schedule(ArrayList<ScheduleListEntity> friend_schedule) {
		this.friend_schedule = friend_schedule;
	}
	public ArrayList<ScheduleListEntity> getMyschedule() {
		return myschedule;
	}
	public void setMyschedule(ArrayList<ScheduleListEntity> myschedule) {
		this.myschedule = myschedule;
	}
	public ArrayList<ScheduleListEntity> getAccepted_schedule() {
		return accepted_schedule;
	}
	public void setAccepted_schedule(ArrayList<ScheduleListEntity> accepted_schedule) {
		this.accepted_schedule = accepted_schedule;
	}
	
	
}
