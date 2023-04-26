package com.example.fooddistribution.Models;

public class FoodItem {
    public int foodtype;
    public int wetndry;
    public String nameNdesc = "";

    public FoodItem(int foodtype, int wetndry, String nameNdesc) {
        this.foodtype = foodtype;
        this.wetndry = wetndry;
        this.nameNdesc = nameNdesc;
    }
}
