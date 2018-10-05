package com.smaat.jolt.entity;

public class JoltMessageEntity {

	String MessageId;
	String MessageText;
	String ConversationID;
	String UserID;
	String MessageDateTime;
	String Name;
	String UserImageUrl;

	public void setMessageId(String messageId) {
		MessageId = messageId;
	}

	public String getMessageId() {
		return MessageId;
	}

	public void setMessageText(String messageText) {
		MessageText = messageText;
	}

	public String getMessageText() {
		return MessageText;
	}

	public void setConversationID(String conversationID) {
		ConversationID = conversationID;
	}

	public String getConversationID() {
		return ConversationID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUserID() {
		return UserID;
	}

	public void setMessageDateTime(String messageDateTime) {
		MessageDateTime = messageDateTime;
	}

	public String getMessageDateTime() {
		return MessageDateTime;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getName() {
		return Name;
	}

	public void setUserImageUrl(String userImageUrl) {
		UserImageUrl = userImageUrl;
	}

	public String getUserImageUrl() {
		return UserImageUrl;
	}
}
