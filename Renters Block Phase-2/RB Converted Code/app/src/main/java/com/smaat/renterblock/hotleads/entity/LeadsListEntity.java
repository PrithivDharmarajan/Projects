package com.smaat.renterblock.hotleads.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class LeadsListEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<LeadsActiveEntity> active;
	private ArrayList<LeadsActiveEntity> passive;

	public ArrayList<LeadsActiveEntity> getPassive() {
		return passive;
	}

	public void setPassive(ArrayList<LeadsActiveEntity> passive) {
		this.passive = passive;
	}

	public ArrayList<LeadsActiveEntity> getActive() {
		return active;
	}

	public void setActive(ArrayList<LeadsActiveEntity> active) {
		this.active = active;
	}

}
