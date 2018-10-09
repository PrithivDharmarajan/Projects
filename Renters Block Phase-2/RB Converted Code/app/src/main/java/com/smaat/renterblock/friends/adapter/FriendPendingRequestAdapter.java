package com.smaat.renterblock.friends.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.FriendDetailsEntity;
import com.smaat.renterblock.friends.ui.FriendsPendingRequest;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FriendPendingRequestAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;

	private ArrayList<FriendDetailsEntity> act_ent = new ArrayList<FriendDetailsEntity>();
	Typeface helvetica_normal, helvetica_bold, helvetica_light;

	public FriendPendingRequestAdapter(Context context, int layout,
			ArrayList<FriendDetailsEntity> fre_ent) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = fre_ent;

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	static class Holder {

		TextView user_name, friend_count, review_Count, photo_count, accept,
				reject;
		RatingBar rating;
		ImageView profile_pic;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);

		FriendDetailsEntity fr_main;

		holder.user_name = (TextView) view.findViewById(R.id.user_name);
		holder.friend_count = (TextView) view.findViewById(R.id.friends_count);
		holder.review_Count = (TextView) view.findViewById(R.id.reviews_count);
		holder.photo_count = (TextView) view.findViewById(R.id.photos_count);
		holder.rating = (RatingBar) view
				.findViewById(R.id.client_review_ratingbar);
		holder.profile_pic = (ImageView) view.findViewById(R.id.profile_img);
		holder.profile_pic.setTag(position);
		holder.profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				Intent profile = new Intent(context, ProfileScreen.class);
				profile.putExtra("user_id", act_ent.get(pos)
						.getUser_friend_id());
				profile.putExtra("call_from", "Agent");
				context.startActivity(profile);
			}
		});
		holder.accept = (TextView) view.findViewById(R.id.accept_friend);
		holder.reject = (TextView) view.findViewById(R.id.reject_friend);

		holder.accept.setTypeface(helvetica_bold);
		holder.reject.setTypeface(helvetica_bold);

		holder.accept.setTag(position);
		holder.reject.setTag(position);

		fr_main = act_ent.get(position);
		holder.user_name.setText(act_ent.get(position).getUser_name());
		holder.friend_count.setText(act_ent.get(position).getFriends());
		holder.photo_count.setText(act_ent.get(position).getPhotos());
		holder.review_Count.setText(act_ent.get(position).getReview());
		holder.rating.setRating(Float
				.valueOf(act_ent.get(position).getRating()));
		AQuery aq1 = new AQuery(context).recycle(view);
		aq1.id(R.id.profile_img)
				.progress(R.id.img_progress)
				.image(fr_main.getUser_pic(), false, false, 0,
						R.drawable.profile_pic);

		holder.accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				String friend_id = act_ent.get(pos).getUser_friend_id();
				FriendsPendingRequest.acceptFriendRequest(friend_id);
			}
		});

		holder.reject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				String friend_id = act_ent.get(pos).getUser_friend_id();
				FriendsPendingRequest.rejectFriendRequest(friend_id);
			}
		});
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
}
