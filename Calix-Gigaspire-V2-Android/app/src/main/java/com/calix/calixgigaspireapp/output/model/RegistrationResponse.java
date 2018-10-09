package com.calix.calixgigaspireapp.output.model;


import com.calix.calixgigaspireapp.utils.AppConstants;

import java.io.Serializable;

/**
 * Created by user on 1/22/2018.
 */

public class RegistrationResponse implements Serializable {


    private String errorType = AppConstants.SUCCESS_CODE;
    private String errorDesc = "";
    private String userId = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
