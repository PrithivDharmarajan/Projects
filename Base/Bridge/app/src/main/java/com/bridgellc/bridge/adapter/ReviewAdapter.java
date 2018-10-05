package com.bridgellc.bridge.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.entity.ReviewEntity;
import com.bridgellc.bridge.utils.GlobalMethods;
import com.bridgellc.bridge.utils.TypefaceSingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends BaseAdapter {

    private Context mContext;
    private Holder viewHolder;
    private ArrayList<ReviewEntity> mReview;
    private Typeface mLightFont, mRegulartFont;
    private SimpleDateFormat mTargetDateTime = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    public ReviewAdapter(Context mContext, ArrayList<ReviewEntity> mRev_Ent) {
        this.mContext = mContext;
        this.mReview = mRev_Ent;
        this.mLightFont = TypefaceSingleton.getTypeface().getLightFont(mContext);
        this.mRegulartFont = TypefaceSingleton.getTypeface().getRegularFont(mContext);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.review_adapter, parent,
                    false);
            viewHolder = new Holder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.mName.setText(mReview.get(position).getRate_user_first_name());
        viewHolder.mComments.setText(mReview.get(position).getComments());
        viewHolder.mDate.setText(GlobalMethods.getCustomDateFormate(mReview.get(position).getDate(), mTargetDateTime));
        viewHolder.mRating_bar.setRating(Float.valueOf(mReview.get(position).getRating()));


        return convertView;

    }

    class Holder {
        TextView mName, mComments, mDate;
        RatingBar mRating_bar;

        public Holder(View mView) {
            mName = (TextView) mView.findViewById(R.id.name_txt);
            mComments = (TextView) mView.findViewById(R.id.comments);
            mDate = (TextView) mView.findViewById(R.id.date);
            mRating_bar = (RatingBar) mView.findViewById(R.id.user_ratingbar);


            mName.setTypeface(mRegulartFont);
            mComments.setTypeface(mRegulartFont);
            mDate.setTypeface(mRegulartFont);
        }
    }

    @Override
    public int getCount() {
        return mReview.size();
    }

    @Override
    public Object getItem(int position) {
        return 10;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
