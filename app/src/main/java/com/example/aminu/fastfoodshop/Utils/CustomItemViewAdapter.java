package com.example.aminu.fastfoodshop.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aminu.fastfoodshop.R;

import java.util.List;

/**
 * Created by aminu on 10/8/2017.
 */

public class CustomItemViewAdapter extends BaseAdapter{
    private static final String TAG = "CustomItemViewAdapter";

    private Context mContext;
    private final List<String> itemName;
    private final List<String> itemImage;
    private final List<String> itemPrice;

    public CustomItemViewAdapter(Context context, List<String> itemName, List<String> itemImage, List<String> itemPrice) {
        mContext = context;
        this.itemImage = itemImage;
        this.itemName = itemName;
        this.itemPrice = itemPrice;

        Log.d(TAG, "CustomGridAdapter: started.");
    }

    @Override
    public int getCount() {
        return itemName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolder{
        TextView textView1;
        TextView textView2;
        ImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.custom_grid_row,null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView1 = (TextView)convertView.findViewById(R.id.gridText1);
        holder.textView2= (TextView)convertView.findViewById(R.id.gridText2);
        holder.imageView = (ImageView)convertView.findViewById(R.id.gridImage);

        holder.textView1.setText(itemName.get(position));
        holder.textView2.setText(itemPrice.get(position));
        Glide.with(mContext).load(itemImage.get(position)).into(holder.imageView);

        return convertView;
    }
}
