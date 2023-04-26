package com.example.fooddistribution.Models;

public class BannerModel {
    String imageUrl;
    String websiteUrl;
    String title;
    String source;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public BannerModel(String imageUrl, String websiteUrl, String title, String source) {
        this.imageUrl = imageUrl;
        this.websiteUrl = websiteUrl;
        this.title = title;
        this.source = source;
    }
}
