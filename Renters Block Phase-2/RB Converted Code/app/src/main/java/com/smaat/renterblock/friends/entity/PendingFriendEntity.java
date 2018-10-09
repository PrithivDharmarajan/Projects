package com.smaat.renterblock.friends.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PendingFriendEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<FriendDetailsEntity> friends_details;

	public ArrayList<FriendDetailsEntity> getFriends_details() {
		return friends_details;
	}

	public void setFriends_details(
			ArrayList<FriendDetailsEntity> friends_details) {
		this.friends_details = friends_details;
	}
}
