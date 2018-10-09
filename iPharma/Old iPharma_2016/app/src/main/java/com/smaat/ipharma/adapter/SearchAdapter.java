package com.smaat.ipharma.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.ui.HomeScreen;

public class SearchAdapter extends ArrayAdapter<ArrayList<MapPropertyEntity>> {

	// final String TAG = AppConstants.SEARCH_ADAPTER;

	Context mContext;
	int layoutResourceId;
	ArrayList<MapPropertyEntity> mData = null;

	public SearchAdapter(Context mContext, int layoutResourceId,
			ArrayList<MapPropertyEntity> data) {

		super(mContext, layoutResourceId);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.mData = data;
	}

	static class ViewHolder {
		TextView shopName, shopStreet, shopAddress, shopCity, shopPincode;
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
				holder.shopStreet = (TextView) convertView
						.findViewById(R.id.shopStreet);

				holder.shopStreet.setVisibility(View.GONE);
				holder.shopAddress = (TextView) convertView
						.findViewById(R.id.shopAddress);
				holder.shopCity = (TextView) convertView
						.findViewById(R.id.shopCity);
				holder.shopPincode = (TextView) convertView
						.findViewById(R.id.shopPincode);

				holder.shopName.setTypeface(HomeScreen.mHelveticaBold);
				holder.shopStreet.setTypeface(HomeScreen.mHelveticaNormal);
				holder.shopAddress.setTypeface(HomeScreen.mHelveticaNormal);
				holder.shopCity.setTypeface(HomeScreen.mHelveticaNormal);
				holder.shopPincode.setTypeface(HomeScreen.mHelveticaLight);
			}

			ViewHolder holder = (ViewHolder) convertView.getTag();

			holder.shopName.setText(mData.get(position).getShopName());
			// holder.shopStreet.setText(mData.get(position).getArea());
			holder.shopAddress.setText(mData.get(position).getAddress());
			holder.shopCity.setText(mData.get(position).getCity());
			holder.shopPincode.setText(mData.get(position).getPincode());

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