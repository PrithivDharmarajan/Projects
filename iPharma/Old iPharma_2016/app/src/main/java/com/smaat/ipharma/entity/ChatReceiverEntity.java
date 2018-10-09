package com.smaat.ipharma.entity;

import java.io.Serializable;

public class ChatReceiverEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String MessageID;
	private String SenderType;
	private String SenderID;
	private String ReceiverID;
	private String Message;
	private String MessageDateTime;
	private String AdminReadReceipt;
	
	public String getMessageID() {
		return MessageID;
	}
	public void setMessageID(String messageID) {
		MessageID = messageID;
	}
	public String getSenderType() {
		return SenderType;
	}
	public void setSenderType(String senderType) {
		SenderType = senderType;
	}
	public String getSenderID() {
		return SenderID;
	}
	public void setSenderID(String senderID) {
		SenderID = senderID;
	}
	public String getReceiverID() {
		return ReceiverID;
	}
	public void setReceiverID(String receiverID) {
		ReceiverID = receiverID;
	}
	public String getMessage() {
		return Message;
	}
	public void setMessage(String message) {
		Message = message;
	}
	public String getMessageDateTime() {
		return MessageDateTime;
	}
	public void setMessageDateTime(String messageDateTime) {
		MessageDateTime = messageDateTime;
	}
	public String getAdminReadReceipt() {
		return AdminReadReceipt;
	}
	public void setAdminReadReceipt(String adminReadReceipt) {
		AdminReadReceipt = adminReadReceipt;
	}

}
