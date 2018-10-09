package com.smaat.renterblock.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.PropertyEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.NonSwipeableViewPager;

public class HomeScreenListAdapter extends BaseAdapter {
	private Context mContext;

	private ArrayList<HashMap<Integer, ArrayList<String>>> mImageUrls;

//	private int mViewPagerPosition;
	private ArrayList<PropertyEntity> mPropertyList;

	public HomeScreenListAdapter(Context context,
			ArrayList<HashMap<Integer, ArrayList<String>>> imageUrls,
			int listViewPosition, int viewPagerPosition,
			ArrayList<PropertyEntity> propList) {
		super();
		mContext = context;
		mImageUrls = imageUrls;
//		mViewPagerPosition = viewPagerPosition;
		mPropertyList = propList;
	}

	class Holder {
		ViewPager pager;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.home_list_adapter, parent,
				false);

		NonSwipeableViewPager pager = (NonSwipeableViewPager) convertView
				.findViewById(R.id.pager);
		pager.setId(position);
		pager.setOffscreenPageLimit(5);

		HashMap<Integer, ArrayList<String>> hashMap = mImageUrls.get(position);

		try {
			CustomPagerAdapter adapter = new CustomPagerAdapter(mContext,
					hashMap.get(0), position, mPropertyList.get(position));

			pager.setAdapter(adapter);
			pager.setCurrentItem(AppConstants.mViewPagerPosition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return mImageUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
