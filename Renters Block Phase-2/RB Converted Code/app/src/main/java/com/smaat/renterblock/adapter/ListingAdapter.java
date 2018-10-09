package com.smaat.renterblock.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ListingActivity;
import com.smaat.renterblock.util.TypefaceSingleton;

public class ListingAdapter extends BaseAdapter {
	private Context mContext;
	private int width, newWidth;
	ArrayList<PropertyEntity> mListingResultList;

	public ListingAdapter(Context context,
			ArrayList<PropertyEntity> listingResultList) {
		mContext = context;
		mListingResultList = listingResultList;
		getDeviceHeight();
	}

	class Holder {

		private TextView mAddress, mReviewsCount;
		private RatingBar mRatingBar;
		private RelativeLayout mGridLay;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		holder = new Holder();
		PropertyEntity mPropertyEntity = mListingResultList.get(position);

		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(R.layout.adapter_listing, null);

		ViewGroup root = (ViewGroup) convertView
				.findViewById(R.id.parent_layout_listing_adapter);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				mContext);
		((BaseActivity) mContext).setFont(root, mTypeface);

		holder.mGridLay = (RelativeLayout) convertView
				.findViewById(R.id.grid_lay);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				newWidth, newWidth);
		holder.mGridLay.setLayoutParams(params);
		holder.mAddress = (TextView) convertView.findViewById(R.id.address);
		holder.mReviewsCount = (TextView) convertView
				.findViewById(R.id.reviews_count);
		holder.mRatingBar = (RatingBar) convertView
				.findViewById(R.id.review_ratingbar);

		holder.mAddress.setText(mPropertyEntity.getAddress());
		holder.mReviewsCount.setText("(" + mPropertyEntity.getReview_count()
				+ ")");

		float rating = Float.parseFloat(mPropertyEntity.getProperty_rating());
		holder.mRatingBar.setRating(rating);

		AQuery aq1 = new AQuery(mContext).recycle(convertView);
		if (mPropertyEntity.getProperty_pics().size() != 0) {
			aq1.id(R.id.building_image).image(
					mPropertyEntity.getProperty_pics().get(0).getFile(), true,
					true, 0, R.drawable.default_prop_icon, null, 0, 1.0f);
		}
		return convertView;
	}

	private void getDeviceHeight() {

		int padding = mContext.getResources().getDimensionPixelSize(
				R.dimen.margin15);

		DisplayMetrics metrics = new DisplayMetrics();
		((ListingActivity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		switch (metrics.densityDpi) {

		case DisplayMetrics.DENSITY_MEDIUM:
			width = metrics.widthPixels;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			width = metrics.widthPixels;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			width = metrics.widthPixels;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			width = metrics.widthPixels;
			break;

		}
		newWidth = width / 2;
		newWidth = newWidth - padding;
	}

	@Override
	public int getCount() {
		return mListingResultList.size();
	}

	@Override
	public Object getItem(int pos) {
		return mListingResultList.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return pos;
	}
}
