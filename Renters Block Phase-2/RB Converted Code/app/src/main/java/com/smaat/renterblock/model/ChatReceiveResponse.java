package com.smaat.renterblock.model;

import java.util.ArrayList;

import com.smaat.renterblock.friends.entity.ChatEntity;

public class ChatReceiveResponse {

	public String error_code;
	public String msg;
	public String group_name;
	public String group_user_name;
	public ArrayList<ChatEntity> result;

}
