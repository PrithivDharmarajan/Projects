package com.smaat.renterblock.adapter;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.renterblock.R;
import com.smaat.renterblock.myfavourite.BoardsChatActivity;
import com.smaat.renterblock.profile.Entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.PropertyDetailsActivity;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class MyFeedsAdapter {

	Context context;
	int layout;
	Holder holder = null;
	private ArrayList<ProfileMyFeedsEntity> act_ent = new ArrayList<ProfileMyFeedsEntity>();
	AQuery aq1, aq2;
	Typeface helvetica_normal, helvetica_bold, helvetica_light;

	public MyFeedsAdapter(Context context,
			ArrayList<ProfileMyFeedsEntity> my_feed_list) {

		this.context = context;
		this.act_ent = my_feed_list;

		helvetica_normal = TypefaceSingleton.getInstance()
				.getHelvetica(context);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(
				context);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				context);
	}

	static class Holder {

		private TextView username, friend_count, review_Count, photo_count,
				review_txt, property_name, time_status;
		private RatingBar rating;
		private ImageView profile_photo_view;
		private Button play_btn;
		private RelativeLayout property_name_lay, profile_photo_view_lay;
		private FrameLayout video_view_lay;
	}

	public void getView(LinearLayout parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		for (int position = 0; position < act_ent.size(); position++) {
			View view = inflater.inflate(
					R.layout.my_feeds_profile_list_adapter, parent, false);

			holder = new Holder();

			// ViewGroup root = (ViewGroup) view
			// .findViewById(R.id.parent_layout_listing_adapter);
			// Typeface mTypeface =
			// TypefaceSingleton.getInstance().getHelvetica(
			// context);
			// ((BaseActivity) context).setFont(root, mTypeface);

			holder.username = (TextView) view.findViewById(R.id.user_name);
			holder.username.setTypeface(helvetica_normal);
			holder.friend_count = (TextView) view
					.findViewById(R.id.friends_count);
			holder.friend_count.setTypeface(helvetica_normal);
			holder.review_Count = (TextView) view
					.findViewById(R.id.reviews_count);
			holder.review_Count.setTypeface(helvetica_normal);
			holder.photo_count = (TextView) view
					.findViewById(R.id.photos_count);
			holder.photo_count.setTypeface(helvetica_normal);
			holder.review_txt = (TextView) view.findViewById(R.id.user_review);
			holder.review_txt.setTypeface(helvetica_normal);
			holder.property_name = (TextView) view
					.findViewById(R.id.property_name);
			holder.property_name.setTypeface(helvetica_normal);
			holder.time_status = (TextView) view.findViewById(R.id.time_status);
			holder.time_status.setTypeface(helvetica_normal);
			holder.rating = (RatingBar) view
					.findViewById(R.id.user_review_ratingbar);
			holder.property_name_lay = (RelativeLayout) view
					.findViewById(R.id.property_name_lay);
			holder.property_name_lay.setTag(position);

			holder.profile_photo_view = (ImageView) view
					.findViewById(R.id.profile_photo_view);
			holder.profile_photo_view.setTag(position);

			holder.time_status.setText("Last Updated "
					+ GlobalMethods.timeDiff(act_ent.get(position)
							.getDatetime()));

			holder.play_btn = (Button) view.findViewById(R.id.video_play_btn);
			holder.video_view_lay = (FrameLayout) view
					.findViewById(R.id.video_view_lay);
			holder.video_view_lay.setTag(position);

			holder.profile_photo_view_lay = (RelativeLayout) view
					.findViewById(R.id.profile_photo_view_lay);

			holder.username.setText(act_ent.get(position).getUser_name());
			holder.friend_count.setText(act_ent.get(position)
					.getFriends_count());
			holder.photo_count.setText(act_ent.get(position).getPhotos_Count());
			holder.review_Count.setText(act_ent.get(position)
					.getReviews_count());
			aq1 = new AQuery(context).recycle(view);
			aq2 = new AQuery(context).recycle(view);

			aq1.id(R.id.profile_img)
					.progress(R.id.progre)
					.image(act_ent.get(position).getUser_profile_image(), true,
							true, 0, R.drawable.profile_pic, null, 0, 1.0f);

			if (act_ent.get(position).getReview_user_id() != null) {
				holder.property_name.setText("Wrote a Review for "
						+ act_ent.get(position).getAddress());
				if (act_ent.get(position).getRating() != null
						&& !act_ent.get(position).getRating()
								.equalsIgnoreCase("")) {
					holder.rating.setRating(Float.valueOf(act_ent.get(position)
							.getRating()));
				} else {
					holder.rating.setRating(0);
				}

				holder.review_txt.setText(act_ent.get(position).getComments());

				holder.rating.setVisibility(View.VISIBLE);
				holder.review_txt.setVisibility(View.VISIBLE);
				holder.profile_photo_view_lay.setVisibility(View.GONE);
				holder.video_view_lay.setVisibility(View.GONE);
				holder.play_btn.setVisibility(View.GONE);

			} else if (act_ent.get(position).getPost_id() != null) {
				holder.property_name.setText("Wrote a Comment for "
						+ act_ent.get(position).getAddress());
				holder.review_txt.setText(act_ent.get(position).getComments());

				holder.rating.setVisibility(View.GONE);
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
							.image(act_ent.get(position).getFile(), true, true,
									0, R.drawable.profile_pic, null, 0, 1.0f);

				} else if (act_ent.get(position).getFile_type().equals("video")) {
					holder.property_name.setText("Added a Video for "
							+ act_ent.get(position).getAddress());
					holder.rating.setVisibility(View.GONE);
					holder.review_txt.setVisibility(View.GONE);
					holder.profile_photo_view_lay.setVisibility(View.GONE);
					holder.video_view_lay.setVisibility(View.VISIBLE);
					holder.play_btn.setVisibility(View.VISIBLE);

				}
			}

			holder.property_name_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					if (act_ent.get(pos).getPost_id() != null) {
						Intent intent = new Intent(context,
								BoardsChatActivity.class);
						intent.putExtra("property_id", act_ent.get(pos)
								.getProperty_id());
						context.startActivity(intent);
					} else {
						Intent intent = new Intent(context,
								PropertyDetailsActivity.class);
						intent.putExtra("PropertyId", act_ent.get(pos)
								.getProperty_id());
						intent.putExtra("PropType", "");
						intent.putExtra("CallFrom", "profile");
						context.startActivity(intent);
					}
				}
			});

			holder.profile_photo_view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					ProfileScreen.showImageVideoDialog(act_ent.get(pos)
							.getFile_type(), act_ent.get(pos).getFile(),
							act_ent.get(pos).getDescription(), act_ent.get(pos)
									.getDatetime());
				}
			});

			holder.video_view_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					int pos = Integer.parseInt(String.valueOf(v.getTag()));
					ProfileScreen.showImageVideoDialog(act_ent.get(pos)
							.getFile_type(), act_ent.get(pos).getFile(),
							act_ent.get(pos).getDescription(), act_ent.get(pos)
									.getDatetime());
				}
			});

			parent.addView(view);
		}

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

}
