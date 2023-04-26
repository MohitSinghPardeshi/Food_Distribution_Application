package com.example.fooddistribution.Models;

public class HistoryModel {
    private String foodImg,foodDetail,noOfPeople,cookingTime,location;

    public String getFoodImg() {
        return foodImg;
    }

    public void setFoodImg(String foodImg) {
        this.foodImg = foodImg;
    }

    public String getFoodDetail() {
        return foodDetail;
    }

    public void setFoodDetail(String foodDetail) {
        this.foodDetail = foodDetail;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(String cookingTime) {
        this.cookingTime = cookingTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public HistoryModel(String foodImg, String foodDetail, String noOfPeople, String cookingTime, String location) {
        this.foodImg = foodImg;
        this.foodDetail = foodDetail;
        this.noOfPeople = noOfPeople;
        this.cookingTime = cookingTime;
        this.location = location;
    }
}
