package com.example.fooddistribution.Models;

import com.example.fooddistribution.Activities.User;

import java.io.Serializable;

public class DonReqModel implements Serializable {
    private String donationId;

    private String UserId;
    private UserModel model = null;
    private NgoModel ngoModel = null;
    long uploadTime;

    private String foodImage;
    private String foodName;
    private String cookTime;
    private String expPepCnt;
    private String loc;

    private String status;
    private String latitude;
    private String longitude;

    public DonReqModel() {
        // no-argument constructor required for Firestore deserialization
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public DonReqModel(String UserId, UserModel model, String foodImage, String foodName, String cookTime, String expPepCnt, String loc, String status, String latitude, String longitude, long uploadTime,NgoModel ngoModel) {
        this.UserId = UserId;
        this.model = model;
        this.foodImage = foodImage;
        this.foodName = foodName;
        this.cookTime = cookTime;
        this.expPepCnt = expPepCnt;
        this.loc = loc;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uploadTime = uploadTime;

        donationId = UserId + uploadTime;
    }


    public UserModel getModel() {
        return model;
    }

    public void setModel(UserModel model) {
        this.model = model;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getExpPepCnt() {
        return expPepCnt;
    }

    public void setExpPepCnt(String expPepCnt) {
        this.expPepCnt = expPepCnt;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDonationId() {
        return donationId;
    }

    public void setDonationId(String donationId) {
        this.donationId = donationId;
    }

    public NgoModel getNgoModel() {
        return ngoModel;
    }

    public void setNgoModel(NgoModel ngoModel) {
        this.ngoModel = ngoModel;
    }
}
