package com.smaat.jolt.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.JoltMessageEntity;
import com.smaat.jolt.fragment.MessageUsFragment;
import com.smaat.jolt.fragment.NewConversationFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.squareup.picasso.Picasso;

public class MessageAdapter extends BaseAdapter {
	private final Activity context;
	private ArrayList<JoltMessageEntity> mess_ent;

	public MessageAdapter(Activity context, ArrayList<JoltMessageEntity> message) {
		this.context = context;
		this.mess_ent = message;
	}

	public static class ViewHolder {
		TextView mConversationtxt;
		ImageView mConversationImg;
		LinearLayout mMsgLay;

	}

	public View getView(int position, View view, ViewGroup parent) {

		if (view == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			view = inflater.inflate(R.layout.message_list_item, parent, false);
			ViewHolder mholder = new ViewHolder();
			mholder.mConversationtxt = (TextView) view
					.findViewById(R.id.converstation_txt);
			mholder.mConversationImg = (ImageView) view
					.findViewById(R.id.converstation_img);

			mholder.mMsgLay = (LinearLayout) view.findViewById(R.id.msg_lay);

			mholder.mConversationtxt.setTypeface(HomeScreen.helveticaNeueLight);
			view.setTag(mholder);

		}
		ViewHolder mholder = (ViewHolder) view.getTag();

		mholder.mConversationtxt.setText(mess_ent.get(position)
				.getMessageText());
		if (!mess_ent.get(position).getUserImageUrl().equals("")) {
			Picasso.with(context)
					.load(AppConstants.BASE_TIMTHUMB
							+ mess_ent.get(position).getUserImageUrl())
					.placeholder(
							context.getResources().getDrawable(
									R.drawable.default_sign_in))
					.error(context.getResources().getDrawable(
							R.drawable.default_sign_in))
					.into(mholder.mConversationImg);
		} else {
			mholder.mConversationImg
					.setImageResource(R.drawable.converstation_jolt_icon);
		}

		mholder.mMsgLay.setTag(mess_ent.get(position));
		mholder.mMsgLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle mBundle = new Bundle();
				JoltMessageEntity joltMsg = (JoltMessageEntity) v.getTag();

				mBundle.putString(AppConstants.MSG_ID, joltMsg.getMessageId());
				mBundle.putString(AppConstants.CONVERSATION_ID, joltMsg.getConversationID());
				mBundle.putString(AppConstants.IMAGE_ID, joltMsg.getUserImageUrl());

				HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
				HomeScreen.moveBackFragment = new MessageUsFragment();

				((HomeScreen) context).mFragment = new NewConversationFragment();
				((HomeScreen) context).mFragment.setArguments(mBundle);

				((HomeScreen) context).updateDisplayFragment(
						((HomeScreen) context).mFragment, true);

			}
		});

		return view;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mess_ent.size();
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