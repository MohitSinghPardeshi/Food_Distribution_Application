package com.example.fooddistribution.Activities;

public class User {
    public String name = "",email = "";
    public String profileImage="";
    public String Id = "";

    public User(){

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public User(String name, String email,String image) {
        this.name = name;
        this.email = email;
        profileImage = image;
    }
}
