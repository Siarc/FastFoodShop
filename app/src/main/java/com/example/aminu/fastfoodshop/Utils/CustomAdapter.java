package com.example.aminu.fastfoodshop.Utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aminu.fastfoodshop.R;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<String>{

    private static final String TAG = "CustomAdapter";

    List<String> options;
    int[] images;

    /**
     * CONSTRUCTOR
     * @param context
     * @param options
     * @param images
     */
    public CustomAdapter(Context context, List<String> options, int images[]) {
        super(context,R.layout.custom_row, options);
        this.images=images;
        this.options=options;
        //this.context=context;

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

        View customView = convertView;

        final ViewHolder holder;
        if (customView==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            customView = inflater.inflate(R.layout.custom_row, parent, false);

            holder = new ViewHolder();
            //String singleTopicItem= getItem(position);
            holder.mOptions = (TextView) customView.findViewById(R.id.arcText1);
            holder.mImages=(ImageView) customView.findViewById(R.id.arcImage);
            customView.setTag(holder);
        }else{
            //String singleTopicItem= getItem(position);
            holder = (ViewHolder) customView.getTag();
            holder.mOptions = (TextView) customView.findViewById(R.id.arcText1);
            holder.mImages=(ImageView) customView.findViewById(R.id.arcImage);
        }

        holder.mOptions.setText(options.get(position));
        holder.mImages.setImageResource(images[position]);
        return customView;

    }
}
