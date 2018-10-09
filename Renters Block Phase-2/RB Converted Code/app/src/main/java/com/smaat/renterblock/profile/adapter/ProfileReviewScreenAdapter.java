package com.smaat.renterblock.profile.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.profile.Entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.ui.ProfilePhotoVideoScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.util.GlobalMethods;

public class ProfileReviewScreenAdapter extends ArrayAdapter<List<String>> {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<ProfileMyFeedsEntity> act_ent = new ArrayList<ProfileMyFeedsEntity>();
	AQuery aq1, aq2;

	public ProfileReviewScreenAdapter(Context context, int layout,
			ArrayList<ProfileMyFeedsEntity> review_list) {

		super(context, layout);
		this.context = context;
		this.layout = layout;
		this.act_ent = review_list;
	}

	@Override
	public int getCount() {
		return act_ent.size();
	}

	static class Holder {

		private TextView username, friend_count, review_Count, photo_count,
				review_txt, property_name, date_time;
		private RatingBar rating;
		private VideoView profile_video;
		private ImageView user_img, profile_photo_view;
		private Button play_btn;
		private RelativeLayout property_name_lay, profile_photo_view_lay;
		private FrameLayout video_view_lay;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		holder = new Holder();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(layout, parent, false);

		holder.username = (TextView) view.findViewById(R.id.user_name);
		holder.friend_count = (TextView) view.findViewById(R.id.friends_count);
		holder.review_Count = (TextView) view.findViewById(R.id.reviews_count);
		holder.photo_count = (TextView) view.findViewById(R.id.photos_count);
		holder.review_txt = (TextView) view.findViewById(R.id.user_review);
		holder.property_name = (TextView) view.findViewById(R.id.property_name);
		holder.date_time = (TextView) view.findViewById(R.id.time_status);
		holder.rating = (RatingBar) view
				.findViewById(R.id.user_review_ratingbar);
		holder.property_name_lay = (RelativeLayout) view
				.findViewById(R.id.property_name_lay);
		holder.property_name_lay.setTag(position);

		holder.profile_photo_view = (ImageView) view
				.findViewById(R.id.profile_photo_view);
		holder.profile_photo_view.setTag(position);

		holder.date_time.setText("Last Updated "
				+ GlobalMethods.timeDiff(act_ent.get(position).getDatetime()));

		holder.user_img = (ImageView) view.findViewById(R.id.profile_img);
		holder.profile_video = (VideoView) view
				.findViewById(R.id.profile_video_view);

		holder.play_btn = (Button) view.findViewById(R.id.video_play_btn);
		holder.video_view_lay = (FrameLayout) view
				.findViewById(R.id.video_view_lay);
		holder.video_view_lay.setTag(position);
		holder.profile_photo_view_lay = (RelativeLayout) view
				.findViewById(R.id.profile_photo_view_lay);

		holder.username.setText(act_ent.get(position).getUser_name());
		holder.friend_count.setText(act_ent.get(position).getFriends_count());
		holder.photo_count.setText(act_ent.get(position).getPhotos_Count());
		holder.review_Count.setText(act_ent.get(position).getReviews_count());
		aq1 = new AQuery(context).recycle(view);
		aq2 = new AQuery(context).recycle(view);

		aq1.id(R.id.profile_img)
				.progress(R.id.progre)
				.image(act_ent.get(position).getUser_profile_image(), true,
						true, 0, R.drawable.profile_pic, null, 0, 1.0f);

		if (act_ent.get(position).getReview_user_id() != null) {
			holder.property_name.setText("Wrote a Review for "
					+ act_ent.get(position).getAddress());
			holder.rating.setRating(Float.valueOf(act_ent.get(position)
					.getRating()));
			holder.review_txt.setText(act_ent.get(position).getComments());

			holder.rating.setVisibility(View.VISIBLE);
			holder.review_txt.setVisibility(View.VISIBLE);
			holder.profile_photo_view_lay.setVisibility(View.GONE);
			holder.video_view_lay.setVisibility(View.GONE);
			holder.play_btn.setVisibility(View.GONE);

		} else {
			if (act_ent.get(position).getFile_type().equals("image")) {
				holder.property_name.setText("Added a Photo for "
						+ act_ent.get(position).getAddress());
				holder.rating.setVisibility(View.GONE);
				holder.review_txt.setVisibility(View.GONE);
				holder.profile_photo_view_lay.setVisibility(View.VISIBLE);
				holder.video_view_lay.setVisibility(View.GONE);
				holder.play_btn.setVisibility(View.GONE);
				aq2.id(R.id.profile_photo_view)
						.progress(R.id.profile_img_progress)
						.image(act_ent.get(position).getFile(), true, true, 0,
								0, null, 0, 1.0f / 1.0f);

			} else if (act_ent.get(position).getFile_type().equals("video")) {
				holder.property_name.setText("Added a Video for "
						+ act_ent.get(position).getAddress());
				holder.rating.setVisibility(View.GONE);
				holder.review_txt.setVisibility(View.GONE);
				holder.profile_photo_view_lay.setVisibility(View.GONE);
				holder.video_view_lay.setVisibility(View.VISIBLE);
				holder.play_btn.setVisibility(View.VISIBLE);

			}

			holder.property_name_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					Intent intent = new Intent(context,
							PropertyDetailsActivity.class);
					intent.putExtra("PropertyId", act_ent.get(pos)
							.getProperty_id());
					intent.putExtra("PropType", "");
					intent.putExtra("CallFrom", "profile");
					context.startActivity(intent);
				}
			});

			holder.profile_photo_view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					ProfilePhotoVideoScreen.showImageVideoDialogs(
							act_ent.get(pos).getFile_type(), act_ent.get(pos)
									.getFile(), act_ent.get(pos)
									.getDescription(), act_ent.get(pos)
									.getDatetime());
				}
			});

			holder.video_view_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					ProfilePhotoVideoScreen.showImageVideoDialogs(
							act_ent.get(pos).getFile_type(), act_ent.get(pos)
									.getFile(), act_ent.get(pos)
									.getDescription(), act_ent.get(pos)
									.getDatetime());
				}
			});
		}

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
