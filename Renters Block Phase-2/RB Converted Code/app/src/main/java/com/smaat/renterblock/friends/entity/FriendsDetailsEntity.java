package com.smaat.renterblock.friends.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class FriendsDetailsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<AcceptFriendEntity> accept_friend;
	private ArrayList<PendingDetailsEntity> sent_pending_details;
	private String pending_count;
	private String getfriends;

	public String getGetfriends() {
		return getfriends;
	}

	public void setGetfriends(String getfriends) {
		this.getfriends = getfriends;
	}

	public ArrayList<AcceptFriendEntity> getAccept_friend() {
		return accept_friend;
	}

	public void setAccept_friend(ArrayList<AcceptFriendEntity> accept_friend) {
		this.accept_friend = accept_friend;
	}

	public ArrayList<PendingDetailsEntity> getSent_pending_details() {
		return sent_pending_details;
	}

	public void setSent_pending_details(
			ArrayList<PendingDetailsEntity> sent_pending_details) {
		this.sent_pending_details = sent_pending_details;
	}

	public String getPending_count() {
		return pending_count;
	}

	public void setPending_count(String pending_count) {
		this.pending_count = pending_count;
	}

}
