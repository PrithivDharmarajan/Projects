package com.smaat.renterblock.entity;

import java.io.Serializable;

public class UrlEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
