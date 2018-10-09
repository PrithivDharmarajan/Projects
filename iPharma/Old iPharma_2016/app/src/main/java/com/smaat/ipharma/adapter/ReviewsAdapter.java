package com.smaat.ipharma.adapter;

import java.util.ArrayList;

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.ShowReviewMessageEntity;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.GlobalMethods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReviewsAdapter extends BaseAdapter {
	private int mLayout;
	private Context mContext;
	private ArrayList<ShowReviewMessageEntity> reviews_ent;

	public ReviewsAdapter(Context context, int layout,
			ArrayList<ShowReviewMessageEntity> review_list) {
		mContext = context;
		mLayout = layout;
		reviews_ent = review_list;
	}

	class Holder {

		TextView mReviewHeading, mReviewDesc, mUserName, mDateTime;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return reviews_ent.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(mLayout, parent, false);
		holder.mUserName = (TextView) convertView.findViewById(R.id.user_name);
		holder.mUserName.setTypeface(HomeScreen.mHelveticaBold);
		holder.mReviewHeading = (TextView) convertView
				.findViewById(R.id.review_heading);
		holder.mReviewHeading.setTypeface(HomeScreen.mHelveticaNormal);
		holder.mReviewDesc = (TextView) convertView
				.findViewById(R.id.review_desc);
		holder.mReviewDesc.setTypeface(HomeScreen.mHelveticaNormal);
		holder.mDateTime = (TextView) convertView.findViewById(R.id.datetime);
		holder.mDateTime.setTypeface(HomeScreen.mHelveticaNormal);

		holder.mUserName.setText(reviews_ent.get(position).getFullName());
//		holder.mReviewHeading.setText(reviews_ent.get(position)
//				.getReviewHeading());
		holder.mReviewDesc
				.setText(reviews_ent.get(position).getReviewComment());
//		SimpleDateFormat sourceFormate = new SimpleDateFormat(
//				"yyyy-mm-dd hh:mm:ss");
//		SimpleDateFormat destinyFormate = new SimpleDateFormat(
//				"dd-mm-yyyy, hh a");
		String formattedDate = GlobalMethods.getFormatedDate(
				reviews_ent.get(position).getReviewDateTime(),
				"yyyy-MM-dd hh:mm:ss", "dd-MM-yyyy, hh a");
		holder.mDateTime.setText(formattedDate);

		return convertView;
	}

}
