package com.smaat.renterblock.entity;

import java.io.Serializable;

import android.widget.Button;

public class AdapterCustomEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Button checked;
	private String btn_pos;
	
	public Button getChecked() {
		return checked;
	}
	public void setChecked(Button checked) {
		this.checked = checked;
	}
	public String getBtn_pos() {
		return btn_pos;
	}
	public void setBtn_pos(String btn_pos) {
		this.btn_pos = btn_pos;
	}

}
