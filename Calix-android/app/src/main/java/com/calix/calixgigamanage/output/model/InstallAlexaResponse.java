package com.calix.calixgigamanage.output.model;

import java.io.Serializable;

public class InstallAlexaResponse implements Serializable {

    private String source="";
    private String speech="";
    private String displayText="";

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }


}
