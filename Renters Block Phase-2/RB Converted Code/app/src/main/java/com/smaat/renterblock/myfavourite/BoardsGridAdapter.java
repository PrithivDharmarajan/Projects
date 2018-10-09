package com.smaat.renterblock.myfavourite;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.BoardsDetails;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;

public class BoardsGridAdapter extends BaseAdapter {
	private Context mContext;
	private int mLayoutId;
	private AQuery mAq;
	private View cell;
	ArrayList<String> imageUrls = null;
	// private ViewPager pager;
	private Holder holder;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	Bitmap bitmap[];
	DisplayMetrics dm;
	ArrayList<BoardsDetails> mBoardDetailss;

	public BoardsGridAdapter(Context context, int layout,
			ArrayList<BoardsDetails> mBoardDetails) {
		super();
		mContext = context;
		mLayoutId = layout;
		this.mBoardDetailss = mBoardDetails;
	}

	class Holder {
		ViewPager pager;
		private TextView mAddress, mReviewsCount;
		private RatingBar mRating;
		private ImageView mPropertyImg;
		private RelativeLayout grid_main_lay;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = null;

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		holder = new Holder();
		convertView = inflater.inflate(mLayoutId, parent, false);

		holder.mAddress = (TextView) convertView.findViewById(R.id.address);
		holder.mReviewsCount = (TextView) convertView
				.findViewById(R.id.reviews);
		holder.mRating = (RatingBar) convertView
				.findViewById(R.id.review_ratingbar);
		holder.mPropertyImg = (ImageView) convertView
				.findViewById(R.id.building_image);
		holder.grid_main_lay = (RelativeLayout) convertView
				.findViewById(R.id.grid_main_lay);

		holder.grid_main_lay.setTag(position);

		holder.grid_main_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				showMyBoardsAdapter(mBoardDetailss.get(pos).getProperty_id(),
						mBoardDetailss.get(pos).getEnhanced_profile());
				String UserID = GlobalMethods.getUserID(mContext);
				if (UserID.equalsIgnoreCase(mBoardDetailss.get(pos)
						.getProperty_details().get(0).getUser_id())) {
					AppConstants.user_property = "1";
				} else {
					AppConstants.user_property = "0";
				}
			}
		});

		if (mBoardDetailss != null
				&& (mBoardDetailss.get(position).getProperty_details() != null && !mBoardDetailss
						.get(position).getProperty_details().isEmpty())) {
			holder.mAddress.setText(mBoardDetailss.get(position)
					.getProperty_details().get(0).getAddress());
			holder.mReviewsCount.setText("("
					+ mBoardDetailss.get(position).getReview_count() + ")");

			AQuery aq = new AQuery(mContext);
			mAq = aq.recycle(convertView);

			if (mBoardDetailss.get(position).getProperty_pics().isEmpty()) {
				holder.mPropertyImg.setBackgroundResource(R.drawable.house_two);
			} else {
				mAq.id(R.id.building_image)
						.progress(R.id.progress1)
						.image(mBoardDetailss.get(position).getProperty_pics()
								.get(0).getFile(), true, true, 0,
								R.drawable.default_prop_icon, null, 0, 1.0f);
			}
			float propertyRating = (float) 0.0;
			try {
				propertyRating = Float.parseFloat(mBoardDetailss.get(position)
						.getProperty_rating());
			} catch (Exception e) {
				propertyRating = (float) 0.0;
			}
			holder.mRating.setRating(propertyRating);
		}
		return convertView;
	}

	public void showMyBoardsAdapter(String prop_id, String isEnhance) {
		Intent inte = new Intent(mContext, BoardsChatActivity.class);
		inte.putExtra("property_id", prop_id);
		inte.putExtra("isEnhance", isEnhance);
		((Activity) mContext).overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
		mContext.startActivity(inte);
	}

	@Override
	public int getCount() {
		if (mBoardDetailss != null) {
			return mBoardDetailss.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mBoardDetailss.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
