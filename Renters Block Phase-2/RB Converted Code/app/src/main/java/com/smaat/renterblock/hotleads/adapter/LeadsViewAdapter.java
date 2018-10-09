package com.smaat.renterblock.hotleads.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.hotleads.entity.LeadsActiveEntity;
import com.smaat.renterblock.hotleads.ui.HotLeadsMainScreen;
import com.smaat.renterblock.hotleads.ui.LeadsActivity;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.BaseActivity.GsonTransformer;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class LeadsViewAdapter extends ArrayAdapter<List<String>> implements
		DialogMangerCallback {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<LeadsActiveEntity> act_ent = new ArrayList<LeadsActiveEntity>();
	AQuery aq1;

	public LeadsViewAdapter(Context context, int layout,
			ArrayList<LeadsActiveEntity> leads_active_aray) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = leads_active_aray;
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {
		TextView friends, reviews, photos, username, time, mRequestInfoTxt;
		RatingBar rating;
		ImageView profile_pic;
		Button send_message, add_as_friend, send_message_list;
		LinearLayout add_friend_send_msg_lay, send_msg_lay;

		Button flame_icon, fav_icon, binoc_icon, send_chat_msg;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);
		aq1 = new AQuery(context).recycle(view);

		ViewGroup mGroup = (ViewGroup) view.findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				context);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(context);
		((BaseActivity) context).setFont(mGroup, mTypefaceBold);

		holder.flame_icon = (Button) view.findViewById(R.id.flame_icon);
		holder.fav_icon = (Button) view.findViewById(R.id.fav_icon);
		holder.binoc_icon = (Button) view.findViewById(R.id.binocular_icon);

		holder.mRequestInfoTxt = (TextView) view.findViewById(R.id.requset_nfo);
		holder.mRequestInfoTxt.setVisibility(View.GONE);
		holder.flame_icon.setVisibility(View.GONE);
		holder.fav_icon.setVisibility(View.GONE);
		holder.binoc_icon.setVisibility(View.GONE);

		if (act_ent.get(position).getIsActive().equalsIgnoreCase("1")) {
			holder.mRequestInfoTxt.setVisibility(View.VISIBLE);
		}
		holder.send_chat_msg = (Button) view
				.findViewById(R.id.send_message_chat);

		holder.send_chat_msg.setTag(position);

		int view_count = Integer.parseInt(act_ent.get(position).getCount());
		if (view_count >= 3) {
			holder.flame_icon.setVisibility(View.VISIBLE);
			holder.binoc_icon.setVisibility(View.VISIBLE);
		}

		if (act_ent.get(position).getIsfavourite().equalsIgnoreCase("1")) {
			holder.fav_icon.setVisibility(View.VISIBLE);
		}

		holder.friends = (TextView) view.findViewById(R.id.friends_count);
		holder.reviews = (TextView) view.findViewById(R.id.reviews_count);
		holder.photos = (TextView) view.findViewById(R.id.photos_count);
		holder.username = (TextView) view.findViewById(R.id.user_name);
		holder.rating = (RatingBar) view
				.findViewById(R.id.client_review_ratingbar);
		holder.time = (TextView) view.findViewById(R.id.time_status);
		holder.send_message = (Button) view.findViewById(R.id.send_message);
		holder.add_as_friend = (Button) view.findViewById(R.id.add_as_friend);

		holder.send_message.setTypeface(mTypefaceBold);
		holder.add_as_friend.setTypeface(mTypefaceBold);

		holder.send_msg_lay = (LinearLayout) view
				.findViewById(R.id.send_message_lay);
		holder.send_message_list = (Button) view
				.findViewById(R.id.send_message_list);
		holder.send_message_list.setTag(position);

		holder.send_message.setTag(position);
		holder.add_as_friend.setTag(position);

		if (act_ent.get(position).getIs_friend().equals("1")) {
			holder.add_as_friend.setText("Already added as Friend");
		}
		final String UserID = GlobalMethods.getUserID(context);
		holder.friends.setText(act_ent.get(position).getFriends_count());
		holder.reviews.setText(act_ent.get(position).getReviews_count());
		holder.photos.setText(act_ent.get(position).getPhotos_count());
		holder.username.setText(act_ent.get(position).getUser_name());
		try {
			holder.rating.setRating(Float.valueOf(act_ent.get(position)
					.getUser_avg_rating()));
		} catch (Exception e) {
			holder.rating.setRating(Float.valueOf("0"));
		}
		holder.time.setText(GlobalMethods.checkTimeLeft(act_ent.get(position)
				.getDatetime(), HotLeadsMainScreen.Current_server_time
				.toString()));

		// holder.time.setText("21 hours left");

		holder.send_chat_msg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				if (act_ent.get(pos).getRb_user().equalsIgnoreCase("1")) {
//					Intent intent = new Intent(context, FriendChatScreen.class);
//					intent.putExtra("ids", act_ent.get(pos).getUserId());
//					intent.putExtra("names", act_ent.get(pos).getUser_name());
//					intent.putExtra("groupId", "");
//					intent.putExtra("type", "group");
//					intent.putExtra("enhanced_profile_ids", "1");
//					intent.putExtra("from_call", "hotleads");
//					context.startActivity(intent);
					callGroupIdService(UserID, act_ent.get(pos).getUserId(),
							pos);
				}
			}
		});

		holder.add_as_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				LeadsActivity.sendFriendRequest(act_ent.get(pos).getUserId());
				// act_ent.get(pos).setIs_friend("1");
			}
		});

		aq1.id(R.id.profile_img)
				.progress(R.id.progre)
				.image(act_ent.get(position).getUser_profileImage(), false,
						false, 0, R.drawable.default_prop_icon);

		return view;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}

	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub

	}
	
	private void callGroupIdService(String UserID, String Friend_UserID,
			final int pos) {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", Friend_UserID);
		params.put("name", "");
		aq1.transformer(t)
				.progress(DialogManager.getProgressDialog(context))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									ChatSendResponse obj = new Gson().fromJson(
											json.toString(),
											ChatSendResponse.class);
									callChatService(obj.result, pos);
								} else {
									// statusErrorCode(status);
								}
							}
						});
	}

	private void callChatService(String group_id, int pos) {
		Intent intent = new Intent(context, FriendChatScreen.class);
		intent.putExtra("ids", act_ent.get(pos).getUserId());
		intent.putExtra("names", act_ent.get(pos).getUser_name());
		intent.putExtra("groupId", group_id);
		intent.putExtra("type", "group");
		intent.putExtra("enhanced_profile_ids", "1");
		if (act_ent.get(pos).getIs_friend().equalsIgnoreCase("1")) {
			intent.putExtra("from_call", "");
		} else {
			intent.putExtra("from_call", "hotleads");
		}
		context.startActivity(intent);
	}
}
