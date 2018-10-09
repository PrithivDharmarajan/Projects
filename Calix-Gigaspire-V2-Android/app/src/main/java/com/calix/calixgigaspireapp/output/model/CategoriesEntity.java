package com.calix.calixgigaspireapp.output.model;

import java.io.Serializable;

/**
 * Created by user on 2/6/2018.
 */

public class CategoriesEntity implements Serializable {

    private int type;
    private String name;
    private int count;
    private int issueCount;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCount() {
        return String.valueOf(count);
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIssueCount() {
        return String.valueOf(issueCount);
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }


}
