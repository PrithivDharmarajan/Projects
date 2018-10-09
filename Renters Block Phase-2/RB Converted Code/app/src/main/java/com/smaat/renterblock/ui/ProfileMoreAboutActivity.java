package com.smaat.renterblock.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.entity.MoreAboutMeModelEntity;
import com.smaat.renterblock.entity.UserPropertyPics;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.ExtendedViewPager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.TouchImageView;

public class ProfileMoreAboutActivity extends BaseActivity implements
		DialogMangerCallback {

	private int width, newWidth;
	private ImageView mUserpic2, mUserpic3, mUserpic4, mUserpic5;
	private ImageView mUserpic1;
	private Button mUservideo1, mUservideo2, mUservideo3, mUservideo4,
			mUservideo5;
	private RelativeLayout mUserpiclay1, mUserpiclay2, mUserpiclay3,
			mUserpiclay4, mUserpiclay5;
	private RelativeLayout user_video_view1, user_video_view2,
			user_video_view3, user_video_view4, user_video_view5;
	private ProgressBar mProgress1, mProgress2, mProgress3, mProgress4,
			mProgress5;
	private ScrollView mScroll;
	private ImageView edit_view, slide_menu;
	private TextView header_txt, user_name_txt, friends_count, reviews_count,
			photos_count;
	private EditText about_me_txt;
	private int mImgID, mVideoID;
	private Bitmap mBitmapFromDevice;
	byte[] mPropImg1;
	File file;
	byte[] bFile;
	FileInputStream fileInputStream = null;
	Uri selectedVideo;
	private boolean img1 = false, img2 = false, img3 = false, img4 = false,
			img5 = false;
	private boolean vid1 = false, vid2 = false, vid3 = false, vid4 = false,
			vid5 = false;
	private String file_order;
	private MoreAboutMeModelEntity response;

	private Dialog d2;
	private TouchImageView profile_pic;
	private ProgressBar image_prog;
	private Button play_btn;
	private Button pause_btn;
	boolean istouched = false;
	int stopPosition;
	private RelativeLayout close_lay;

	String img_url1 = "", img_url2 = "", img_url3 = "", img_url4 = "",
			img_url5 = "";
	String vid_url1 = "", vid_url2 = "", vid_url3 = "", vid_url4 = "",
			vid_url5 = "";

	private Button video_play_btn1, video_play_btn2, video_play_btn3,
			video_play_btn4, video_play_btn5;

	private String user_pic, friend_count, review_count, photo_count, username,
			rating_count;
	private RatingBar rating;
	private RelativeLayout close_layout;
	private String show_add = "";
	private TextView agent_with_txt;

	// hide layouts
	TextView photo_textview, video_textview;
	View hide_view1, hide_view2;
	LinearLayout photo_lay, video_lay;
	Button update_btn;

	private FrameLayout photo_frame_layout;

	private String agent_id, mBusiness_name;

	private UserPropertyPics user_prop_pic_ent;
	private ArrayList<UserPropertyPics> user_prop_pic_array = new ArrayList<UserPropertyPics>();

	TouchImageView profile_pics;
	ExtendedViewPager mViewPager;
	private VideoDetailOnPageChangeListener list_ner;

	FrameLayout frame;
	private LinearLayout mLicense_txt_lay;
	private String selectedType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_more_about_me);

		Bundle extras = getIntent().getExtras();

		photo_textview = (TextView) findViewById(R.id.photo_textview);
		video_textview = (TextView) findViewById(R.id.video_textview);
		hide_view1 = (View) findViewById(R.id.hide_view_1);
		hide_view2 = (View) findViewById(R.id.hide_view2);
		photo_lay = (LinearLayout) findViewById(R.id.photo_lay);
		video_lay = (LinearLayout) findViewById(R.id.video_lay);
		update_btn = (Button) findViewById(R.id.update_btn);
		if (extras != null) {
			friend_count = extras.getString("friend_count");
			review_count = extras.getString("review_count");
			photo_count = extras.getString("photo_count");
			username = extras.getString("username");
			user_pic = extras.getString("profile_pic");
			show_add = extras.getString("show_add");
			agent_id = extras.getString("agent_id");
			mBusiness_name = extras.getString("business_name");
		}

		getDeviceHeight();
		agent_with_txt = (TextView) findViewById(R.id.agent_with_txt);
		agent_with_txt.setText(mUser_Type + " with " + mBusiness_name);
		initComponents();
		longPressListener();
		mScroll.setScrollY(0);
		mScroll.setScrollX(0);
		getPhotoandVideos();

		if (show_add != null && show_add.equals("1")) {
			photo_textview.setVisibility(View.GONE);
			video_textview.setVisibility(View.GONE);
			hide_view1.setVisibility(View.GONE);
			hide_view2.setVisibility(View.GONE);
			photo_lay.setVisibility(View.GONE);
			video_lay.setVisibility(View.GONE);
			about_me_txt.setFocusable(false);
			about_me_txt.setFocusableInTouchMode(false);
			update_btn.setVisibility(View.GONE);

		}
	}

	private void longPressListener() {
		final String img_msg = "Do you want to delete the photo?";
		final String vid_msg = "Do you want to delete the video?";
		mUserpic1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (img1) {
					String val = "img1";
					showDeleteAlert(img_msg, val, "1", "image");
				}
				return false;
			}
		});

		mUserpic2.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (img2) {
					String val = "img2";
					showDeleteAlert(img_msg, val, "2", "image");
				}
				return false;
			}
		});

		mUserpic3.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (img3) {
					String val = "img3";
					showDeleteAlert(img_msg, val, "3", "image");
				}
				return false;
			}
		});

		mUserpic4.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (img4) {
					String val = "img4";
					showDeleteAlert(img_msg, val, "4", "image");
				}
				return false;
			}
		});

		mUserpic5.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (img5) {
					String val = "img5";
					showDeleteAlert(img_msg, val, "5", "image");
				}
				return false;
			}
		});

		video_play_btn1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (vid1) {
					String val = "vid1";
					showDeleteAlert(vid_msg, val, "1", "video");
				}
				return false;
			}
		});

		video_play_btn2.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (vid2) {
					String val = "vid2";
					showDeleteAlert(vid_msg, val, "2", "video");
				}
				return false;
			}
		});

		video_play_btn3.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (vid3) {
					String val = "vid3";
					showDeleteAlert(vid_msg, val, "3", "video");
				}
				return false;
			}
		});

		video_play_btn4.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (vid4) {
					String val = "vid4";
					showDeleteAlert(vid_msg, val, "4", "video");
				}
				return false;
			}
		});

		video_play_btn5.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				if (vid5) {
					String val = "vid5";
					showDeleteAlert(vid_msg, val, "5", "video");
				}
				return false;
			}
		});

	}

	private void showDeleteAlert(String msg, final String img_vid,
			final String file_order, final String file_type) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProfileMoreAboutActivity.this);

		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								if (img_vid.equals("img1")) {
									img1 = false;
									mUserpic1
											.setImageResource(R.drawable.about_photo_icon);
								} else if (img_vid.equals("img2")) {
									img2 = false;
									mUserpic2
											.setImageResource(R.drawable.about_photo_icon);
								} else if (img_vid.equals("img3")) {
									img3 = false;
									mUserpic3
											.setImageResource(R.drawable.about_photo_icon);
								} else if (img_vid.equals("img4")) {
									img4 = false;
									mUserpic4
											.setImageResource(R.drawable.about_photo_icon);
								} else if (img_vid.equals("img5")) {
									img5 = false;
									mUserpic5
											.setImageResource(R.drawable.about_photo_icon);
								} else if (img_vid.equals("vid1")) {
									vid1 = false;
								} else if (img_vid.equals("vid2")) {
									vid2 = false;
								} else if (img_vid.equals("vid3")) {
									vid3 = false;
								} else if (img_vid.equals("vid4")) {
									vid4 = false;
								} else if (img_vid.equals("vid5")) {
									vid5 = false;
								}
								for (int i = 0; i < response.getResult().size(); i++) {
									if (response.getResult().get(i)
											.getFile_type().equals(file_type)) {
										if (response.getResult().get(i)
												.getFile_order()
												.equals(file_order)) {
											String photo_id = response
													.getResult().get(i)
													.getPhoto_video_id();
											calldeleteService(photo_id);
											break;
										}
									}
								}
								dialog.cancel();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void calldeleteService(String photo_id) {
		String url = AppConstants.BASE_URL + "updateme/delete";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("photo_video_id", photo_id);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									DialogManager.showCustomAlertDialog(
											ProfileMoreAboutActivity.this,
											ProfileMoreAboutActivity.this,
											"Deleted Successfully.");
								} else {
									statusErrorCode(status);
								}

							}
						});

	}

	private void getPhotoandVideos() {
		String url = AppConstants.BASE_URL + "updateme/view";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		if (show_add != null && show_add.equals("1")) {
			params.put("user_id", agent_id);
		} else {
			params.put("user_id", UserID);
		}

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									response = new Gson().fromJson(
											json.toString(),
											MoreAboutMeModelEntity.class);
									onRequestSucc(response);
								} else {
									statusErrorCode(status);
								}

							}
						});

	}

	private void onRequestSucc(MoreAboutMeModelEntity response) {

		if (response.getError_code().equals("1")) {

			about_me_txt.setText(response.getAbout());
			user_prop_pic_array.clear();
			for (int i = 0; i < response.getResult().size(); i++) {

				user_prop_pic_ent = new UserPropertyPics();
				user_prop_pic_ent.setDatetime(response.getResult().get(i)
						.getDatetime());
				user_prop_pic_ent
						.setFile(response.getResult().get(i).getFile());
				user_prop_pic_ent.setDescription(response.getResult().get(i)
						.getPhoto_description());
				user_prop_pic_ent.setFile_type(response.getResult().get(i)
						.getFile_type());
				user_prop_pic_ent.setFriends(friend_count);
				user_prop_pic_ent.setPhoto_video_id(response.getResult().get(i)
						.getPhoto_video_id());
				user_prop_pic_ent.setPhotos(photo_count);
				user_prop_pic_ent.setReview(review_count);
				user_prop_pic_ent.setUser_name(username);
				user_prop_pic_ent.setUser_pic(user_pic);

				user_prop_pic_array.add(user_prop_pic_ent);

				if (response.getResult().get(i).getFile_order().equals("1")) {
					if (response.getResult().get(i).getFile_type()
							.equals("image")) {
						// mProgress1.setVisibility(View.VISIBLE);
						img1 = true;
						aq().id(R.id.user_image1)
								.progress(R.id.img_prog_1)
								.image(response.getResult().get(i).getFile(),
										true, true, R.drawable.profile_pic, 0,
										null, 0, 1.0f / 1.0f);
						img_url1 = response.getResult().get(i).getFile();
					} else {
						vid1 = true;
						mUservideo1.setVisibility(View.GONE);
						user_video_view1.setBackgroundColor(Color
								.parseColor("#000000"));
						video_play_btn1.setVisibility(View.VISIBLE);
						String link = response.getResult().get(i).getFile();
						vid_url1 = link;
					}
				} else if (response.getResult().get(i).getFile_order()
						.equals("2")) {
					if (response.getResult().get(i).getFile_type()
							.equals("image")) {
						img2 = true;
						aq().id(R.id.user_image2)
								.progress(R.id.img_prog_2)
								.image(response.getResult().get(i).getFile(),
										true, true, 0, 0, null, 0, 1.0f / 1.0f);
						img_url2 = response.getResult().get(i).getFile();
					} else {
						vid2 = true;
						mUservideo2.setVisibility(View.GONE);
						user_video_view2.setBackgroundColor(Color
								.parseColor("#000000"));
						video_play_btn2.setVisibility(View.VISIBLE);
						String link = response.getResult().get(i).getFile();
						vid_url2 = link;
					}
				} else if (response.getResult().get(i).getFile_order()
						.equals("3")) {

					if (response.getResult().get(i).getFile_type()
							.equals("image")) {
						img3 = true;
						aq().id(R.id.user_image3)
								.progress(R.id.img_prog_3)
								.image(response.getResult().get(i).getFile(),
										true, true, 0, 0, null, 0, 1.0f / 1.0f);
						img_url3 = response.getResult().get(i).getFile();
					} else {
						vid3 = true;
						mUservideo3.setVisibility(View.GONE);
						user_video_view3.setBackgroundColor(Color
								.parseColor("#000000"));
						video_play_btn3.setVisibility(View.VISIBLE);
						String link = response.getResult().get(i).getFile();
						vid_url3 = link;
					}
				} else if (response.getResult().get(i).getFile_order()
						.equals("4")) {
					if (response.getResult().get(i).getFile_type()
							.equals("image")) {
						img4 = true;
						aq().id(R.id.user_image4)
								.progress(R.id.img_prog_4)
								.image(response.getResult().get(i).getFile(),
										true, true, 0, 0, null, 0, 1.0f / 1.0f);
						img_url4 = response.getResult().get(i).getFile();
					} else {
						vid4 = true;
						mUservideo4.setVisibility(View.GONE);
						user_video_view4.setBackgroundColor(Color
								.parseColor("#000000"));
						video_play_btn4.setVisibility(View.VISIBLE);
						String link = response.getResult().get(i).getFile();
						vid_url4 = link;
					}
				} else if (response.getResult().get(i).getFile_order()
						.equals("5")) {
					if (response.getResult().get(i).getFile_type()
							.equals("image")) {
						img5 = true;
						aq().id(R.id.user_image5)
								.progress(R.id.img_prog_5)
								.image(response.getResult().get(i).getFile(),
										true, true, 0, 0, null, 0, 1.0f / 1.0f);
						img_url5 = response.getResult().get(i).getFile();
					} else {
						vid5 = true;
						mUservideo5.setVisibility(View.GONE);
						user_video_view5.setBackgroundColor(Color
								.parseColor("#000000"));
						video_play_btn5.setVisibility(View.VISIBLE);
						String link = response.getResult().get(i).getFile();
						vid_url5 = link;
					}
				}
			}
		}
	}

	public static Drawable LoadImageFromWeb(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	private void initComponents() {
		mScroll = (ScrollView) findViewById(R.id.parent_scroll);
		mUserpic1 = (ImageView) findViewById(R.id.user_image1);
		mUserpic2 = (ImageView) findViewById(R.id.user_image2);
		mUserpic3 = (ImageView) findViewById(R.id.user_image3);
		mUserpic4 = (ImageView) findViewById(R.id.user_image4);
		mUserpic5 = (ImageView) findViewById(R.id.user_image5);

		mProgress1 = (ProgressBar) findViewById(R.id.img_prog_1);
		mProgress2 = (ProgressBar) findViewById(R.id.img_prog_2);
		mProgress3 = (ProgressBar) findViewById(R.id.img_prog_3);
		mProgress4 = (ProgressBar) findViewById(R.id.img_prog_4);
		mProgress5 = (ProgressBar) findViewById(R.id.img_prog_5);

		mUserpiclay1 = (RelativeLayout) findViewById(R.id.user_image1_lay);
		mUserpiclay2 = (RelativeLayout) findViewById(R.id.user_image2_lay);
		mUserpiclay3 = (RelativeLayout) findViewById(R.id.user_image3_lay);
		mUserpiclay4 = (RelativeLayout) findViewById(R.id.user_image4_lay);
		mUserpiclay5 = (RelativeLayout) findViewById(R.id.user_image5_lay);

		video_play_btn1 = (Button) findViewById(R.id.play_vid_1);
		video_play_btn2 = (Button) findViewById(R.id.play_vid_2);
		video_play_btn3 = (Button) findViewById(R.id.play_vid_3);
		video_play_btn4 = (Button) findViewById(R.id.play_vid_4);
		video_play_btn5 = (Button) findViewById(R.id.play_vid_5);

		mUservideo1 = (Button) findViewById(R.id.user_video1);
		mUservideo2 = (Button) findViewById(R.id.user_video2);
		mUservideo3 = (Button) findViewById(R.id.user_video3);
		mUservideo4 = (Button) findViewById(R.id.user_video4);
		mUservideo5 = (Button) findViewById(R.id.user_video5);

		user_video_view1 = (RelativeLayout) findViewById(R.id.video_view_relay_1);
		user_video_view2 = (RelativeLayout) findViewById(R.id.video_view_relay_2);
		user_video_view3 = (RelativeLayout) findViewById(R.id.video_view_relay_3);
		user_video_view4 = (RelativeLayout) findViewById(R.id.video_view_relay_4);
		user_video_view5 = (RelativeLayout) findViewById(R.id.video_view_relay_5);

		LayoutParams params = new LayoutParams(newWidth, newWidth);
		int margin = getResources().getDimensionPixelSize(R.dimen.margin5);
		params.setMargins(0, 0, margin, 0);

		mUserpiclay1.setLayoutParams(params);
		mUserpiclay2.setLayoutParams(params);
		mUserpiclay3.setLayoutParams(params);
		mUserpiclay4.setLayoutParams(params);
		mUserpiclay5.setLayoutParams(params);

		user_video_view1.setLayoutParams(params);
		user_video_view2.setLayoutParams(params);
		user_video_view3.setLayoutParams(params);
		user_video_view4.setLayoutParams(params);
		user_video_view5.setLayoutParams(params);

		// mUservideo1.setLayoutParams(params);
		// mUservideo2.setLayoutParams(params);
		// mUservideo3.setLayoutParams(params);
		// mUservideo4.setLayoutParams(params);
		// mUservideo5.setLayoutParams(params);

		photo_frame_layout = (FrameLayout) findViewById(R.id.photo_frame_layout);

		if (show_add != null && show_add.equals("1")) {
			photo_frame_layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub\
					showAgentPhotosandVideos();
				}
			});
		}

		edit_view = (ImageView) findViewById(R.id.filter);
		slide_menu = (ImageView) findViewById(R.id.slide);
		slide_menu.setImageResource(R.drawable.back_arrow_white);
		header_txt = (TextView) findViewById(R.id.how);
		header_txt.setText(getString(R.string.about));
		user_name_txt = (TextView) findViewById(R.id.user_name_txt);
		edit_view.setVisibility(View.INVISIBLE);
		about_me_txt = (EditText) findViewById(R.id.about_me_txt);

		aq().id(R.id.user_image).progress(R.id.progress)
				.image(user_pic, true, true, 0, 0, null, 0, 1.0f / 1.0f);
		user_name_txt.setText(username);
		friends_count = (TextView) findViewById(R.id.friends_count);
		reviews_count = (TextView) findViewById(R.id.reviews_count);
		photos_count = (TextView) findViewById(R.id.photos_count);
		rating = (RatingBar) findViewById(R.id.user_ratingbar);
		mLicense_txt_lay = (LinearLayout) findViewById(R.id.license_txt_lay);
		

		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);

		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mLicense_txt_lay.setVisibility(View.INVISIBLE);
			rating.setVisibility(View.INVISIBLE);
			agent_with_txt.setVisibility(View.INVISIBLE);
		} else {
			mLicense_txt_lay.setVisibility(View.VISIBLE);
			rating.setVisibility(View.VISIBLE);
			agent_with_txt.setVisibility(View.VISIBLE);
		}

		friends_count.setText(friend_count);
		reviews_count.setText(review_count);
		photos_count.setText(photo_count);
		if (rating_count != null) {
			rating.setRating(Float.valueOf(rating_count));
		}

		close_layout = (RelativeLayout) findViewById(R.id.close_layout);
		close_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mScroll.fullScroll(View.FOCUS_UP);
			}
		}, 1000);
		;

	}

	private void showAgentPhotosandVideos() {
		d2 = new Dialog(ProfileMoreAboutActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d2.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d2.setContentView(R.layout.photo_video_full_view);
		d2.setCancelable(true);

		mViewPager = (ExtendedViewPager) d2.findViewById(R.id.view_pager);
		mViewPager.setAdapter(new VideoImagePagerAdapter(user_prop_pic_array));
		list_ner = new VideoDetailOnPageChangeListener();
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(list_ner);

		d2.show();
	}

	class VideoImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		private ArrayList<UserPropertyPics> prop_pic;

		VideoImagePagerAdapter(ArrayList<UserPropertyPics> user_img) {
			inflater = getLayoutInflater();
			this.prop_pic = user_img;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ExtendedViewPager) container).removeView((View) object);
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return prop_pic.size();
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(
					R.layout.profile_photo_video_full_view, null);

			final VideoView profile_video;
			RelativeLayout adap_vidl1;
			final ProgressBar video_prog;

			// user Profile details
			ImageView profile_img = (ImageView) imageLayout
					.findViewById(R.id.profile_img);
			TextView user_name = (TextView) imageLayout
					.findViewById(R.id.user_name);
			TextView friends_count = (TextView) imageLayout
					.findViewById(R.id.friends_count);
			TextView reviews_count = (TextView) imageLayout
					.findViewById(R.id.reviews_count);
			TextView photos_count = (TextView) imageLayout
					.findViewById(R.id.photos_count);

			aq().id(profile_img).image(prop_pic.get(position).getUser_pic(),
					true, true, 0, 0, null, 0, 1.0f / 1.0f);

			user_name.setText(prop_pic.get(position).getUser_name());
			friends_count.setText(prop_pic.get(position).getFriends());
			reviews_count.setText(prop_pic.get(position).getReview());
			photos_count.setText(prop_pic.get(position).getPhotos());

			TextView time_txt = (TextView) imageLayout
					.findViewById(R.id.date_time);
			time_txt.setText("Latest updated "
					+ GlobalMethods.timeDiff(prop_pic.get(position)
							.getDatetime()));

			profile_pics = (TouchImageView) imageLayout
					.findViewById(R.id.adap_img1);
			profile_video = (VideoView) imageLayout
					.findViewById(R.id.adap_video1);
			adap_vidl1 = (RelativeLayout) imageLayout
					.findViewById(R.id.adap_vidl1);
			image_prog = (ProgressBar) imageLayout
					.findViewById(R.id.adap_progress1);
			video_prog = (ProgressBar) imageLayout
					.findViewById(R.id.adap_vid_progress1);
			play_btn = (Button) imageLayout
					.findViewById(R.id.adap_video_play_btn1);
			frame = (FrameLayout) imageLayout.findViewById(R.id.main_frame);
			close_lay = (RelativeLayout) imageLayout
					.findViewById(R.id.close_dia);
			Button close_btn = (Button) imageLayout
					.findViewById(R.id.close_dialog);
			RelativeLayout main_lay = (RelativeLayout) imageLayout
					.findViewById(R.id.parent_layout);
			pause_btn = (Button) imageLayout
					.findViewById(R.id.adap_video_pause_btn1);
			TextView desc_text = (TextView) imageLayout
					.findViewById(R.id.desc_text);

			desc_text.setText(prop_pic.get(position).getDescription());

			close_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					d2.dismiss();
				}
			});

			close_lay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					d2.dismiss();
				}
			});

			if (prop_pic.get(position).getFile_type().equals("image")) {
				// image_prog.setVisibility(View.VISIBLE);
				profile_pics.setVisibility(View.VISIBLE);
				String url = prop_pic.get(position).getFile();
				aq().id(profile_pics).progress(image_prog).image(url);
			} else {
				adap_vidl1.setVisibility(View.VISIBLE);
				profile_pics.setVisibility(View.GONE);
				image_prog.setVisibility(View.GONE);
				main_lay.setBackgroundColor(Color.parseColor("#000000"));
				String file = prop_pic.get(position).getFile();
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
						Toast.makeText(ProfileMoreAboutActivity.this,
								"Sorry! Video Cannot be Played.",
								Toast.LENGTH_SHORT).show();
						// d2.dismiss();
						return true;
					}
				});

				profile_video.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {

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

				profile_video
						.setOnCompletionListener(new OnCompletionListener() {

							@Override
							public void onCompletion(MediaPlayer mp) {
								play_btn.setVisibility(View.VISIBLE);
							}
						});
			}
			((ExtendedViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}

	}

	class VideoDetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			super.onPageScrollStateChanged(state);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			mViewPager.getParent().requestDisallowInterceptTouchEvent(true);

		}

	}

	class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

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
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent inte = new Intent(ProfileMoreAboutActivity.this,
				ProfileScreen.class);
		startActivity(inte);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide:
			// launchActivity(ProfileScreen.class);
			if (show_add != null && show_add.equals("1")) {
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				Intent inte = new Intent(ProfileMoreAboutActivity.this,
						ProfileScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;
		case R.id.user_image1:
			if (img1) {
				showImageVideoFullView("image", img_url1, 0);
			} else {
				ProfileScreen.file_order = "1";
				mImgID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "pic");
			}
			break;
		case R.id.user_image2:
			if (img2) {
				showImageVideoFullView("image", img_url2, 1);
			} else {
				mImgID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "pic");
				ProfileScreen.file_order = "2";
			}
			break;
		case R.id.user_image3:
			if (img3) {
				showImageVideoFullView("image", img_url3, 2);
			} else {
				mImgID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "pic");
				ProfileScreen.file_order = "3";
			}
			break;
		case R.id.user_image4:
			if (img4) {
				showImageVideoFullView("image", img_url4, 3);
			} else {
				mImgID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "pic");
				ProfileScreen.file_order = "4";
			}
			break;
		case R.id.user_image5:
			if (img5) {
				showImageVideoFullView("image", img_url5, 4);
			} else {
				mImgID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "pic");
				ProfileScreen.file_order = "5";
			}
			break;
		case R.id.user_video1:
			if (vid1) {
				showImageVideoFullView("video", vid_url1, 0);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "1";
			}
			break;
		case R.id.user_video2:
			if (vid2) {
				showImageVideoFullView("video", vid_url2, 1);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "2";
			}
			break;
		case R.id.user_video3:
			if (vid3) {
				showImageVideoFullView("video", vid_url3, 2);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "3";
			}
			break;
		case R.id.user_video4:
			if (vid4) {
				showImageVideoFullView("video", vid_url4, 3);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "4";
			}
			break;
		case R.id.user_video5:
			if (vid5) {
				showImageVideoFullView("video", vid_url5, 4);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "5";
			}
			break;
		case R.id.update_btn:
			if (show_add != null && show_add.equals("1")) {

			} else {
				callUpdatePhotoVideoService();
			}
			break;
		case R.id.play_vid_1:
			if (vid1) {
				showImageVideoFullView("video", vid_url1, 0);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "1";
			}
			break;
		case R.id.play_vid_2:
			if (vid2) {
				showImageVideoFullView("video", vid_url2, 1);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "2";
			}
			break;
		case R.id.play_vid_3:
			if (vid3) {
				showImageVideoFullView("video", vid_url3, 2);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "3";
			}
			break;
		case R.id.play_vid_4:
			if (vid4) {
				showImageVideoFullView("video", vid_url4, 3);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "4";
			}
			break;
		case R.id.play_vid_5:
			if (vid5) {
				showImageVideoFullView("video", vid_url5, 4);
			} else {
				mVideoID = v.getId();
				ProfileImageSelectionUtil.showOptionNew(this, "video");
				ProfileScreen.file_order = "5";
			}
			break;
		default:
			break;
		}
	}

	private void showImageVideoFullView(String file_type, String file, int pos) {
		final VideoView profile_video;
		RelativeLayout adap_vidl1;
		final ProgressBar video_prog;
		FrameLayout frame;
		MediaController controller;
		d2 = new Dialog(ProfileMoreAboutActivity.this,
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
		TextView description = (TextView) d2.findViewById(R.id.desc_text);
		TextView date_time = (TextView) d2.findViewById(R.id.date_time);

		for (int i = 0; i < response.getResult().size(); i++) {
			if (response.getResult().get(i).getFile().equals(file)) {
				date_time.setText("Last Updated "
						+ GlobalMethods.timeDiff(response.getResult().get(i)
								.getDatetime()));

				String desc = response.getResult().get(i)
						.getPhoto_description().toString();
				description.setText(desc);
			}
		}

		ImageView profile_img = (ImageView) d2.findViewById(R.id.profile_img);

		aq().id(profile_img).image(user_pic, true, true, 0, 0, null, 0,
				1.0f / 1.0f);

		TextView user_name = (TextView) d2.findViewById(R.id.user_name);
		TextView friends_count = (TextView) d2.findViewById(R.id.friends_count);
		TextView reviews_count = (TextView) d2.findViewById(R.id.reviews_count);
		TextView photos_count = (TextView) d2.findViewById(R.id.photos_count);

		// if (response.getResult().get(pos).getFile_type().equals("image")) {
		// aq().id(R.id.profile_img).image(user_pic);
		// }
		close_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d2.dismiss();
			}
		});
		close_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				d2.dismiss();
			}
		});

		user_name.setText(username);
		friends_count.setText(friend_count);
		reviews_count.setText(review_count);
		photos_count.setText(photo_count);

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
					profile_video.setBackgroundDrawable(null);
					profile_video.start();
					video_prog.setVisibility(View.GONE);
				}
			});

			profile_video.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					video_prog.setVisibility(View.GONE);
					Toast.makeText(ProfileMoreAboutActivity.this,
							"Sorry! Video Cannot be Played.",
							Toast.LENGTH_SHORT).show();
					d2.dismiss();
					return true;
				}
			});

			profile_video.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {

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

	// class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {
	//
	// @Override
	// protected Bitmap doInBackground(String... params) {
	//
	// try {
	// URL urlConnection = new URL(params[0]);
	// HttpURLConnection connection = (HttpURLConnection) urlConnection
	// .openConnection();
	// connection.setDoInput(true);
	// connection.connect();
	// InputStream input = connection.getInputStream();
	// Bitmap myBitmap = BitmapFactory.decodeStream(input);
	// return myBitmap;
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Bitmap result) {
	// super.onPostExecute(result);
	// image_prog.setVisibility(View.GONE);
	// profile_pic.setImageBitmap(result);
	// close_lay.setFocusable(true);
	// close_lay.setFocusableInTouchMode(true);
	// close_lay.bringToFront();
	// }
	//
	// }

	private void callUpdatePhotoVideoService() {

		String url = AppConstants.BASE_URL + "updateme";
		GsonTransformer t = new GsonTransformer();
		// file_order = (String) GlobalMethods.getValueFromPreference(this,
		// GlobalMethods.STRING_PREFERENCE, AppConstants.file_order);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("file_type", "");
		params.put("about", about_me_txt.getText().toString());
		params.put("file_order", "");
		params.put("about_image", "");
		params.put("photo_description", "");

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									DialogManager.showCustomAlertDialog(
											ProfileMoreAboutActivity.this,
											ProfileMoreAboutActivity.this,
											"Updated Successfully.");
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			if (resultCode == RESULT_OK) {

				if (requestCode == ProfileImageSelectionUtil.CAMERA
						|| requestCode == ProfileImageSelectionUtil.GALLERY) {

					Bitmap image = ProfileImageSelectionUtil.getImage(data,
							this);

					if (image != null) {
						if (requestCode == ProfileImageSelectionUtil.CAMERA) {
							if (ProfileImageSelectionUtil.isUriTrue) {
								image = ProfileImageSelectionUtil
										.getCorrectOrientationImage(this,
												data.getData(), image);
							} else {
								image = ProfileImageSelectionUtil
										.getCorrectOrientationImage(this, image);
							}
						} else {

							Uri selectedImage = data.getData();

							image = ProfileImageSelectionUtil
									.getCorrectOrientationImage(this,
											selectedImage, image);
						}
						mBitmapFromDevice = image;
						showDescriptionAlert("image", image);
						// updateProfileImage("image");
						setImage(mImgID, mBitmapFromDevice);

					} else {
						DialogManager.showCustomAlertDialog(this, this,
								"Unsupported file format");
					}

				} else if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO
						|| requestCode == ProfileImageSelectionUtil.VIDEO_GALLERY) {
					Bitmap thumb = null;
					if (requestCode == ProfileImageSelectionUtil.CAMERA_VIDEO) {
						thumb = ThumbnailUtils.createVideoThumbnail(
								getFilename().getAbsolutePath(), 70);
					} else {
						selectedVideo = data.getData();
						file = new File(convertMediaUriToPath(selectedVideo));
						thumb = ThumbnailUtils.createVideoThumbnail(
								convertMediaUriToPath(selectedVideo), 70);
					}
					// setVideoPlayback(mVideoID, selectedVideo);
					showDescriptionAlert("video", thumb);
					// updateProfileImage("video");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showDescriptionAlert(final String file_type, Bitmap thumb) {
		final Dialog d3 = new Dialog(ProfileMoreAboutActivity.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.post_photo_video_view);
		d3.setCancelable(false);
		TextView post_desc = (TextView) d3.findViewById(R.id.post_desc);
		LinearLayout cancel = (LinearLayout) d3.findViewById(R.id.back_icon);
		final EditText mDescription = (EditText) d3
				.findViewById(R.id.description);
		ImageView capture_image = (ImageView) d3
				.findViewById(R.id.capture_image);

		capture_image.setImageBitmap(thumb);

		post_desc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateProfileImage(file_type, mDescription.getText().toString());
				d3.dismiss();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateProfileImage(file_type, "");
				d3.dismiss();
			}
		});

		d3.show();
	}

	protected String convertMediaUriToPath(Uri uri) {
		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		cursor.close();
		return path;
	}

	private File getFilename() {

		file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
				"cameravideo.mp4");

		System.out.println("audio " + file);
		return file;
	}

	private void updateProfileImage(final String file_type,
			final String description) {
		long length = 0;
		String url = AppConstants.BASE_URL + "updateme";
		GsonTransformer t = new GsonTransformer();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("file_type", file_type);
		params.put("about", about_me_txt.getText().toString());
		params.put("file_order", ProfileScreen.file_order);
		params.put("photo_description", description);
		if (file_type.equals("video")) {
			if (file != null && !file.equals("")) {

				try {
					bFile = new byte[(int) file.length()];
					fileInputStream = new FileInputStream(file);
					fileInputStream.read(bFile);
					fileInputStream.close();
					length = file.length();
					length = length / 1024;
					params.put("about_image", bFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
			mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100,
					oututStream);
			byte[] mainImageData = (oututStream).toByteArray();
			params.put("about_image", mainImageData);
		}
		if (file_type.equals("video") && length > 25360) {
			mDialog = DialogManager.showDialog(this,
					"Video Limit Exceeded. Max size is 25 MB.",
					getString(R.string.ok), null, 0, 0);
		} else {
			aq().transformer(t)
					.progress(DialogManager.getProgressDialog(this))
					.ajax(url, params, JSONObject.class,
							new AjaxCallback<JSONObject>() {

								@Override
								public void callback(String url,
										JSONObject json, AjaxStatus status) {

									if (json != null) {
										String message = "";
										if (file_type.equals("video")) {
											message = "Video Updated Successfully.";
										} else {
											message = "Image Updated Successfully.";
										}
										DialogManager.showCustomAlertDialog(
												ProfileMoreAboutActivity.this,
												ProfileMoreAboutActivity.this,
												message);
									} else {
										statusErrorCode(status);
									}

								}
							});
		}
	}

	private void setImage(int mImgID, Bitmap mBitmap) {

		mBitmapFromDevice = mBitmap;
		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
		// byte[] mainImageData = (oututStream).toByteArray();

		if (mImgID == R.id.user_image1) {
			// mPropImg1 = mainImageData;
			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					mBitmapFromDevice);
			mUserpic1.setBackgroundDrawable(bitmapDrawable);
		} else if (mImgID == R.id.user_image2) {
			// mPropImg1 = mainImageData;
			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					mBitmapFromDevice);
			mUserpic2.setBackgroundDrawable(bitmapDrawable);
		} else if (mImgID == R.id.user_image3) {
			// mPropImg1 = mainImageData;
			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					mBitmapFromDevice);
			mUserpic3.setBackgroundDrawable(bitmapDrawable);
		} else if (mImgID == R.id.user_image4) {
			// mPropImg1 = mainImageData;
			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					mBitmapFromDevice);
			mUserpic4.setBackgroundDrawable(bitmapDrawable);
		} else if (mImgID == R.id.user_image5) {
			// mPropImg1 = mainImageData;
			BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),
					mBitmapFromDevice);
			mUserpic5.setBackgroundDrawable(bitmapDrawable);
		}
	}

	@Override
	public void onOkclick() {

		super.onOkclick();
		getPhotoandVideos();
	}

	private void getDeviceHeight() {

		int padding = getResources().getDimensionPixelSize(R.dimen.margin10);

		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		switch (metrics.densityDpi) {

		case DisplayMetrics.DENSITY_MEDIUM:
			width = metrics.widthPixels;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			width = metrics.widthPixels;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			width = metrics.widthPixels;
			break;
		default:
			width = metrics.widthPixels;
			break;

		}
		newWidth = width / 5;
		newWidth = newWidth - padding;
	}
}
