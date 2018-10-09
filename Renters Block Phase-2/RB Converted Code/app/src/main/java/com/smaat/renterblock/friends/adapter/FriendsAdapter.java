package com.smaat.renterblock.friends.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.FriendMainScreenEntity;
import com.smaat.renterblock.friends.ui.FriendsMainScreen;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends ArrayAdapter<List<String>> implements
		DialogMangerCallback {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<FriendMainScreenEntity> act_ent = new ArrayList<FriendMainScreenEntity>();
	private Dialog d2;
	private String type_of_chat;
	private String UserName;

	public FriendsAdapter(Context context, int layout,
			ArrayList<FriendMainScreenEntity> freinds_entity) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = freinds_entity;
		UserName = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_user_name);
	}

	static class Holder {
		TextView user_name, friend_count, review_Count, photo_count, status,
				online_status;
		RatingBar rating;
		ImageView profile_pic;
		RelativeLayout call_icon, frinds_main_lay;
		Button call_img_btn;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		// if (view == null) {
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);
		FriendMainScreenEntity fr_main;

		holder.user_name = (TextView) view.findViewById(R.id.user_name);
		holder.friend_count = (TextView) view.findViewById(R.id.friends_count);
		holder.review_Count = (TextView) view.findViewById(R.id.reviews_count);
		holder.photo_count = (TextView) view.findViewById(R.id.photos_count);
		holder.status = (TextView) view.findViewById(R.id.status);
		holder.online_status = (TextView) view.findViewById(R.id.online_status);
		holder.rating = (RatingBar) view
				.findViewById(R.id.client_review_ratingbar);
		holder.profile_pic = (ImageView) view.findViewById(R.id.profile_img);
		holder.call_icon = (RelativeLayout) view.findViewById(R.id.call_icon);
		holder.call_img_btn = (Button) view.findViewById(R.id.call_btn);
		holder.call_img_btn.setTag(position);
		holder.call_icon.setTag(position);
		holder.profile_pic.setTag(position);
		holder.status.setText(act_ent.get(position).getIsnew());

		if (act_ent.get(position).getIsnew().equals("Pending")) {
			holder.call_icon.setVisibility(View.GONE);
		} else {
			holder.call_icon.setVisibility(View.VISIBLE);
		}
		if (act_ent.get(position).getAccept() != null
				&& act_ent.get(position).getAccept().contains("3")) {
			String accept[] = act_ent.get(position).getAccept().split(",");
			if (accept.length >= 2) {
				if (accept[1].equalsIgnoreCase(act_ent.get(position)
						.getUser_friend_id())) {
					holder.status.setText("");
				} else {
					holder.status.setText("Blocked");
					holder.call_icon.setVisibility(View.GONE);
				}
			}
		}
		holder.frinds_main_lay = (RelativeLayout) view
				.findViewById(R.id.frinds_main_lay);
		holder.frinds_main_lay.setTag(position);
		holder.frinds_main_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				if (act_ent.get(pos).getIsnew()
						.equalsIgnoreCase("Pending")) {
				} else {
					if (act_ent.get(pos).getIsnew()
							.equalsIgnoreCase("Block")) {
						String accept[] = act_ent.get(pos).getAccept()
								.split(",");
						if (accept.length >= 2) {
							if (accept[1].equalsIgnoreCase(act_ent
									.get(pos).getUser_friend_id())) {
								AppConstants.friend_block_user = "true";
								AppConstants.friend_block_username = act_ent
										.get(pos).getUser_name();
							}
						}
						FriendsMainScreen.callGroupchatid(act_ent.get(pos)
								.getUser_friend_id(), act_ent.get(pos)
								.getUser_name());
					} else {
						String accept[] = act_ent.get(pos).getAccept()
								.split(",");
						if (accept.length >= 2) {
							if (accept[1].equalsIgnoreCase(act_ent
									.get(pos).getUser_friend_id())) {
								AppConstants.friend_block_user = "true";
								AppConstants.friend_block_username = act_ent
										.get(pos).getUser_name();
							}
							FriendsMainScreen.callGroupchatid(act_ent.get(pos)
									.getUser_friend_id(), act_ent.get(pos)
									.getUser_name());
						} else {
							FriendsMainScreen.callGroupchatid(act_ent.get(pos)
									.getUser_friend_id(), act_ent.get(pos)
									.getUser_name());
						}
					}
				}

			}
		});

		holder.call_img_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				if (act_ent.get(pos).getEnhanced_profile().equals("1")) {
					String accept[] = act_ent.get(position).getAccept()
							.split(",");
					if (accept.length >= 2) {
						if (accept[1].equalsIgnoreCase(act_ent.get(position)
								.getUser_friend_id())) {
							DialogManager
									.showCustomAlertDialog(
											context,
											FriendsAdapter.this,
											act_ent.get(pos).getUser_name()
													+ " not available to receive your call. Please try again later.");
						} else {
							showCalloptionDialog(act_ent.get(pos)
									.getUser_name(), act_ent.get(pos)
									.getUser_friend_id());
						}
					} else {
						showCalloptionDialog(act_ent.get(pos).getUser_name(),
								act_ent.get(pos).getUser_friend_id());
					}
				} else {
					DialogManager.showCustomAlertDialog(context,
							FriendsAdapter.this,
							context.getString(R.string.don_make_call));
				}
			}
		});

		holder.call_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				if (act_ent.get(pos).getEnhanced_profile().equals("1")) {
					showCalloptionDialog(act_ent.get(pos).getUser_name(),
							act_ent.get(pos).getUser_friend_id());
				} else {
					DialogManager.showCustomAlertDialog(context,
							FriendsAdapter.this,
							context.getString(R.string.don_make_call));
				}
			}
		});

		holder.profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				Intent profile = new Intent(context, ProfileScreen.class);
				profile.putExtra("user_id", act_ent.get(pos)
						.getUser_friend_id());
				profile.putExtra("call_from", "Agent");
				context.startActivity(profile);
			}
		});

		fr_main = act_ent.get(position);
		holder.user_name.setText(act_ent.get(position).getUser_name());
		holder.friend_count.setText(act_ent.get(position).getFriends());
		holder.photo_count.setText(act_ent.get(position).getPhotos());
		holder.review_Count.setText(act_ent.get(position).getReview());
		holder.rating.setRating(Float
				.valueOf(act_ent.get(position).getRating()));
		if (act_ent.get(position).getStatus().equals("1")) {
			holder.online_status.setVisibility(View.VISIBLE);
		} else {
			holder.online_status.setVisibility(View.GONE);
		}
		AQuery aq1 = new AQuery(context).recycle(view);
		aq1.id(R.id.profile_img)
				.progress(R.id.img_prg)
				.image(fr_main.getUser_pic(), false, false, 0,
						R.drawable.profile_pic);
		return view;
	}

	private void showCalloptionDialog(final String user_name,
			final String friend_id) {
		d2 = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);

		d2.setContentView(R.layout.friends_call_option_dialog);
		d2.setCancelable(true);

		Button voice_call = (Button) d2.findViewById(R.id.voice_call);
		Button video_call = (Button) d2.findViewById(R.id.video_call);

		voice_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Voice Call
//				AppConstants.Type_of_call = "voice";
//				type_of_chat = "voice";
//				// ShowFriendsDialog("voice_call");
//				AppConstants.Type_of_call = "voice";
//				Intent video_chat = new Intent(context,
//						VideoChatMainActivity.class);
//				FriendsMainScreen.type_of_chat = "voice";
//
//				// [request setPostValue:FriendId forKey:@"friend_id"];
//				// [request setPostValue:Subject forKey:@"subject"];
//				// [request setPostValue:UserName forKey:@"username"];
//				// [request setPostValue:Type forKey:@"type"];
//
//				video_chat.putExtra("user_id", GlobalMethods.getUserID(context));
//				video_chat.putExtra("friend_id", friend_id);
//				video_chat.putExtra("username", UserName);
//				video_chat.putExtra("type", "voice");
//				video_chat.putExtra("subject", "voice");
//
//				// video_chat.putExtra("name", user_name);
//				// video_chat.putExtra("group_id", friend_id);
//				// video_chat.putExtra("call_from", "friends");
//				context.startActivity(video_chat);
				d2.dismiss();
			}
		});

		video_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Video Call
//				AppConstants.Type_of_call = "video";
//				type_of_chat = "video";
//				// ShowFriendsDialog("video_call");
//				AppConstants.Type_of_call = "video";
//				Intent video_chat = new Intent(context,
//						VideoChatMainActivity.class);
//				FriendsMainScreen.type_of_chat = "video";
//				// video_chat.putExtra("name", user_name);
//				// video_chat.putExtra("group_id", friend_id);
//				// video_chat.putExtra("call_from", "friends");
//
//				video_chat.putExtra("user_id", GlobalMethods.getUserID(context));
//				video_chat.putExtra("friend_id", friend_id);
//				video_chat.putExtra("username", UserName);
//				video_chat.putExtra("type", "video");
//				video_chat.putExtra("subject", "video");
//
//				context.startActivity(video_chat);
				d2.dismiss();
			}
		});

		d2.show();
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

		return dir.delete();
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
