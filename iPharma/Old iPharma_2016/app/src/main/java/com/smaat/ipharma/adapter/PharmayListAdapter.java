package com.smaat.ipharma.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.PharmacyList;

public class PharmayListAdapter extends BaseAdapter {
	String[] result;
	Context context;

	private static LayoutInflater inflater = null;

	public PharmayListAdapter(PharmacyList mainActivity, String[] prgmNameList,
							  int[] prgmImages) {
		result = prgmNameList;
		context = mainActivity;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public class Holder {
		TextView tv;
		ImageView img;
	}

	public int getCount() {
		return 20;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = new Holder();
		View rowView;
		rowView = inflater.inflate(R.layout.list_item_pharmacy, null);
		holder.tv = (TextView) rowView.findViewById(R.id.textView1);

		// holder.tv.setText("");
		return rowView;
	}
}
