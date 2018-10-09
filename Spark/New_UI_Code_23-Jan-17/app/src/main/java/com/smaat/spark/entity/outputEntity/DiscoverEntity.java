package com.smaat.spark.entity.outputEntity;

import java.io.Serializable;


public class DiscoverEntity implements Serializable {


    private String discovery_id;
    private String dicoveryName;
    private String video;
    private String datetime;
    private String video_thumbnail;
    private String thumbnail_1;
    private String thumbnail_2;
    private String thumbnail_3;

    public String getThumbnail_3() {
        if (thumbnail_3 == null) {
            thumbnail_3 = "";
        }
        return thumbnail_3;
    }

    public void setThumbnail_3(String thumbnail_3) {
        this.thumbnail_3 = thumbnail_3;
    }

    public String getThumbnail_2() {
        if (thumbnail_2 == null) {
            thumbnail_2 = "";
        }
        return thumbnail_2;
    }

    public void setThumbnail_2(String thumbnail_2) {
        this.thumbnail_2 = thumbnail_2;
    }

    public String getThumbnail_1() {
        if (thumbnail_1 == null) {
            thumbnail_1 = "";
        }
        return thumbnail_1;
    }

    public void setThumbnail_1(String thumbnail_1) {
        this.thumbnail_1 = thumbnail_1;
    }



    public String getVideo_thumbnail() {
        if (video_thumbnail == null) {
            video_thumbnail = "";
        }
        return video_thumbnail;
    }

    public void setVideo_thumbnail(String video_thumbnail) {
        this.video_thumbnail = video_thumbnail;
    }


    public String getDatetime() {
        if (datetime == null) {
            datetime = "";
        }
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getVideo() {
        if (video == null) {
            video = "";
        }
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getDicoveryName() {
        if (dicoveryName == null) {
            dicoveryName = "";
        }
        return dicoveryName;
    }

    public void setDicoveryName(String dicoveryName) {
        this.dicoveryName = dicoveryName;
    }

    public String getDiscovery_id() {
        if (discovery_id == null) {
            discovery_id = "";
        }
        return discovery_id;
    }

    public void setDiscovery_id(String discovery_id) {
        this.discovery_id = discovery_id;
    }


}
