package com.example.aminu.fastfoodshop.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aminu.fastfoodshop.HomeActivity.HomeActivity;
import com.example.aminu.fastfoodshop.Methods.TotalPrice;
import com.example.aminu.fastfoodshop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aminu on 8/25/2017.
 */


public class CustomListAdapter extends BaseAdapter implements View.OnClickListener {

    private static final String TAG = "CustomListAdapter";

    List<String> itemName;
    List<String> rate;
    Context mContext;
    public static String counter="1";
    public static List<String> item_priceBundle= new ArrayList<String>();
    public static List<String> item_counter= new ArrayList<String>();

    /**
     * CONSTRUCTOR
     * @param context
     * @param item_listName
     * @param item_listPrice
     * @param item_listPrice_copy
     */
    public CustomListAdapter(Context context, List<String> item_listName, List<String> item_listPrice, List<String> item_listPrice_copy) {

        this.mContext=context;
        this.itemName=item_listName;
        this.rate=item_listPrice;
        this.item_priceBundle=item_listPrice_copy;

        Log.d(TAG, "CustomListAdapter: started.");
    }

    @Override
    public int getCount() {
        return itemName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        ImageView img1;
        ImageView img2;
        ImageView img3;

    }

    /**
     * LISTVIEW WORKING CONDITIONS
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        item_counter.add(counter);
        double amount = 0;

        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.custom_checkout_row, null);
            holder = new ViewHolder();
            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text1 = (TextView) convertView.findViewById(R.id.checkoutText1);
        holder.text2 = (TextView) convertView.findViewById(R.id.counter);
        holder.text3 = (TextView) convertView.findViewById(R.id.rate);
        holder.text4 = (TextView) convertView.findViewById(R.id.totalBundle);
        holder.img1 = (ImageView) convertView.findViewById(R.id.decrement);
        holder.img2 = (ImageView) convertView.findViewById(R.id.increment);
        holder.img3 = (ImageView) convertView.findViewById(R.id.cancel_row);

        /**
         * DECREMENT IMAGE BUTTON CONDITION
         */
        try {
            final ViewHolder finalHolder = holder;
            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String holdCount = (String) finalHolder.text2.getText();
                    int decCount = Integer.parseInt(holdCount);
                    if (decCount <= 1) {
                        return;
                    } else {
                        decCount--;

                        String decCounter = String.valueOf(decCount);
                        item_counter.set(position, decCounter);
                        finalHolder.text2.setText(item_counter.get(position));
                        Log.d(TAG, "onClick: Decreased quantity number: " + finalHolder.text2.getText());

                        Holocast(decCount);
                    }
                }

                private void Holocast(int decCount) {

                    String countPrice1 = rate.get(position);
                    decCount *= Integer.parseInt(countPrice1);//multiply count number with individual price of item

                    item_priceBundle.set(position, String.valueOf(decCount));
                    finalHolder.text4.setText(item_priceBundle.get(position));

                    /**
                     * TextView for total price
                     */
                    String lastPrice = TotalPrice.netPrice(item_priceBundle);
                    HomeActivity.last_price.setText(lastPrice);

                    Log.d(TAG, "Holocast: Price increased: " + finalHolder.text4.getText());

                }
            });
        }catch (Exception e){
            Log.d(TAG, "getView: decrement price error: "+e.getMessage());
        }

        /**
         * INCREMENT IMAGE BUTTON CONDITION
         */
        try {
            final ViewHolder finalHolder1 = holder;
            holder.img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String holdCount = (String) finalHolder1.text2.getText();
                    int incCount = Integer.parseInt(holdCount);
                    incCount++;

                    String counter2 = String.valueOf(incCount);
                    item_counter.set(position, counter2);
                    finalHolder1.text2.setText(item_counter.get(position));

                    Log.d(TAG, "onClick: Increased quantity number: " + finalHolder1.text2.getText());

                    Holocast(incCount);

                }

                private void Holocast(int incCount) {

                    String countPrice2 = rate.get(position);
                    incCount *= Integer.parseInt(countPrice2);//multiply count number with individual price of item

                    item_priceBundle.set(position, String.valueOf(incCount));
                    finalHolder1.text4.setText(item_priceBundle.get(position));

                    /**
                     * TextView for total price
                     */
                    String lastPrice = TotalPrice.netPrice(item_priceBundle);
                    HomeActivity.last_price.setText(lastPrice);

                    Log.d(TAG, "Holocast: Price increased: " + finalHolder1.text4.getText());

                }
            });
        }catch (Exception e){
            Log.d(TAG, "getView: price increment error: "+e.getMessage());
        }

        /**
         * DELETE IMAGE BUTTON CONDITION
         */
        holder.img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemName.remove(position);
                rate.remove(position);
                item_priceBundle.remove(position);
                item_counter.remove(position);
                HomeActivity.item_listid.remove(position);
                String lastPrice = TotalPrice.netPrice(item_priceBundle);
                HomeActivity.last_price.setText(lastPrice);

                notifyDataSetChanged();

                Log.d(TAG, "onClick: Row number "+position+" deleted.");

            }
        });

        holder.text1.setText(itemName.get(position));
        holder.text2.setText(item_counter.get(position));
        holder.text3.setText(rate.get(position));
        holder.text4.setText(item_priceBundle.get(position));
        holder.img1.setImageResource(R.drawable.ic_decrement);
        holder.img2.setImageResource(R.drawable.ic_increment);
        holder.img3.setImageResource(R.drawable.ic_cancel);

        return convertView;

    }

    @Override
    public void onClick(View view) {

    }

}

