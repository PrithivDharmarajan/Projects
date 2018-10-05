package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.HistoryDetailsEntity;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.GlobalMethods;

public class HistoryAdapter extends BaseAdapter {
	private Activity context;
	private ArrayList<HistoryDetailsEntity> mHistory_txt;

	public HistoryAdapter(Activity context,
			ArrayList<HistoryDetailsEntity> mHistory) {
		this.context = context;
		this.mHistory_txt = mHistory;
	}

	public static class holder {
		TextView mDateTime, mSelectedDrinks, mShopId, mSelectedDrinkSize;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {

			LayoutInflater inflater = context.getLayoutInflater();
			convertView = inflater.inflate(R.layout.myhistory_adapter, null,
					true);

			holder mholder = new holder();

			mholder.mDateTime = (TextView) convertView
					.findViewById(R.id.date_time);
			mholder.mSelectedDrinks = (TextView) convertView
					.findViewById(R.id.selected_drinks);
			mholder.mShopId = (TextView) convertView.findViewById(R.id.shop_id);
			mholder.mSelectedDrinkSize = (TextView) convertView
					.findViewById(R.id.selected_drink_size);

			mholder.mSelectedDrinks.setTypeface(HomeScreen.helveticaNeueBold);
			mholder.mDateTime.setTypeface(HomeScreen.helveticaNeueLight);
			mholder.mShopId.setTypeface(HomeScreen.helveticaNeueLight);
			mholder.mSelectedDrinkSize
					.setTypeface(HomeScreen.helveticaNeueMedium);
			convertView.setTag(mholder);
		}
		holder mholder = (holder) convertView.getTag();
		mholder.mDateTime.setText(GlobalMethods.getDisplayFormat(mHistory_txt
				.get(position).getDatetime()));
		mholder.mSelectedDrinks.setText(mHistory_txt.get(position)
				.getSelectedDrinks());
		mholder.mShopId.setText(mHistory_txt.get(position).getShopName());
		mholder.mSelectedDrinkSize.setText(mHistory_txt.get(position)
				.getNoOfDrinks());
		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mHistory_txt.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
