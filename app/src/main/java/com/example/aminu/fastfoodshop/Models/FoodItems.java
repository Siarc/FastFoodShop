package com.example.aminu.fastfoodshop.Models;

/**
 * Created by aminu on 9/15/2017.
 */

public class FoodItems {

    public String itemImgUrl;
    public String name;
    public Long price;
    public String itemId;

    public FoodItems(String itemImgUrl, String name, Long price,String itemId) {
        this.itemImgUrl = itemImgUrl;
        this.name = name;
        this.price = price;
        this.itemId = itemId;
    }

    public FoodItems() {
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
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

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "FoodItems{" +
                "itemImgUrl='" + itemImgUrl + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", itemId='" + itemId + '\'' +
                '}';
    }
}
