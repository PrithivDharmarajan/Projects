package com.smaat.renterblock.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class AcceptFriendEntity implements Serializable {

	
	private ArrayList<FriendDetailsEntity> friends_details;

	public ArrayList<FriendDetailsEntity> getFriends_details() {
		return friends_details==null?new ArrayList<FriendDetailsEntity>():friends_details;
	}

	public void setFriends_details(ArrayList<FriendDetailsEntity> friends_details) {
		this.friends_details = friends_details;
	}

}
