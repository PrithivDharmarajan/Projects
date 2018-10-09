package com.smaat.renterblock.findagent;

import java.io.Serializable;
import java.util.ArrayList;

public class AgentUserDetailsResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private AgentUserDetailsEntity userresult;
	private ArrayList<AgentReviewsEntity> reviewresult;

	public AgentUserDetailsEntity getUserresult() {
		return userresult;
	}

	public void setUserresult(AgentUserDetailsEntity userresult) {
		this.userresult = userresult;
	}

	public ArrayList<AgentReviewsEntity> getReviewresult() {
		return reviewresult;
	}

	public void setReviewresult(ArrayList<AgentReviewsEntity> reviewresult) {
		this.reviewresult = reviewresult;
	}

}
