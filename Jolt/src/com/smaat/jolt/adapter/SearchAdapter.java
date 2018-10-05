package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;

public class SearchAdapter extends ArrayAdapter<ArrayList<ShopDetailsEntity>> {

	final String TAG = AppConstants.SEARCH_ADAPTER;

	Context mContext;
	int layoutResourceId;
	ArrayList<ShopDetailsEntity> mData = null;

	public SearchAdapter(Context mContext, int layoutResourceId,
			ArrayList<ShopDetailsEntity> data) {

		super(mContext, layoutResourceId);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.mData = data;
	}

	static class ViewHolder {
		TextView shopName, shopStreet;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {

			if (convertView == null) {
				// inflate the layout
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(layoutResourceId, parent, false);

				ViewHolder holder = new ViewHolder();

				holder.shopName = (TextView) convertView
						.findViewById(R.id.shopName);
				holder.shopName.setTypeface(HomeScreen.helveticaNeueLight);

				holder.shopStreet = (TextView) convertView
						.findViewById(R.id.shopStreet);
				holder.shopStreet.setTypeface(HomeScreen.helveticaNeueBold);

				convertView.setTag(holder);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();

			holder.shopName.setText(mData.get(position).getShopName());

			holder.shopStreet.setText(mData.get(position).getShopStreet());

		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return convertView;

	}

	@Override
	public int getCount() {
		return mData.size();
	}
}