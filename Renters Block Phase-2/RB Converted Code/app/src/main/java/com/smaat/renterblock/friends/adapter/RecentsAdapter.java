package com.smaat.renterblock.friends.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.FriendMainScreenEntity;

public class RecentsAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<FriendMainScreenEntity> act_ent = new ArrayList<FriendMainScreenEntity>();

	public RecentsAdapter(Context context, int layout,
			ArrayList<FriendMainScreenEntity> freinds_entity) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = freinds_entity;
		// this.act_ent = user_details;
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {
		TextView user_name, friend_count, review_Count, photo_count, status,
				chat_hist;
		ImageView profile_pic;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);

		FriendMainScreenEntity fr_main;

		holder.user_name = (TextView) view.findViewById(R.id.user_name);
		holder.friend_count = (TextView) view.findViewById(R.id.friends_count);
		holder.review_Count = (TextView) view.findViewById(R.id.reviews_count);
		holder.photo_count = (TextView) view.findViewById(R.id.photos_count);
		// holder.status = (TextView) view.findViewById(R.id.status);
		holder.chat_hist = (TextView) view.findViewById(R.id.chat_list);
		holder.profile_pic = (ImageView) view.findViewById(R.id.profile_img);

		fr_main = act_ent.get(position);
		holder.user_name.setText(act_ent.get(position).getFirst_name());
		holder.friend_count.setText(act_ent.get(position).getFriends());
		holder.photo_count.setText(act_ent.get(position).getPhotos());
		holder.review_Count.setText(act_ent.get(position).getReview());
		// holder.status.setText(act_ent.get(position).getIsnew());
		holder.chat_hist.setText("Test chat..");
		AQuery aq1 = new AQuery(context).recycle(view);
		aq1.id(R.id.profile_img)
				.progress(R.id.progre)
				.image(fr_main.getUser_pic(), false, false, 0,
						R.drawable.profile_pic);
		return view;
	}

	public static void deleteCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
}
