package com.smaat.ipharma.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Text;
import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.HistoryOrderEntity;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RecentOrderEntity;
import com.smaat.ipharma.entity.ShowReviewMessageEntity;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.GlobalMethods;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by admin on 1/24/2017.
 */

public class ReviewAdapter extends BaseAdapter implements View.OnClickListener {
    private Context mContext;
    private ArrayList<ShowReviewMessageEntity> mShowreviewentity;
    public static Dialog mDialog;
    public static String mShopId, mUserID;
    public static int mSelectPos;

    String deliver_status = "";

    public ReviewAdapter(Context context,
                                  ArrayList<ShowReviewMessageEntity> mRecentList) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mShowreviewentity = mRecentList;
        mUserID = ((String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, AppConstants.USER_ID));
    }

    public class Holder {
        TextView UserName;
        TextView date_time;
        TextView review_text;
        TextView comments_text;
        RatingBar ratingBar;


    }

    public View getView(int position, View row, ViewGroup parent) {

        View convertView = row;
        Holder mholder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_review_screen, parent, false);
            mholder = new Holder();
            mholder.UserName = (TextView) convertView
                    .findViewById(R.id.user_name);
            mholder.date_time = (TextView) convertView
                    .findViewById(R.id.date_time);
            mholder.review_text = (TextView) convertView
                    .findViewById(R.id.review_text);
            mholder.comments_text = (TextView) convertView
                    .findViewById(R.id.comments_text);
            mholder.ratingBar = (RatingBar) convertView
                    .findViewById(R.id.review_rating_bar);



            convertView.setTag(mholder);
        } else {
            mholder = (Holder) convertView.getTag();
        }
        final ShowReviewMessageEntity tempData = mShowreviewentity.get(position);


        mholder.UserName.setText(tempData.getFullName());
        mholder.date_time.setText(tempData.getReviewDateTime());
        mholder.review_text.setText(tempData.getReviewComment().trim());
        mholder.ratingBar.setRating(Float.parseFloat(tempData.getReviewRating()));
        mholder.comments_text.setText(tempData.getReviewHeading().trim());



        return convertView;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mShowreviewentity.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}
