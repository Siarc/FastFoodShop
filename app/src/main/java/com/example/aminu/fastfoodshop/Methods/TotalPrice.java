package com.example.aminu.fastfoodshop.Methods;

import android.util.Log;

import com.example.aminu.fastfoodshop.HomeActivity.HomeActivity;

import java.util.List;

/**
 * Created by aminu on 9/11/2017.
 */

public class TotalPrice {

    private static final String TAG = "TotalPrice";


    public static String netPrice(List<String> totalPrice) {

        int netPrice = 0;
        String ResetPrice="Price";
        try {

            for (int i = 0; i < totalPrice.size(); i++) {

                netPrice += Integer.parseInt(HomeActivity.item_listPrice_copy.get(i));
                Log.d(TAG, "netPrice: " + netPrice);
            }
        } catch (Exception e) {
            Log.d(TAG, "netPrice: error: " + e.getMessage());
        }

        if(netPrice==0){
            return ResetPrice;
        }else {
            return String.valueOf(netPrice);
        }
    }
}
