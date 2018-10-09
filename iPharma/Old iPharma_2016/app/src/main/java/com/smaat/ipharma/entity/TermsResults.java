package com.smaat.ipharma.entity;

import java.io.Serializable;

public class TermsResults implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String DisclaimerText;
	private String TermsOfUse;
	
	public String getDisclaimerText() {
		return DisclaimerText;
	}
	public void setDisclaimerText(String disclaimerText) {
		DisclaimerText = disclaimerText;
	}
	public String getTermsOfUse() {
		return TermsOfUse;
	}
	public void setTermsOfUse(String termsOfUse) {
		TermsOfUse = termsOfUse;
	}
}
