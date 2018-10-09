package com.smaat.ipharma.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.ipharma.R;


/**
 * Created by sys on 10/27/2016.
 */

public class NavigationAdapter extends BaseAdapter {

    private Activity activity;
    private String[] nNamesArrray;
    private TypedArray nImagesArrray;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader;

    public NavigationAdapter(Activity a, String[] Names_Array, TypedArray Images_Array) {
        activity = a;
        nNamesArrray=Names_Array;
        nImagesArrray=Images_Array;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return nNamesArrray.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.menu_item, null);

        TextView itemname=(TextView)vi.findViewById(R.id.menu_text);
        ImageView itemimage=(ImageView)vi.findViewById(R.id.menu_image);
        itemname.setText(nNamesArrray[position]);
        itemimage.setImageResource(nImagesArrray.getResourceId(position, -1));

        return vi;
    }

}
