package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class MarkerEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<MarkerID> mapID;

	public ArrayList<MarkerID> getMapID() {
		return mapID;
	}

	public void setMapID(ArrayList<MarkerID> mapID) {
		this.mapID = mapID;
	}

}
