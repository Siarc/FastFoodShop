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

public class CustomAdapter extends ArrayAdapter<String>{

    private static final String TAG = "CustomAdapter";

    private List<String> options;
    private List<String> images;
    private Context context;

    /**
     * CONSTRUCTOR
     * @param context
     * @param options
     * @param images
     */
    public CustomAdapter(Context context, List<String> options, List<String> images) {
        super(context,R.layout.custom_row, options);
        this.images=images;
        this.options=options;
        this.context=context;

        Log.d(TAG, "CustomAdapter: started.");
    }

    public static class ViewHolder{
        TextView mOptions;
        ImageView mImages;
    }

    /**
     * LISTVIEW WORKING CONDITIONS
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.custom_row, parent, false);

            holder = new ViewHolder();
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mOptions = (TextView) convertView.findViewById(R.id.arcText1);
        holder.mImages=(ImageView) convertView.findViewById(R.id.arcImage);

        holder.mOptions.setText(options.get(position));
        Glide.with(context).load(images.get(position)).into(holder.mImages);
        return convertView;

    }
}


