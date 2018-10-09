package com.smaat.renterblock.ui;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.profile.Entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.profile.adapter.ProfileReviewScreenAdapter;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TouchImageView;

public class ProfilePhotoVideoScreen extends BaseActivity {

	private ListView profile_rev_list;
	private ArrayList<ProfileMyFeedsEntity> review_list = new ArrayList<ProfileMyFeedsEntity>();
	private ArrayList<ProfileMyFeedsEntity> review_list_temp = new ArrayList<ProfileMyFeedsEntity>();
	private ProfileReviewScreenAdapter my_reviews_adapter;
	ProfileMyFeedsEntity review_ent;
	private TextView header_text;

	private Dialog d2;
	private TouchImageView profile_pic;
	private ProgressBar image_prog;
	private Button play_btn;
	private Button pause_btn;
	boolean istouched = false;
	int stopPosition;
	private RelativeLayout close_lay;
	static Context mContext;
	private String show_add = "";

	String profile_pics, friend_counts, review_counts, photo_counts, username;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_review_activity);

		header_text = (TextView) findViewById(R.id.header_txt);
		header_text.setText("Photos and Videos");
		mContext = ProfilePhotoVideoScreen.this;

		// ints.putExtra("profile_image", profile_list.getResult()
		// .getUser().get(0).getUser_pic());
		// ints.putExtra("friend_count", profile_list.getResult()
		// .getFriendscount());
		// ints.putExtra("review_count", profile_list.getResult()
		// .getReviewscount());
		// ints.putExtra("photo_count", profile_list.getResult()
		// .getPhotocount());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			review_list = (ArrayList<ProfileMyFeedsEntity>) extras
					.getSerializable("review_list");
			show_add = extras.getString("show_add");
			profile_pics = extras.getString("profile_image");
			friend_counts = extras.getString("friend_count");
			review_counts = extras.getString("review_count");
			photo_counts = extras.getString("photo_count");
			username = extras.getString("username");

		}

		profile_rev_list = (ListView) findViewById(R.id.review_list);

		for (int i = 0; i < review_list.size(); i++) {
			if (review_list.get(i).getFile() != null) {
				review_ent = new ProfileMyFeedsEntity();
				review_ent.setUser_id(review_list.get(i).getUser_id());
				review_ent.setProperty_id(review_list.get(i).getProperty_id());
				review_ent.setFile(review_list.get(i).getFile());
				review_ent.setFile_type(review_list.get(i).getFile_type());
				review_ent.setPicture_id(review_list.get(i).getPicture_id());
				review_ent.setDatetime(review_list.get(i).getDatetime());
				review_ent.setProperty_name(review_list.get(i)
						.getProperty_name());
				review_ent.setFriends_count(review_list.get(i)
						.getFriends_count());
				review_ent.setReviews_count(review_list.get(i)
						.getReviews_count());
				review_ent
						.setPhotos_Count(review_list.get(i).getPhotos_Count());
				review_ent.setUser_name(review_list.get(i).getUser_name());
				review_ent.setUser_profile_image(review_list.get(i)
						.getUser_profile_image());
				review_ent.setAddress(review_list.get(i).getAddress());
				review_ent.setDescription(review_list.get(i).getDescription());
				review_list_temp.add(review_ent);
			}
		}

		my_reviews_adapter = new ProfileReviewScreenAdapter(
				ProfilePhotoVideoScreen.this,
				R.layout.my_feeds_profile_list_adapter, review_list_temp);
		profile_rev_list.setAdapter(my_reviews_adapter);
		my_reviews_adapter.notifyDataSetChanged();

	}

	public static void showImageVideoDialogs(String file_type, String file,
			String desc, String time) {
		// TODO Auto-generated method stub
		((ProfilePhotoVideoScreen) mContext).showCustomViewDialog(file_type,
				file, desc, time);

	}

	private void showCustomViewDialog(String file_type, String file,
			String desc, String time) {
		final VideoView profile_video;
		RelativeLayout adap_vidl1;
		final ProgressBar video_prog;
		FrameLayout frame;
		MediaController controller;
		d2 = new Dialog(ProfilePhotoVideoScreen.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);

		d2.setContentView(R.layout.profile_photo_video_full_view);
		d2.setCancelable(true);

		profile_pic = (TouchImageView) d2.findViewById(R.id.adap_img1);
		profile_video = (VideoView) d2.findViewById(R.id.adap_video1);
		adap_vidl1 = (RelativeLayout) d2.findViewById(R.id.adap_vidl1);
		image_prog = (ProgressBar) d2.findViewById(R.id.adap_progress1);
		video_prog = (ProgressBar) d2.findViewById(R.id.adap_vid_progress1);
		play_btn = (Button) d2.findViewById(R.id.adap_video_play_btn1);
		frame = (FrameLayout) d2.findViewById(R.id.main_frame);
		close_lay = (RelativeLayout) d2.findViewById(R.id.close_dia);
		Button close_btn = (Button) d2.findViewById(R.id.close_dialog);
		RelativeLayout main_lay = (RelativeLayout) d2
				.findViewById(R.id.parent_layout);
		pause_btn = (Button) d2.findViewById(R.id.adap_video_pause_btn1);

		// time, desc and profile details
		TextView desc_text = (TextView) d2.findViewById(R.id.desc_text);
		TextView time_txt = (TextView) d2.findViewById(R.id.date_time);
		TextView user_name = (TextView) d2.findViewById(R.id.user_name);
		TextView friends_count = (TextView) d2.findViewById(R.id.friends_count);
		TextView reviews_count = (TextView) d2.findViewById(R.id.reviews_count);
		TextView photos_count = (TextView) d2.findViewById(R.id.photos_count);
		ImageView profile_img = (ImageView) d2.findViewById(R.id.profile_img);

		aq().id(profile_img).image(profile_pics, true, true, 0, 0, null, 0,
				1.0f / 1.0f);

		user_name.setText(username);
		friends_count.setText(friend_counts);
		reviews_count.setText(review_counts);
		photos_count.setText(photo_counts);

		time_txt.setText("Latest updated " + GlobalMethods.timeDiff(time));
		desc_text.setText(desc);

		close_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d2.dismiss();
			}
		});

		close_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				d2.dismiss();
			}
		});

		if (file_type.equals("image")) {
			image_prog.setVisibility(View.VISIBLE);
			new ImageLoadTask().execute(file);
		} else {
			adap_vidl1.setVisibility(View.VISIBLE);
			profile_pic.setVisibility(View.GONE);
			image_prog.setVisibility(View.GONE);
			main_lay.setBackgroundColor(Color.parseColor("#000000"));
			String link = file;
			Uri video = Uri.parse(link);
			profile_video.setVideoURI(video);
			profile_video.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					profile_video.setBackground(null);
					profile_video.start();
					video_prog.setVisibility(View.GONE);
				}
			});

			profile_video.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					video_prog.setVisibility(View.GONE);
					Toast.makeText(ProfilePhotoVideoScreen.this,
							"Sorry! Video Cannot be Played.",
							Toast.LENGTH_SHORT).show();
					d2.dismiss();
					return true;
				}
			});

			profile_video.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (!istouched) {
						pause_btn.setVisibility(View.VISIBLE);
						istouched = true;
					} else {
						pause_btn.setVisibility(View.GONE);
						istouched = false;
					}
					return false;
				}
			});

			pause_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					stopPosition = profile_video.getCurrentPosition();
					play_btn.setVisibility(View.VISIBLE);
					pause_btn.setVisibility(View.GONE);
					profile_video.pause();
				}
			});

			play_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pause_btn.setVisibility(View.GONE);
					play_btn.setVisibility(View.GONE);
					profile_video.seekTo(stopPosition);
					profile_video.start();
				}
			});

			profile_video.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					play_btn.setVisibility(View.VISIBLE);
				}
			});
		}

		d2.show();

	}

	class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				URL urlConnection = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) urlConnection
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			image_prog.setVisibility(View.GONE);
			profile_pic.setImageBitmap(result);
			close_lay.setFocusable(true);
			close_lay.setFocusableInTouchMode(true);
			close_lay.bringToFront();
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			if (show_add != null && show_add.equals("1")) {
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				Intent inte = new Intent(ProfilePhotoVideoScreen.this,
						ProfileScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}

			break;
		default:
			break;
		}
	}
}
