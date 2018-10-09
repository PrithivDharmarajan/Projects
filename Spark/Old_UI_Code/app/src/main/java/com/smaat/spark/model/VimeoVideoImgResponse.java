package com.smaat.spark.model;

import java.io.Serializable;


public class VimeoVideoImgResponse implements Serializable {
    private String thumbnail_url;
    private String thumbnail_url_with_play_button;

    public String getThumbnail_url_with_play_button() {
        if (thumbnail_url_with_play_button == null) {
            thumbnail_url_with_play_button = "";
        }
        return thumbnail_url_with_play_button;
    }

    public void setThumbnail_url_with_play_button(String thumbnail_url_with_play_button) {
        this.thumbnail_url_with_play_button = thumbnail_url_with_play_button;
    }

    public String getThumbnail_url() {
        if (thumbnail_url == null) {
            thumbnail_url = "";
        }
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

}
