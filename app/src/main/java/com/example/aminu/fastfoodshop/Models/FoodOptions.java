package com.example.aminu.fastfoodshop.Models;

/**
 * Created by aminu on 9/15/2017.
 */

public class FoodOptions {

    private static final String TAG = "FoodOptions";

    public static String id;
    public String name;

    public FoodOptions(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public FoodOptions() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "FoodOptions{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
