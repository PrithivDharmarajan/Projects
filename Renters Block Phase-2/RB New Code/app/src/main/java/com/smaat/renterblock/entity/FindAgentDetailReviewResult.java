package com.smaat.renterblock.entity;


import java.io.Serializable;
import java.util.ArrayList;

public class FindAgentDetailReviewResult implements Serializable {
    private FindAgentDetailUserResult userresult;
    private ArrayList<FindAgentDetailReviewResultEntity> reviewresult;

    public FindAgentDetailUserResult getUserresult() {
        return userresult == null ? new FindAgentDetailUserResult() : userresult;
    }

    public void setUserresult(FindAgentDetailUserResult userresult) {
        this.userresult = userresult;
    }

    public ArrayList<FindAgentDetailReviewResultEntity> getReviewresult() {
        return reviewresult == null ? new ArrayList<FindAgentDetailReviewResultEntity>() : reviewresult;
    }

    public void setReviewresult(ArrayList<FindAgentDetailReviewResultEntity> reviewresult) {
        this.reviewresult = reviewresult;
    }

}
