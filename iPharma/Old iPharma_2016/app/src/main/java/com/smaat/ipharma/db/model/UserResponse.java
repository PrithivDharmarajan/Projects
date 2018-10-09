package com.smaat.ipharma.db.model;

import java.util.ArrayList;

public class UserResponse {
	ArrayList<UserDetails> pharma;

	public ArrayList<UserDetails> getPharma() {
		return pharma;
	}

	public void setPharma(ArrayList<UserDetails> pharma) {
		this.pharma = pharma;
	}

}
