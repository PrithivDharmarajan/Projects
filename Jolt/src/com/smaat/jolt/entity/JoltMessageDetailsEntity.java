package com.smaat.jolt.entity;

public class JoltMessageDetailsEntity {

	private String MessageId;
	private String MessageText;
	private String ConversationID;
	private String UserID;
	private String MessageDateTime;
	private String Name;
	private String UserImageUrl;
	
	
	public String getMessageId() {
		return MessageId;
	}

	public void setMessageId(String messageId) {
		MessageId = messageId;
	}
	
	public String getMessageText() {
		return MessageText;
	}

	public void setMessageText(String messageText) {
		MessageText = messageText;
	}

	public String getConversationID() {
		return ConversationID;
	}

	public void setConversationID(String conversationID) {
		ConversationID = conversationID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getMessageDateTime() {
		return MessageDateTime;
	}

	public void setMessageDateTime(String messageDateTime) {
		MessageDateTime = messageDateTime;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUserImageUrl() {
		return UserImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		UserImageUrl = userImageUrl;
	}

	
}
