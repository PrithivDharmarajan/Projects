package com.fautus.fautusapp.entity;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;


public class ParsePhotographerEntity implements Serializable {


    private ParseUser user = new ParseUser();
    private String fullName = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String postalCode = "";
    private String country = "";
    private String langugages = "";
    private String dateOfBirth = "";
    private String last4SSN = "";
    private String introMessage = "";
    private String cameraBrand = "";
    private String cameraModel = "";
    private String otherCamera = "";
    private String businessName = "";
    private String businessWebsite = "";
    private int maxSkillLevel = 0;
    private float averageFriendliness = 0.0f;
    private float averageQuality = 0.0f;
    private boolean cameraStrapShipped = false;
    private boolean approvedPro = false;
    private boolean isAvailable = false;
    private Date cameraStrapShippedDate = new Date();
    private ParseGeoPoint currentLocation = new ParseGeoPoint();
    private ParseFile profilePhoto;
    private ParseFile photoIDPhoto;

    public String getOtherCamera() {
        return otherCamera;
    }

    public void setOtherCamera(String otherCamera) {
        if(otherCamera==null){
            return;
        }
        this.otherCamera = otherCamera;
    }


    public ParseUser getUser() {
        return user;
    }

    public void setUser(ParseUser user) {
        if(user==null){
            return;
        }

        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if(fullName==null){
            return;
        }
        this.fullName = fullName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        if(address1==null){
            return;
        }
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        if(address2==null){
            return;
        }
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if(city==null){
            return;
        }
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        if(state==null){
            return;
        }
        this.state = state;
    }

    public String getPostalCode() {

        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if(postalCode==null){
            return;
        }
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if(country==null){
            return;
        }
        this.country = country;
    }

    public String getLangugages() {
        return langugages;
    }

    public void setLangugages(String langugages) {
        if(langugages==null){
            return;
        }
        this.langugages = langugages;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if(dateOfBirth==null){
            return;
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getLast4SSN() {
        return last4SSN;
    }

    public void setLast4SSN(String last4SSN) {
        if(last4SSN==null){
            return;
        }
        this.last4SSN = last4SSN;
    }

    public String getIntroMessage() {
        return introMessage;
    }

    public void setIntroMessage(String introMessage) {
        if(introMessage==null){
            return;
        }
        this.introMessage = introMessage;
    }

    public String getCameraBrand() {
        return cameraBrand;
    }

    public void setCameraBrand(String cameraBrand) {
        if(cameraBrand==null){
            return;
        }
        this.cameraBrand = cameraBrand;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraModel(String cameraModel) {
        if(cameraModel==null){
            return;
        }
        this.cameraModel = cameraModel;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        if(businessName==null){
            return;
        }
        this.businessName = businessName;
    }

    public String getBusinessWebsite() {
        return businessWebsite;
    }

    public void setBusinessWebsite(String businessWebsite) {
        if(businessWebsite==null){
            return;
        }
        this.businessWebsite = businessWebsite;
    }

    public int getMaxSkillLevel() {

        return maxSkillLevel;
    }

    public void setMaxSkillLevel(int maxSkillLevel) {

        this.maxSkillLevel = maxSkillLevel;
    }

    public float getAverageFriendliness() {
        return averageFriendliness;
    }

    public void setAverageFriendliness(float averageFriendliness) {
        this.averageFriendliness = averageFriendliness;
    }

    public float getAverageQuality() {
        return averageQuality;
    }

    public void setAverageQuality(float averageQuality) {
        this.averageQuality = averageQuality;
    }

    public boolean isCameraStrapShipped() {

        return cameraStrapShipped;
    }

    public void setCameraStrapShipped(boolean cameraStrapShipped) {

        this.cameraStrapShipped = cameraStrapShipped;
    }

    public boolean isApprovedPro() {
        return approvedPro;
    }

    public void setApprovedPro(boolean approvedPro) {
        this.approvedPro = approvedPro;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public Date getCameraStrapShippedDate() {
        return cameraStrapShippedDate;
    }

    public void setCameraStrapShippedDate(Date cameraStrapShippedDate) {
        if(cameraStrapShippedDate==null){
            return;
        }
        this.cameraStrapShippedDate = cameraStrapShippedDate;
    }

    public ParseGeoPoint getCurrentLocation() {

        return currentLocation;
    }

    public void setCurrentLocation(ParseGeoPoint currentLocation) {
        if(currentLocation==null){
            return;
        }
        this.currentLocation = currentLocation;
    }

    public ParseFile getProfilePhoto() {

        return profilePhoto;
    }

    public void setProfilePhoto(ParseFile profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public ParseFile getPhotoIDPhoto() {
        return photoIDPhoto;
    }

    public void setPhotoIDPhoto(ParseFile photoIDPhoto) {
        this.photoIDPhoto = photoIDPhoto;
    }


}


