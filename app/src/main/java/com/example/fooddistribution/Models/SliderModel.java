package com.example.fooddistribution.Models;

public class SliderModel {
    String imageUrl;
    String headTxt;
    String subTxt;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHeadTxt() {
        return headTxt;
    }

    public void setHeadTxt(String headTxt) {
        this.headTxt = headTxt;
    }

    public String getSubTxt() {
        return subTxt;
    }

    public void setSubTxt(String subTxt) {
        this.subTxt = subTxt;
    }

    public SliderModel(String imageUrl, String headTxt, String subTxt) {
        this.imageUrl = imageUrl;
        this.headTxt = headTxt;
        this.subTxt = subTxt;
    }
}
