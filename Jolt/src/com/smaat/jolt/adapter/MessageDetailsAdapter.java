package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.JoltMessageDetailsEntity;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.GlobalMethods;

public class MessageDetailsAdapter extends BaseAdapter {

	private final Activity context;
	private ArrayList<JoltMessageDetailsEntity> mess_details;
	String mUserId;

	public MessageDetailsAdapter(Activity context,
			ArrayList<JoltMessageDetailsEntity> mMessageDetails) {

		this.context = context;
		this.mess_details = mMessageDetails;
		mUserId = GlobalMethods.getUserID(context);
	}

	public static class ViewHolder {
		TextView mLeftSideTxt, mRightSideTxt;
		ImageView mConversationImg;
		LinearLayout mLeftSideLay, mRightSideLay;

	}

	public View getView(int position, View rowView, ViewGroup parent) {

		if (rowView == null) {
			ViewHolder mholder = new ViewHolder();

			LayoutInflater inflater = context.getLayoutInflater();
			rowView = inflater.inflate(R.layout.chat, parent, false);

			mholder.mLeftSideLay = (LinearLayout) rowView
					.findViewById(R.id.left_side_lay);
			mholder.mRightSideLay = (LinearLayout) rowView
					.findViewById(R.id.right_side_lay);
			mholder.mLeftSideTxt = (TextView) rowView
					.findViewById(R.id.left_side_txt);
			mholder.mRightSideTxt = (TextView) rowView
					.findViewById(R.id.right_side_txt);
			mholder.mRightSideTxt.setTypeface(HomeScreen.helveticaNeueLight);
			mholder.mLeftSideTxt.setTypeface(HomeScreen.helveticaNeueLight);
			rowView.setTag(mholder);
		}

		ViewHolder mholder = (ViewHolder) rowView.getTag();
		if (mUserId.equalsIgnoreCase(mess_details.get(position).getUserID())) {
			mholder.mRightSideLay.setVisibility(View.VISIBLE);
			mholder.mLeftSideLay.setVisibility(View.GONE);
			mholder.mRightSideTxt.setText(mess_details.get(position)
					.getMessageText());
		} else {
			mholder.mLeftSideLay.setVisibility(View.VISIBLE);
			mholder.mRightSideLay.setVisibility(View.GONE);
			mholder.mLeftSideTxt.setText(mess_details.get(position)
					.getMessageText());
		}

		return rowView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mess_details.size();
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
	};
}
