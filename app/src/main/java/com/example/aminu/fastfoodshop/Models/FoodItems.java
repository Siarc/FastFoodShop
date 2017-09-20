package com.example.aminu.fastfoodshop.Models;

/**
 * Created by aminu on 9/15/2017.
 */

public class FoodItems {

    public String image;
    public String name;
    public Long price;

    public FoodItems(String image, String name, Long price) {

        this.name = name;
        this.price = price;
    }
    public FoodItems() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "FoodItems{" +
                "image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
