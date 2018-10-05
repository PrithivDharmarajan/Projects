package com.bridgellc.bridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 4/30/2016.
 */
public class NotificationAdapter extends BaseAdapter {

    private Context context;

    ArrayList<HomeSingleItemEntity> mAdapRes;
    private Holder viewHolder;
    private Typeface mLightFont, mRegulartFont;

    public NotificationAdapter(Context context, ArrayList<HomeSingleItemEntity> mRes) {
        this.context = context;
        this.mAdapRes = mRes;
        this.mLightFont = TypefaceSingleton.getTypeface().getLightFont(context);
        this.mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(context);

    }

    @Override
    public int getCount() {
        return mAdapRes.size();
    }

    @Override
    public Object getItem(int position) {
        return mAdapRes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_notification, parent,
                    false);
            viewHolder = new Holder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }


        viewHolder.mItemNameTxt.setText(mAdapRes.get(position).getItem_name());
        viewHolder.mMessageTxt.setText(GlobalMethods.getDecodedmessage(mAdapRes.get(
                position).getMessage()));
        viewHolder.mProductPriceTxt.setText("$" + GlobalMethods.getPriValWithTwoPoint(mAdapRes.get(position).getItem_cost(),false));
//        viewHolder.mDateTxt.setText(getLocalTime(mAdapRes.get(position).getDate_time()));
        String date_time = GlobalMethods.convertUTCtime(context,
                mAdapRes.get(position).getDate_time());
        Date date2 = new Date(date_time);
        viewHolder.mDateTxt.setText(GlobalMethods.utcprintDifferenceAgo(new Date(), date2));


        if (mAdapRes.get(position).getItem_mode().equalsIgnoreCase("1")) {
            viewHolder.mProductPriceTxt.setBackgroundResource(R.drawable.home_blue_small);
        } else {
            viewHolder.mProductPriceTxt.setBackgroundResource(R.drawable.home_green_small);
        }

        if (mAdapRes.get(position).getNotifystatus().equalsIgnoreCase(context.getString(R.string.one))) {
            viewHolder.mParentLay.setBackgroundColor(context.getResources().getColor(R.color.light_green));
        } else {
            viewHolder.mParentLay.setBackgroundColor(context.getResources().getColor(R.color.screen_bg));
        }


        try {
            if (mAdapRes.get(position).getPicture1() != null && !mAdapRes.get(position).getPicture1().equalsIgnoreCase("")) {
                Glide.with(context)
                        .load(mAdapRes.get(position).getPicture1())
                        .asBitmap().into(viewHolder.mProductImg);
            } else if (mAdapRes.get(position).getPicture2() != null && !mAdapRes.get(position).getPicture2().equalsIgnoreCase("")) {
                Glide.with(context)
                        .load(mAdapRes.get(position).getPicture2())
                        .asBitmap().into(viewHolder.mProductImg);
            } else if (mAdapRes.get(position).getPicture3() != null && !mAdapRes.get(position).getPicture3().equalsIgnoreCase("")) {
                Glide.with(context)
                        .load(mAdapRes.get(position).getPicture3())
                        .asBitmap().into(viewHolder.mProductImg);
            }
        } catch (Exception e) {

        }


        return convertView;
    }


    class Holder {
        RelativeLayout mParentLay;
        TextView mItemNameTxt, mMessageTxt, mProductPriceTxt, mDateTxt;
        ImageView mProductImg;

        public Holder(View view) {

            mParentLay = (RelativeLayout) view.findViewById(R.id.payclick_lay);
            mItemNameTxt = (TextView) view.findViewById(R.id.name_txt);
            mProductPriceTxt = (TextView) view.findViewById(R.id.product_price);
            mMessageTxt = (TextView) view.findViewById(R.id.productname_txt);
            mMessageTxt.setEllipsize(TextUtils.TruncateAt.END);
            mMessageTxt.setMaxLines(3);
            mDateTxt = (TextView) view.findViewById(R.id.date_txt);
            mProductImg = (ImageView) view.findViewById(R.id.product_img);


            mItemNameTxt.setTypeface(mLightFont);
            mProductPriceTxt.setTypeface(mRegulartFont);
            mMessageTxt.setTypeface(mLightFont);
            mDateTxt.setTypeface(mLightFont);
        }
    }

    public static String getLocalTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String dateFormateInLocalTimeZone = "";
        try {
            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss", Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("DD-MM-yyyy", Locale.US);
            //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateFormateInLocalTimeZone;
    }


}

