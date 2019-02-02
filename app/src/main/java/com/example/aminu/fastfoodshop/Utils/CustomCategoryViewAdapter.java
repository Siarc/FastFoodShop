package com.example.aminu.fastfoodshop.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.aminu.fastfoodshop.R;

import java.util.List;

/**
 * Created by aminu on 10/6/2017.
 */

public class CustomCategoryViewAdapter extends ArrayAdapter<String> {

    private static final String TAG = "CustomCategoryView";;

    private List<String> options;
    private List<String> images;
    private Context context;

    public CustomCategoryViewAdapter(Context context, List<String> options, List<String> images) {
        super(context, R.layout.custom_row, options);

        this.images=images;
        this.options=options;
        this.context=context;

        Log.d(TAG, "CustomCategoryView: started.");

    }


    public static class ViewHolder{
        TextView mOptions;
        ImageView mImages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mOptions = (TextView) convertView.findViewById(R.id.arcText1);
        holder.mImages=(ImageView) convertView.findViewById(R.id.arcImage);

        holder.mOptions.setText(options.get(position));
        Glide.with(context).load(images.get(position)).into(holder.mImages);
        return convertView;
    }
}
