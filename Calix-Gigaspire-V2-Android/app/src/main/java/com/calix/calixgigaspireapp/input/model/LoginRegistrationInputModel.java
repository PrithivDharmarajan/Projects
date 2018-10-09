package com.calix.calixgigaspireapp.input.model;

import java.io.Serializable;

public class LoginRegistrationInputModel implements Serializable {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private  LoginRegistrationMobileInputModel mobileDevice;

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName == null ? "" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  LoginRegistrationMobileInputModel getMobileDevice() {
        return mobileDevice == null ? new  LoginRegistrationMobileInputModel() : mobileDevice;
    }

    public void setMobileDevice( LoginRegistrationMobileInputModel mobileDevice) {
        this.mobileDevice = mobileDevice;
    }


}
