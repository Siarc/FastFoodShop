package com.example.aminu.fastfoodshop.Models;

/**
 * Created by aminu on 9/15/2017.
 */

public class FoodOptions {

    private static final String TAG = "FoodOptions";

    public String foodOptionsId;
    public String name;
    public String categoryImgUrl;

    public FoodOptions(String name, String categoryImgUrl, String foodOptionsId) {
        this.foodOptionsId = foodOptionsId;
        this.name = name;
        this.categoryImgUrl = categoryImgUrl;
    }

    public FoodOptions() {
    }

    public String getFoodOptionsId() {
        return foodOptionsId;
    }

    public void setFoodOptionsId(String foodOptionsId) {
        this.foodOptionsId = foodOptionsId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryImgUrl() {
        return categoryImgUrl;
    }

    public void setCategoryImgUrl(String categoryImgUrl) {
        this.categoryImgUrl = categoryImgUrl;
    }

    @Override
    public String toString() {
        return "FoodOptions{" +
                "foodOptionsId='" + foodOptionsId + '\'' +
                ", name='" + name + '\'' +
                ", categoryImgUrl='" + categoryImgUrl + '\'' +
                '}';
    }
}
