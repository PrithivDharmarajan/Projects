package com.smaat.spark.entity.outputEntity;

import java.io.Serializable;


public class TrendsEntity implements Serializable {

    private String trend_id;
    private String trend_selected;
    private String name;
    private String datetime;

    public String getDatetime() {
        if (datetime == null) {
            datetime = "";
        }
        return datetime;
    }

    public String getTrend_selected() {
        if (trend_selected == null) {
            trend_selected = "";
        }
        return trend_selected;
    }

    public void setTrend_selected(String trend_selected) {
        this.trend_selected = trend_selected;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrend_id() {
        if (trend_id == null) {
            trend_id = "";
        }
        return trend_id;
    }

    public void setTrend_id(String trend_id) {
        this.trend_id = trend_id;
    }

}
