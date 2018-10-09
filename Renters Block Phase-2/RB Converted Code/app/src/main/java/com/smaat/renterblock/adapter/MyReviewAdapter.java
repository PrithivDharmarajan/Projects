package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.MyReviews;
import com.smaat.renterblock.entity.MyReviewsResult;
import com.smaat.renterblock.util.TypefaceSingleton;

public class MyReviewAdapter extends BaseAdapter {

	Context mContext;
	MyReviewsResult mMyReviewsResult;
	ArrayList<MyReviews> mMyReviewsList;
	float rating;

	public MyReviewAdapter(Context context, MyReviewsResult result) {
		mContext = context;
		mMyReviewsResult = result;
		mMyReviewsList = mMyReviewsResult.getReview();

	}

	class Holder {

		private TextView mAddress, mDescription;
		private RatingBar mRatingBar;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		holder = new Holder();
		MyReviews mMyReviews;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(R.layout.adapter_myreview, null);

		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(mContext);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(mContext);

		holder.mAddress = (TextView) convertView.findViewById(R.id.property_name);
		holder.mDescription = (TextView) convertView.findViewById(R.id.description);
		holder.mRatingBar = (RatingBar) convertView.findViewById(R.id.review_ratingbar);

		holder.mAddress.setTypeface(mTypefaceBold);
		holder.mDescription.setTypeface(mTypeface);

		mMyReviews = mMyReviewsList.get(position);
		AQuery aq1 = new AQuery(mContext).recycle(convertView);

		aq1.id(R.id.building_image).image(mMyReviews.getPro_img(), true, true, 0, R.drawable.default_prop_icon, null, 0,
				1.0f);

		if (mMyReviews.getProperty_name() != null && mMyReviews.getAddress() != null) {
//			if (mMyReviews.getProperty_name().equals("")) {
				holder.mAddress.setText(mMyReviews.getAddress());
//			} else {
//				holder.mAddress.setText(mMyReviews.getProperty_name());
//			}
		} else {
			holder.mAddress.setText("");
		}
		if (mMyReviews.getProperty_review_comment().size() > 0) {
			int size = mMyReviews.getProperty_review_comment().size();
			String mString = mMyReviews.getProperty_review_comment().get(size - 1).getReview_comments().toString();
			holder.mDescription.setText(mString);
		} else {
			holder.mDescription.setText(mMyReviews.getComments());
		}

		if (mMyReviews.getProperty_review_comment().size() > 0) {
			int size = mMyReviews.getProperty_review_comment().size();
			rating = Float.parseFloat(mMyReviews.getProperty_review_comment().get(size - 1).getReview_rating());
		} else {
			rating = Float.parseFloat(mMyReviews.getRating());
		}

		holder.mRatingBar.setRating(rating);
		return convertView;
	}

	@Override
	public int getCount() {
		return mMyReviewsList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mMyReviewsList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
}
