package com.fautus.fautusapp.model;

import java.io.Serializable;

/**
 * Created by sys on 22-May-17.
 */

public class SendEmailResponse implements Serializable {
    private String id;
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
