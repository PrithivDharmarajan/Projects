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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.entity.SendRequestEntity;
import com.smaat.renterblock.friends.ui.FriendSearchScreen;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.TypefaceSingleton;

public class SendFriendRequestAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<SendRequestEntity> act_ent = new ArrayList<SendRequestEntity>();
	Typeface helvetica_normal, helvetica_bold, helvetica_light;
	
	public SendFriendRequestAdapter(Context context, int layout,
			ArrayList<SendRequestEntity> send_req_ent) {
		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = send_req_ent;
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(context);
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {
		TextView user_name, friend_count, review_Count, photo_count, status;
		RatingBar rating;
		Button send_req;
		ImageView profile_pic;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		// if (view == null) {
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);
		SendRequestEntity fr_main;

		holder.user_name = (TextView) view.findViewById(R.id.user_name);
		holder.friend_count = (TextView) view.findViewById(R.id.friends_count);
		holder.review_Count = (TextView) view.findViewById(R.id.reviews_count);
		holder.photo_count = (TextView) view.findViewById(R.id.photos_count);
		holder.status = (TextView) view.findViewById(R.id.status);
		holder.send_req = (Button) view.findViewById(R.id.send_request);
		holder.send_req.setVisibility(View.VISIBLE);
		holder.send_req.setTag(position);
		holder.send_req.setTypeface(helvetica_bold);
		holder.status.setVisibility(View.GONE);
		holder.rating = (RatingBar) view
				.findViewById(R.id.client_review_ratingbar);
		holder.profile_pic = (ImageView) view.findViewById(R.id.profile_img);
		holder.profile_pic.setTag(position);

		holder.profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				Intent profile = new Intent(context, ProfileScreen.class);
				profile.putExtra("user_id", act_ent.get(pos).getUser_id());
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
		AQuery aq1 = new AQuery(context).recycle(view);
		aq1.id(R.id.profile_img)
				.progress(R.id.img_prg)
				.image(fr_main.getUser_pic(), false, false, 0,
						R.drawable.profile_pic);

		holder.send_req.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = Integer.parseInt(String.valueOf(v.getTag()));
				FriendSearchScreen.callSendRequestService(act_ent.get(pos)
						.getUser_id());
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
