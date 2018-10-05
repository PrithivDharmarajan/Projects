package com.bridgellc.bridge.adapter;

/**
 * Created by sys on 3/14/2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.ImageViewRounded;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class HomeGridAdapter extends BaseAdapter {
    private Context context;

    private ArrayList<HomeSingleItemEntity> mHomeSingleItemEntityList;
    private Typeface mLightFont, mRegulartFont;


//    private String[] rates = new String[]{"99.99$", "199.19$", "299.29$", "399.49$", "499.19$"};
//    private int[] images = new int[]{R.drawable.image_one, R.drawable.image_two, R.drawable.image_three, R.drawable.image_four, R.drawable.image_five};


    public HomeGridAdapter(Context context, ArrayList<HomeSingleItemEntity> arrayList) {
        this.context = context;
        this.mHomeSingleItemEntityList = arrayList;
        this.mLightFont = TypefaceSingleton.getTypeface().getLightFont(context);
        this.mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(context);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = inflater.inflate(R.layout.home_grid_item,parent,false);

        } else {
            gridView = (View) convertView;
        }
        TextView productPrice = (TextView) gridView.findViewById(R.id.product_price);
        TextView productName = (TextView) gridView.findViewById(R.id.product_name);
        ImageViewRounded imageViewRounded = (ImageViewRounded) gridView.findViewById(R.id.product_img);

        HomeSingleItemEntity homeSingleItemEntity = mHomeSingleItemEntityList.get(position);


        String tag = "";

        String productPriceText = "";
        if (homeSingleItemEntity.getItem_cost() != null) {
            productPriceText = "$" + GlobalMethods.getPriValWithTwoPoint(homeSingleItemEntity.getItem_cost(), false);

        }
        productPrice.setText(productPriceText);
        productPrice.setTypeface(mRegulartFont);
        productName.setTypeface(mRegulartFont);

        productName.setText(homeSingleItemEntity.getItem_name());

        Glide.with(context)
                .load(homeSingleItemEntity.getPicture1())
                .asBitmap().into(imageViewRounded);


        if (homeSingleItemEntity.getItem_mode().equalsIgnoreCase("1")) {
            productPrice.setBackgroundResource(R.drawable.home_blue_small);
            tag = "b_" + homeSingleItemEntity.getPicture1() + "_" + productPriceText;
        } else {
            productPrice.setBackgroundResource(R.drawable.home_green_small);
            tag = "g_" + homeSingleItemEntity.getPicture1() + "_" + productPriceText;
        }


        TagClass tagClass = new TagClass();
        tagClass.tag = tag;
        tagClass.mHomeSingleItemEntity = homeSingleItemEntity;
        gridView.setTag(tagClass);

        return gridView;
    }

    public static class TagClass {
        public String tag;
        public HomeSingleItemEntity mHomeSingleItemEntity;
    }


    @Override
    public int getCount() {
        return mHomeSingleItemEntityList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
