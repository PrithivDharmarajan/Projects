package com.smaat.renterblock.friends.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.FriendsRecentsEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FriendsRecentsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<FriendsRecentsEntity> mFriendsRecentsEntityList;
	private ArrayList<String> Names = new ArrayList<String>();
	private String UserName;

	public FriendsRecentsAdapter(Context context,
			ArrayList<FriendsRecentsEntity> friendsRecentsEntityList) {
		mContext = context;
		mFriendsRecentsEntityList = friendsRecentsEntityList;
		UserName = (String) GlobalMethods.getValueFromPreference(mContext,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_user_name);
	}

	class Holder {
		private TextView mNameTxt, mMessageTxt, mDatetimeTxt;
		private RelativeLayout two_user_img_lay, three_user_img_lay,
				four_user_img_lay;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		holder = new Holder();
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = mLayoutInflater.inflate(
				R.layout.adapter_friends_recents_list, null);
		ViewGroup mGroup = (ViewGroup) convertView
				.findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(mContext);
		((BaseActivity) mContext).setFont(mGroup, mTypefaceBold);
		holder.mNameTxt = (TextView) convertView.findViewById(R.id.name_txt);
		holder.mMessageTxt = (TextView) convertView
				.findViewById(R.id.message_txt);

		holder.two_user_img_lay = (RelativeLayout) convertView
				.findViewById(R.id.two_user_img_lay);
		holder.three_user_img_lay = (RelativeLayout) convertView
				.findViewById(R.id.three_user_img_lay);
		holder.four_user_img_lay = (RelativeLayout) convertView
				.findViewById(R.id.four_user_img_lay);

		holder.mDatetimeTxt = (TextView) convertView
				.findViewById(R.id.date_time);

		holder.mDatetimeTxt.setText(GlobalMethods
				.recentsTimeCalculation(mFriendsRecentsEntityList.get(position)
						.getSend_time()));

		holder.two_user_img_lay.setVisibility(View.GONE);
		holder.three_user_img_lay.setVisibility(View.GONE);
		holder.four_user_img_lay.setVisibility(View.GONE);

		AQuery aq = new AQuery(mContext).recycle(convertView);
		Names.clear();
		if (mFriendsRecentsEntityList.size() != 0) {
			FriendsRecentsEntity mRecentsEntity = mFriendsRecentsEntityList
					.get(position);
			if (mRecentsEntity.getType().equals("group")) {
				if (mRecentsEntity.getGroupname() != null
						&& mRecentsEntity.getGroupname().equals("")) {
					if (mRecentsEntity.getUserdetails().size() != 0) {
						for (int i = 0; i < mRecentsEntity.getUserdetails()
								.size(); i++) {
							if (!UserName.equalsIgnoreCase(mRecentsEntity
									.getUserdetails().get(i).getUser_name())) {
								Names.add(mRecentsEntity.getUserdetails()
										.get(i).getUser_name());
							}
						}

						String mString = Names.toString().replace("[", "")
								.replace("]", "");
						holder.mNameTxt.setText(mString);
					}
				} else {
					holder.mNameTxt.setText(mRecentsEntity.getGroupname()
							.toString());
				}
			} else {
				holder.mNameTxt.setText(mRecentsEntity.getMeeting_subject());
			}
			if (UserName.equalsIgnoreCase(mRecentsEntity.getUser_name())) {
				holder.mMessageTxt.setText("You: "
						+ mRecentsEntity.getMessage());
			} else {
				holder.mMessageTxt.setText(mRecentsEntity.getUser_name() + ": "
						+ mRecentsEntity.getMessage());
			}

			if (mRecentsEntity.getUserdetails().size() != 0) {
				if (mRecentsEntity.getUserdetails().size() == 2) {
					holder.two_user_img_lay.setVisibility(View.VISIBLE);
					holder.three_user_img_lay.setVisibility(View.GONE);
					holder.four_user_img_lay.setVisibility(View.GONE);
					aq.id(R.id.two_recent_img1).image(
							mRecentsEntity.getUserdetails().get(0)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.two_recent_img2).image(
							mRecentsEntity.getUserdetails().get(1)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
				} else if (mRecentsEntity.getUserdetails().size() == 3) {
					holder.two_user_img_lay.setVisibility(View.GONE);
					holder.three_user_img_lay.setVisibility(View.VISIBLE);
					holder.four_user_img_lay.setVisibility(View.GONE);
					aq.id(R.id.three_recent_img1).image(
							mRecentsEntity.getUserdetails().get(0)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.three_recent_img2).image(
							mRecentsEntity.getUserdetails().get(1)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.three_recent_img3).image(
							mRecentsEntity.getUserdetails().get(2)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
				} else if (mRecentsEntity.getUserdetails().size() > 3) {
					holder.two_user_img_lay.setVisibility(View.GONE);
					holder.three_user_img_lay.setVisibility(View.GONE);
					holder.four_user_img_lay.setVisibility(View.VISIBLE);
					aq.id(R.id.four_recent_img1).image(
							mRecentsEntity.getUserdetails().get(0)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.four_recent_img2).image(
							mRecentsEntity.getUserdetails().get(1)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.four_recent_img3).image(
							mRecentsEntity.getUserdetails().get(2)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
					aq.id(R.id.four_recent_img4).image(
							mRecentsEntity.getUserdetails().get(3)
									.getUserimage(), false, false, 200,
							R.drawable.profile_pic, null, 0);
				}

			}
		}

		return convertView;
	}

	@Override
	public int getCount() {
		return mFriendsRecentsEntityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mFriendsRecentsEntityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
