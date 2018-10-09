package com.smaat.renterblock.ui;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.MyFeedsAdapter;
import com.smaat.renterblock.entity.ProfileModel;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.friends.ui.FriendSearchScreen;
import com.smaat.renterblock.friends.ui.FriendsMainScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.profile.Entity.ProfileMyFeedsEntity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.ProfileImageSelectionUtil;
import com.smaat.renterblock.util.TouchImageView;

public class ProfileScreen extends BaseActivity implements DialogMangerCallback {

	private ImageView edit_view, slide_menu;
	private TextView header_txt, address_txt;
	private TableLayout feeds_list;
	private ScrollView parent_scroll;
	private int mImgID;
	private ImageView mListImg1;
	private Bitmap mBitmapFromDevice;
	byte[] mPropImg1;

	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView, enhanced_profile_lay;
	private String selectedType;
	static Context mContext;
	private TextView profile_name;
	private ArrayList<String> user_count = new ArrayList<String>();
	ProfileModel profile_list;

	private ArrayList<ProfileMyFeedsEntity> my_feed_list;
	private ProfileMyFeedsEntity my_feeds_ent;
	private Dialog d2;
	private TouchImageView profile_pic;
	private ProgressBar image_prog;
	private Button play_btn;
	private Button pause_btn;
	boolean istouched = false;
	int stopPosition;
	private RelativeLayout close_lay;
	public static String file_order;
	private String call_from, mEnhancedProfile, if_purchased;
	private LinearLayout things_to_try_lay, profile_addfriend_lay, notification_addfriend_lay;

	private RelativeLayout edit_user_name_lay;

	private Button mMoreAboutMe, chat_icon;
	private TextView mMyFeedText;
	Bundle extras;
	private View eh_view;

	static final int DATE_DIALOG_ID = 999;

	private String card_month, card_year;
	private WebView webView;
	private ProgressDialog progres;

	private TextView agent_description_txt, agent_user_name_txt, agent_phone_number;
	private Button ads_toggle_btn, mEdit_btn, mEnhanced_btn;

	private String ison;
	private RelativeLayout mAdsLay;
	private Dialog d3;
	private EditText mUsername;
	String fr_user_name;
	private LinearLayout mChat_option, mChange_password_lay;
	private RelativeLayout mChange_lay;

	private Button profile_add_friends_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_screen);

		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		mContext = ProfileScreen.this;
		initializeViews();
		setGoogleAnalytics(ProfileScreen.this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getProfileDetails();
				}
			});
		}
	}

	private void initializeViews() {
		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		chat_icon = (Button) findViewById(R.id.chat_icon);
		edit_user_name_lay = (RelativeLayout) findViewById(R.id.edit_user_name_lay);
		mEdit_btn = (Button) findViewById(R.id.edit_icon);
		mEnhanced_btn = (Button) findViewById(R.id.enhanced_btn);

		if (selectedType.equalsIgnoreCase(AppConstants.AGENT) || selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mEnhanced_btn.setText("Upgrade To Enhance Profile - $ 50 / mo");
		} else if (selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mEnhanced_btn.setText("Upgrade To Enhance Profile - $ 500 / mo");
		}

		edit_view = (ImageView) findViewById(R.id.filter);
		slide_menu = (ImageView) findViewById(R.id.slide);
		mAdsLay = (RelativeLayout) findViewById(R.id.ads_lay);

		profile_add_friends_btn = (Button) findViewById(R.id.profile_add_friends_btn);

		webView = (WebView) findViewById(R.id.webView1);

		ads_toggle_btn = (Button) findViewById(R.id.ads_toggle_btn);

		eh_view = (View) findViewById(R.id.eh_view);

		profile_name = (TextView) findViewById(R.id.profile_name);

		mChat_option = (LinearLayout) findViewById(R.id.chat_option);

		mChange_password_lay = (LinearLayout) findViewById(R.id.change_password_lay);

		my_feed_list = new ArrayList<ProfileMyFeedsEntity>();
		mEnhancedProfile = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.ENHANCED_PROFILE);

		if_purchased = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.PURCHASE_EXPIRATION);

		ison = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SHOWADS);
		if (ison.equals("true")) {
			ads_toggle_btn.setBackgroundResource(R.drawable.toggle_off);
		} else {
			ads_toggle_btn.setBackgroundResource(R.drawable.toggle_on);
		}

		enhanced_profile_lay = (LinearLayout) findViewById(R.id.enhanced_profile_lay);
		enhanced_profile_lay.setVisibility(View.GONE);

		header_txt = (TextView) findViewById(R.id.how);
		header_txt.setText(getString(R.string.profile));
		edit_view.setVisibility(View.INVISIBLE);
		feeds_list = (TableLayout) findViewById(R.id.my_feeds_list);
		parent_scroll = (ScrollView) findViewById(R.id.profile_parent_scroll);
		mListImg1 = (ImageView) findViewById(R.id.list_img11);
		address_txt = (TextView) findViewById(R.id.address_txt);
		things_to_try_lay = (LinearLayout) findViewById(R.id.things_try_lay);
		profile_addfriend_lay = (LinearLayout) findViewById(R.id.profile_addfriend_lay);
		notification_addfriend_lay = (LinearLayout) findViewById(R.id.notification_addfriend_lay);
		mMoreAboutMe = (Button) findViewById(R.id.more_about_btn);
		mMyFeedText = (TextView) findViewById(R.id.my_feed_txt);

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			eh_view.setVisibility(View.GONE);
			setBuyBackground(R.id.buy_profile);
			AppConstants.view_id = R.id.buy_profile;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_profile);
			AppConstants.view_id = R.id.sell_profile;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_profile);
			AppConstants.view_id = R.id.agent_profile;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_profile);
			AppConstants.view_id = R.id.buy_profile;
		}
		extras = getIntent().getExtras();
		if (extras != null) {
			Friend_UserID = extras.getString("user_id");
			profile_name.setText(UserName);
			call_from = extras.getString("call_from");
		} else {
			UserID = GlobalMethods.getUserID(this);
			profile_name.setText(UserName);
		}
		if (call_from != null) {
			slide_menu.setImageResource(R.drawable.back_arrow_white);
			things_to_try_lay.setVisibility(View.GONE);
			profile_addfriend_lay.setVisibility(View.GONE);
			notification_addfriend_lay.setVisibility(View.GONE);
		} else {
			slide_menu.setImageResource(R.drawable.slide_button);
			things_to_try_lay.setVisibility(View.VISIBLE);
			profile_addfriend_lay.setVisibility(View.GONE);
			notification_addfriend_lay.setVisibility(View.VISIBLE);
			if (mEnhancedProfile != null && mEnhancedProfile.equalsIgnoreCase("1")) {
				if (if_purchased.equalsIgnoreCase("0")) {
					enhanced_profile_lay.setVisibility(View.GONE);
					// eh_view.setVisibility(View.GONE);
				} else {
					enhanced_profile_lay.setVisibility(View.VISIBLE);
				}
			} else {
				enhanced_profile_lay.setVisibility(View.VISIBLE);
				mAdsLay.setVisibility(View.GONE);
			}
		}
		if (extras != null || mUser_Type.equalsIgnoreCase("buyer") || mUser_Type.equalsIgnoreCase("renter")) {
			enhanced_profile_lay.setVisibility(View.GONE);
			mAdsLay.setVisibility(View.GONE);
			// eh_view.setVisibility(View.GONE);
		}
		if (Friend_UserID != null && !Friend_UserID.equals("")) {
			mEdit_btn.setVisibility(View.INVISIBLE);
			address_txt.setVisibility(View.GONE);
			mChat_option.setVisibility(View.VISIBLE);
		} else {
			if (AppConstants.prop_friend_id.equalsIgnoreCase("")) {
				mEdit_btn.setVisibility(View.VISIBLE);
				mChat_option.setVisibility(View.GONE);
			} else {
				mEdit_btn.setVisibility(View.INVISIBLE);
				address_txt.setVisibility(View.GONE);
				mChat_option.setVisibility(View.VISIBLE);
			}
		}
		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);
		slide_holder.setAllowInterceptTouch(false);

		mChange_lay = (RelativeLayout) findViewById(R.id.change_lay);
		mChange_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPasswordChangeDialog();
			}
		});

		mChange_password_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPasswordChangeDialog();
			}
		});
	}

	private void getProfileDetails() {

		String url = AppConstants.BASE_URL + "myprofile";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		if (Friend_UserID != null && !Friend_UserID.equals("")) {
			params.put("user_id", Friend_UserID);
			params.put("friend_id", UserID);
		} else {
			if (AppConstants.prop_friend_id.equalsIgnoreCase("")) {
				params.put("user_id", UserID);
				params.put("friend_id", Friend_UserID);
			} else {
				params.put("user_id", AppConstants.prop_friend_id);
				params.put("friend_id", UserID);
			}
		}
		params.put("offset", 0);
		params.put("count", 100);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							profile_list = new Gson().fromJson(json.toString(), ProfileModel.class);
							onSuccessRequest(profile_list);
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));
						}
//						AppConstants.prop_friend_id = "";
					}
				});

	}

	private void onSuccessRequest(ProfileModel response) {

		if (response.getError_code().equals("1")) {

			if (response.getResult().getUser().get(0).getUser_id()
					.equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
				if (response.getResult().getUser().get(0).getUser_type().equalsIgnoreCase("email")) {
					mChange_password_lay.setVisibility(View.VISIBLE);
				} else {
					mChange_password_lay.setVisibility(View.GONE);
				}
			} else {
				mChange_password_lay.setVisibility(View.GONE);
			}

			if (response.getResult().getUser().get(0).getUser_id()
					.equalsIgnoreCase(GlobalMethods.getUserID(mContext))) {
				if (!response.getResult().getUser().get(0).getEnhanced_profile().equalsIgnoreCase("1")) {
					enhanced_profile_lay.setVisibility(View.VISIBLE);
				} else {
					enhanced_profile_lay.setVisibility(View.GONE);
				}
			} else {
				enhanced_profile_lay.setVisibility(View.GONE);
			}

			if (response.getResult().getIs_friend().equalsIgnoreCase("1")) {
				profile_add_friends_btn.setVisibility(View.GONE);
			}

			if (response.getResult().getUser().get(0).getRb_user() != null
					&& response.getResult().getUser().get(0).getRb_user().equalsIgnoreCase("0")) {
				mChat_option.setVisibility(View.GONE);
			}

			if (response.getResult().getUser().get(0).getCity().toString().equals("")) {

				address_txt.setText("");
			} else {
				address_txt.setText(response.getResult().getUser().get(0).getCity().toString() + ", "
						+ response.getResult().getUser().get(0).getAddress1().toString());
			}

			address_txt.setVisibility(View.INVISIBLE);

			if (response.getResult().getIs_friend() != null) {
				if (response.getResult().getIs_friend().equals("1")) {
					profile_addfriend_lay.setVisibility(View.GONE);
				}
			} else {
				profile_addfriend_lay.setVisibility(View.GONE);
			}

			profile_name.setText(response.getResult().getUser().get(0).getUser_name());
			fr_user_name = response.getResult().getUser().get(0).getUser_name();
			if (UserID.equalsIgnoreCase(response.getResult().getUser().get(0).getUser_id())) {

			} else {
				mMoreAboutMe.setText("More About " + response.getResult().getUser().get(0).getUser_name());
				mMyFeedText.setText(response.getResult().getUser().get(0).getUser_name() + " Feed");
			}

			user_count.clear();
			user_count.add(response.getResult().getFriendscount());
			user_count.add(response.getResult().getReviewscount());
			user_count.add(response.getResult().getPhotocount());
			user_count.add(response.getResult().getRating());

			aq().id(R.id.list_img11).progress(R.id.profile_img_progress).image(
					response.getResult().getUser().get(0).getUser_pic(), false, false, 200, R.drawable.profile_pic,
					null, 0);

			my_feed_list.clear();

			for (int i = 0; i < response.getResult().getMessageboard().size(); i++) {
				my_feeds_ent = new ProfileMyFeedsEntity();
				my_feeds_ent.setPost_id(response.getResult().getMessageboard().get(i).getPost_id());
				my_feeds_ent.setUser_id(response.getResult().getMessageboard().get(i).getUser_id());
				my_feeds_ent.setProperty_id(response.getResult().getMessageboard().get(i).getProperty_id());
				my_feeds_ent.setMessage(response.getResult().getMessageboard().get(i).getMessage());
				my_feeds_ent.setDatetime(response.getResult().getMessageboard().get(i).getDate_time());
				my_feeds_ent.setUser_name(response.getResult().getMessageboard().get(i).getUser_name());
				my_feeds_ent
						.setUser_profile_image(response.getResult().getMessageboard().get(i).getUser_profileImage());
				my_feeds_ent.setUser_rating(response.getResult().getMessageboard().get(i).getUser_avg_rating());
				my_feeds_ent.setFriends_count(response.getResult().getMessageboard().get(i).getFriends_count());
				my_feeds_ent.setReviews_count(response.getResult().getMessageboard().get(i).getReviews_count());
				my_feeds_ent.setPhotos_Count(response.getResult().getMessageboard().get(i).getPhotos_count());
				my_feeds_ent.setAddress(response.getResult().getMessageboard().get(i).getAddress());
				my_feeds_ent.setProperty_name(response.getResult().getMessageboard().get(i).getProperty_name());
				my_feeds_ent.setComments(response.getResult().getMessageboard().get(i).getMessage());

				my_feed_list.add(my_feeds_ent);
			}

			for (int i = 0; i < response.getResult().getReviews().size(); i++) {
				my_feeds_ent = new ProfileMyFeedsEntity();
				my_feeds_ent.setProperty_review_id(response.getResult().getReviews().get(i).getProperty_review_id());
				my_feeds_ent.setProperty_id(response.getResult().getReviews().get(i).getProperty_id());
				my_feeds_ent.setReview_user_id(response.getResult().getReviews().get(i).getReview_user_id());
				my_feeds_ent.setComments(response.getResult().getReviews().get(i).getComments());
				my_feeds_ent.setRating(response.getResult().getReviews().get(i).getRating());
				my_feeds_ent.setDatetime(response.getResult().getReviews().get(i).getDate_time());
				my_feeds_ent.setProperty_name(response.getResult().getReviews().get(i).getProperty_name());
				my_feeds_ent.setFriends_count(user_count.get(0));
				my_feeds_ent.setReviews_count(user_count.get(1));
				my_feeds_ent.setPhotos_Count(user_count.get(2));
				my_feeds_ent.setRating(user_count.get(3));
				my_feeds_ent.setUser_name(response.getResult().getUser().get(0).getUser_name());
				my_feeds_ent.setUser_profile_image(response.getResult().getUser().get(0).getUser_pic());
				my_feeds_ent.setAddress(response.getResult().getReviews().get(i).getAddress());
				my_feeds_ent.setDescription(response.getResult().getReviews().get(i).getDescription());
				my_feed_list.add(my_feeds_ent);
			}
			for (int i = 0; i < response.getResult().getPhotoandvideo().size(); i++) {
				my_feeds_ent = new ProfileMyFeedsEntity();
				my_feeds_ent.setUser_id(response.getResult().getPhotoandvideo().get(i).getUser_id());
				my_feeds_ent.setProperty_id(response.getResult().getPhotoandvideo().get(i).getProperty_id());
				my_feeds_ent.setFile(response.getResult().getPhotoandvideo().get(i).getFile());
				my_feeds_ent.setFile_type(response.getResult().getPhotoandvideo().get(i).getFile_type());
				my_feeds_ent.setPicture_id(response.getResult().getPhotoandvideo().get(i).getPicture_id());
				my_feeds_ent.setDatetime(response.getResult().getPhotoandvideo().get(i).getDatetime());
				my_feeds_ent.setProperty_name(response.getResult().getPhotoandvideo().get(i).getProperty_name());
				my_feeds_ent.setFriends_count(user_count.get(0));
				my_feeds_ent.setReviews_count(user_count.get(1));
				my_feeds_ent.setPhotos_Count(user_count.get(2));
				my_feeds_ent.setRating(user_count.get(3));
				my_feeds_ent.setUser_name(response.getResult().getUser().get(0).getUser_name());
				my_feeds_ent.setUser_profile_image(response.getResult().getUser().get(0).getUser_pic());
				my_feeds_ent.setAddress(response.getResult().getPhotoandvideo().get(i).getAddress());
				my_feeds_ent.setDescription(response.getResult().getPhotoandvideo().get(i).getDescription());

				my_feed_list.add(my_feeds_ent);

			}
			Collections.sort(my_feed_list, ProfileMyFeedsEntity.DATE_SORT);
			new MyFeedsAdapter(this, my_feed_list).getView(feeds_list);
			parent_scroll.fullScroll(View.FOCUS_UP);
			if (extras == null) {
				profile_addfriend_lay.setVisibility(View.GONE);
			}
			if (profile_list.getResult().getAccept().contains("3")) {
				chat_icon.setVisibility(View.GONE);
			} else {
				chat_icon.setVisibility(View.VISIBLE);
			}

		}
	}

	private void callGroupIdService() {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", Friend_UserID);
		params.put("name", "");
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							ChatSendResponse obj = new Gson().fromJson(json.toString(), ChatSendResponse.class);
							// onSuccessRequest(obj);
							callChatService(obj.result);
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	private void callChatService(String group_id) {
		Intent intent = new Intent(ProfileScreen.this, FriendChatScreen.class);
		intent.putExtra("ids", Friend_UserID);
		intent.putExtra("names", fr_user_name);
		intent.putExtra("groupId", group_id);
		intent.putExtra("type", "group");
		intent.putExtra("enhanced_profile_ids", profile_list.getResult().getUser().get(0).getEnhanced_profile());
		if (profile_list.getResult().getIs_friend().equalsIgnoreCase("1")) {
			intent.putExtra("from_call", "");
		} else {
			intent.putExtra("from_call", "hotleads");
		}
		startActivity(intent);
	}

	public void onClick(View v) {
		ison = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SHOWADS);
		if (v.getId() == R.id.slide) {
			if (call_from != null) {
				finish();
				AppConstants.isAPI = true;
			} else {
				slide_holder.toggle();
			}
		} else if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			switch (v.getId()) {
			case R.id.settings_icon:
				showProfileSettingDialog();
				break;
			case R.id.chat_icon:
				callGroupIdService();
				break;
			case R.id.slide:
				if (call_from != null) {
					AppConstants.prop_friend_id = "";
					AppConstants.isAPI = true;
					finish();
				} else {
					slide_holder.toggle();
				}
				break;
			case R.id.list_img11:
				if (call_from != null) {
					System.out.println("not touchable");
				} else {
					mImgID = v.getId();
					ProfileImageSelectionUtil.showOptionNew(this, "pic");
				}
				break;
			case R.id.notification_lay:
				// DialogManager.showCustomAlertDialog(ProfileScreen.this,
				// ProfileScreen.this, "Not Yet Integrated.");
				AppConstants.from_profile_notification = "true";
				Intent intee = new Intent(ProfileScreen.this, NotificationActivity.class);
				startActivity(intee);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.review_lay:
				AppConstants.from_profile_reviews = "true";
				Intent inte = new Intent(ProfileScreen.this, MyReviewActivity.class);
				if (call_from != null) {
					inte.putExtra("show_add", "1");
				}
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.photo_video_lay:
				Intent ints = new Intent(ProfileScreen.this, ProfilePhotoVideoScreen.class);
				ints.putExtra("review_list", my_feed_list);
				ints.putExtra("profile_image", profile_list.getResult().getUser().get(0).getUser_pic());
				ints.putExtra("friend_count", profile_list.getResult().getFriendscount());
				ints.putExtra("review_count", profile_list.getResult().getReviewscount());
				ints.putExtra("photo_count", profile_list.getResult().getPhotocount());
				if (call_from != null) {
					ints.putExtra("show_add", "1");
				}
				startActivity(ints);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.more_about_lay:
				Intent more_abouts = new Intent(ProfileScreen.this, ProfileMoreAboutActivity.class);
				more_abouts.putExtra("username", profile_list.getResult().getUser().get(0).getUser_name());
				more_abouts.putExtra("friend_count", user_count.get(0));
				more_abouts.putExtra("review_count", user_count.get(1));
				more_abouts.putExtra("photo_count", user_count.get(2));
				more_abouts.putExtra("rating_count", user_count.get(3));
				more_abouts.putExtra("profile_pic", profile_list.getResult().getUser().get(0).getUser_pic());
				more_abouts.putExtra("agent_id", profile_list.getResult().getUser().get(0).getUser_id());
				more_abouts.putExtra("business_name", profile_list.getResult().getUser().get(0).getBusiness_name());
				if (call_from != null) {
					more_abouts.putExtra("show_add", "1");
				}
				startActivity(more_abouts);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.add_friends_lay:
				AppConstants.from_profile_friends = "true";
				Intent friend = new Intent(ProfileScreen.this, FriendSearchScreen.class);
				startActivity(friend);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.schedule_video_chat_lay:
				AppConstants.from_profile_friends = "true";
				Intent friends = new Intent(ProfileScreen.this, FriendsMainScreen.class);
				startActivity(friends);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.add_photo_video_lay:
				AppConstants.from_map_list = false;
				AppConstants.from_profile_list = "true";
				Intent map = new Intent(ProfileScreen.this, MapFragmentActivity.class);
				startActivity(map);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.notification_btn:
				// DialogManager.showCustomAlertDialog(ProfileScreen.this,
				// ProfileScreen.this, "Not Yet Integrated.");
				AppConstants.from_profile_notification = "true";
				Intent intees = new Intent(ProfileScreen.this, NotificationActivity.class);
				startActivity(intees);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.review_btn:
				AppConstants.from_profile_reviews = "true";
				Intent intes = new Intent(ProfileScreen.this, MyReviewActivity.class);
				if (call_from != null) {
					intes.putExtra("show_add", "1");
				}
				startActivity(intes);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.photo_video_btn:
				Intent intss = new Intent(ProfileScreen.this, ProfilePhotoVideoScreen.class);
				intss.putExtra("review_list", my_feed_list);
				intss.putExtra("profile_image", profile_list.getResult().getUser().get(0).getUser_pic());
				intss.putExtra("friend_count", profile_list.getResult().getFriendscount());
				intss.putExtra("review_count", profile_list.getResult().getReviewscount());
				intss.putExtra("photo_count", profile_list.getResult().getPhotocount());
				intss.putExtra("username", profile_list.getResult().getUser().get(0).getUser_name());
				if (call_from != null) {
					intss.putExtra("show_add", "1");
				}
				startActivity(intss);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.more_about_btn:
				Intent more_about = new Intent(ProfileScreen.this, ProfileMoreAboutActivity.class);
				more_about.putExtra("username", profile_list.getResult().getUser().get(0).getUser_name());
				more_about.putExtra("friend_count", user_count.get(0));
				more_about.putExtra("review_count", user_count.get(1));
				more_about.putExtra("photo_count", user_count.get(2));
				more_about.putExtra("rating_count", user_count.get(3));
				more_about.putExtra("profile_pic", profile_list.getResult().getUser().get(0).getUser_pic());
				more_about.putExtra("agent_id", profile_list.getResult().getUser().get(0).getUser_id());
				more_about.putExtra("business_name", profile_list.getResult().getUser().get(0).getBusiness_name());
				if (call_from != null) {
					more_about.putExtra("show_add", "1");
				}
				startActivity(more_about);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.add_friends_btn:
				AppConstants.from_profile_friends = "true";
				Intent friendss = new Intent(ProfileScreen.this, FriendSearchScreen.class);
				startActivity(friendss);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.schedule_video_chat_btn:
				AppConstants.from_profile_friends = "true";
				Intent frien = new Intent(ProfileScreen.this, FriendsMainScreen.class);
				startActivity(frien);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.add_photo_video_btn:
				AppConstants.from_map_list = false;
				AppConstants.from_profile_list = "true";
				Intent maps = new Intent(ProfileScreen.this, MapFragmentActivity.class);
				startActivity(maps);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				break;
			case R.id.profile_add_friends_btn:
				callSendRequestService();
				break;
			case R.id.enhanced_profile_lay:
				// upgardeToEnhancedProfile();
				showPaymentDialogAlert();
				break;
			case R.id.enhanced_btn:
				// upgardeToEnhancedProfile();
				showPaymentDialogAlert();
				break;
			case R.id.ads_toggle_btn:
				if (ison.equalsIgnoreCase("true")) {
					ads_toggle_btn.setBackgroundResource(R.drawable.toggle_on);
					GlobalMethods.storeValuetoPreference(ProfileScreen.this, GlobalMethods.STRING_PREFERENCE,
							AppConstants.SHOWADS, "false");
				} else {
					ads_toggle_btn.setBackgroundResource(R.drawable.toggle_off);
					GlobalMethods.storeValuetoPreference(ProfileScreen.this, GlobalMethods.STRING_PREFERENCE,
							AppConstants.SHOWADS, "true");
				}
				break;
			case R.id.edit_user_name_lay:
				if (Friend_UserID != null && !Friend_UserID.equals("")) {

				} else {
					showCustomUserNameChangeDialog();
				}
				break;
			}
		}
	}

	private void showProfileSettingDialog() {
		d3 = new Dialog(ProfileScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.profile_setting_dialog);
		d3.setCancelable(false);

		RelativeLayout block_user = (RelativeLayout) d3.findViewById(R.id.block_user);
		RelativeLayout report_user = (RelativeLayout) d3.findViewById(R.id.report_user);
		RelativeLayout cancel_dialog = (RelativeLayout) d3.findViewById(R.id.cancel_dialog);
		final TextView block_txt = (TextView) d3.findViewById(R.id.block_txt);

		String accept[] = profile_list.getResult().getAccept().split(",");
		if (accept.length >= 2) {
			if (accept[1].equalsIgnoreCase(UserID)) {
				block_txt.setText("Un Block");
			} else {
				block_txt.setText("Block");
			}
		} else {
			block_txt.setText("Block");
		}

		block_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (profile_list.getResult().getIs_friend() != null
						&& profile_list.getResult().getIs_friend().equalsIgnoreCase("1")) {
					String accept[] = profile_list.getResult().getAccept().split(",");
					if (accept.length >= 2) {
						if (!accept[1].equalsIgnoreCase(UserID)) {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									"You are not a friend with "
											+ profile_list.getResult().getUser().get(0).getUser_name());
						} else {
							blockUserProfile("unblock");
						}
					} else {
						if (block_txt.getText().toString().equalsIgnoreCase("Un Block")) {
							blockUserProfile("unblock");
						} else {
							blockUserProfile("block");
						}
					}
				} else {
					if (block_txt.getText().toString().equalsIgnoreCase("Un Block")) {
						blockUserProfile("unblock");
					} else {
						DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
								"You are not a friend with "
										+ profile_list.getResult().getUser().get(0).getUser_name());
					}
				}
				d3.dismiss();
			}
		});

		report_user.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent email = new Intent(Intent.ACTION_SEND);
				email.putExtra(Intent.EXTRA_EMAIL, new String[] { "admin@rentersblock.com" });
				email.putExtra(Intent.EXTRA_SUBJECT,
						"Report on " + profile_list.getResult().getUser().get(0).getUser_name());
				email.putExtra(Intent.EXTRA_TEXT, "From rentersblock");
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email, "Choose an Email client :"));
				d3.dismiss();
			}
		});

		cancel_dialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
			}
		});

		d3.show();
	}

	private void blockUserProfile(final String type) {
		String url = "";
		if (type.equalsIgnoreCase("block")) {
			url = AppConstants.BASE_URL + "friend/block";
		} else {
			url = AppConstants.BASE_URL + "friend/unblock";
		}
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		if (Friend_UserID != null && !Friend_UserID.equalsIgnoreCase("")) {
			params.put("friend_user_id", Friend_UserID);
		} else {
			params.put("friend_user_id", AppConstants.prop_friend_id);
		}

		aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							CommonResponse obj = new Gson().fromJson(json.toString(), CommonResponse.class);
							onRequestSucces(obj);
						} else {
							// statusErrorCodes(status);
						}
					}
				});
	}

	private void onRequestSucces(CommonResponse response) {
		DialogManager.showCustomAlertDialog(this, this, response.getMsg());
	}

	private void showCustomUserNameChangeDialog() {
		d3 = new Dialog(ProfileScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		d3.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d3.setContentView(R.layout.chat_group_name);
		d3.setCancelable(false);
		Button cancel = (Button) d3.findViewById(R.id.cancel);
		cancel.setText("Cancel");
		Button save = (Button) d3.findViewById(R.id.save);

		TextView text1 = (TextView) d3.findViewById(R.id.text1);
		text1.setText("Update your UserName");

		mUsername = (EditText) d3.findViewById(R.id.enter_search_name);
		mUsername.setHint("UserName");

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				d3.dismiss();
				callUsernameUpdateService(mUsername.getText().toString());
			}
		});

		d3.show();
	}

	private void callUsernameUpdateService(String user_name) {
		String url = AppConstants.BASE_URL + "username";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("user_name", user_name);

		aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							CommonResponse obj = new Gson().fromJson(json.toString(), CommonResponse.class);
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, obj.getMsg());
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));

						}
					}
				});
	}

	EditText card_number, cvv_number, card_holder_name;
	Button expire_date;
	private int year;
	private int month;
	private int day;
	private Dialog payment_alert_dialog;
	private Dialog change_password_dialog;

	private void showPaymentDialogAlert() {
		payment_alert_dialog = new Dialog(ProfileScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		payment_alert_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		payment_alert_dialog.setContentView(R.layout.payment_alert_dialog);
		payment_alert_dialog.setCancelable(true);

		payment_alert_dialog.show();

		card_number = (EditText) payment_alert_dialog.findViewById(R.id.card_number);
		expire_date = (Button) payment_alert_dialog.findViewById(R.id.expire_date);
		cvv_number = (EditText) payment_alert_dialog.findViewById(R.id.cvv_number);
		card_holder_name = (EditText) payment_alert_dialog.findViewById(R.id.card_holder_name);
		Button pay = (Button) payment_alert_dialog.findViewById(R.id.payment);
		Button close = (Button) payment_alert_dialog.findViewById(R.id.btn_close);

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				payment_alert_dialog.dismiss();
			}
		});

		expire_date.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});

		pay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				validatePayment();
			}
		});

	}

	private void showPasswordChangeDialog() {

		change_password_dialog = new Dialog(ProfileScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
		change_password_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		change_password_dialog.setContentView(R.layout.dialog_change_password);
		change_password_dialog.setCancelable(true);

		change_password_dialog.show();

		final EditText current_password = (EditText) change_password_dialog.findViewById(R.id.current_password);
		final EditText new_password = (EditText) change_password_dialog.findViewById(R.id.new_password);
		final EditText confirm_new_password = (EditText) change_password_dialog.findViewById(R.id.confirm_new_password);
		Button update = (Button) change_password_dialog.findViewById(R.id.update);
		Button close = (Button) change_password_dialog.findViewById(R.id.btn_close);

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				change_password_dialog.dismiss();
			}
		});

		update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				change_password_dialog.dismiss();
				String mCurrentpw = current_password.getText().toString();
				String mNewpw = new_password.getText().toString();
				String mConfirm_pw = confirm_new_password.getText().toString();
				if (isValidate(mCurrentpw, mNewpw, mConfirm_pw)) {
					callUpdateAPI(mCurrentpw, mNewpw, mConfirm_pw);
				}
			}
		});

	}

	private boolean isValidate(String mCurrentpw, String mNewpw, String mConfirm_pw) {

		if (mCurrentpw.equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
					"Please Enter Current password.");
			return false;
		} else if (mNewpw.equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, "Please Enter New password.");
			return false;
		} else if (mConfirm_pw.equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
					"Please Enter Confirm New password.");
			return false;
		} else if (!validateMatchPass(mNewpw, mConfirm_pw)) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, "Password doesn't match.");
		}
		return true;
	}

	private boolean validateMatchPass(String mNewpw, String mConfirm_pw) {
		if (!mConfirm_pw.equals(mNewpw)) {
			return false;
		} else {
			return true;
		}
	}

	private void callUpdateAPI(String mCurrentpw, String mNewpw, String mConfirm_pw) {
		String url = AppConstants.BASE_URL + "changepassword";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("password", mNewpw);

		aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							CommonResponse obj = new Gson().fromJson(json.toString(), CommonResponse.class);
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, obj.getMsg());
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));

						}
					}
				});
	}

	private void validatePayment() {
		if (card_number.getText().toString().trim().equals("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, "Please Enter a Card Number.");
		} else if (expire_date.getText().toString().trim().equals("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, "Please Enter a Expire Date.");
		} else if (cvv_number.getText().toString().trim().equals("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this, "Please Enter a CVV Number.");
		} else if (card_holder_name.getText().toString().trim().equals("")) {
			DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
					"Please Enter a Card Holder's Name.");
		} else {
			payment_alert_dialog.dismiss();
			callPaymentService();
		}
	}

	// http://rentersblock.com/payment/pay/test.php?
	// card=42424242424242&exp_year=2016&exp_month=10
	// &cvc=123&amount=100&user_id=450

	private void callPaymentService() {
		progres = ProgressDialog.show(ProfileScreen.this, null, null);
		progres.setContentView(R.layout.custom_dialog);
		progres.show();
		String mAmount = "";
		if (selectedType.equalsIgnoreCase(AppConstants.AGENT) || selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mAmount = "50";
		} else if (selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAmount = "500";
		}
		String url = "http://rentersblock.com/payment/pay/test.php?" + "card=" + card_number.getText().toString()
				+ "&exp_year=" + card_year + "&exp_month=" + card_month + "&cvc=" + cvv_number.getText().toString()
				+ "&amount=" + mAmount + "&user_id=" + UserID;

		webView.setWebViewClient(new MyWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
		webView.requestFocus();
		webView.clearCache(true);
		android.webkit.CookieManager.getInstance().removeAllCookie();
	}

	public class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			// progres.show();
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (url.contains("message")) {
				if (progres != null) {
					progres.dismiss();
					progres = null;
				}

				Uri uri = Uri.parse(url);
				String message = uri.getQueryParameter("message");
				String errorcode = uri.getQueryParameter("error_code");

				GlobalMethods.storeValuetoPreference(ProfileScreen.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.ENHANCED_PROFILE, "1");

				stopWebview(message, errorcode);
				upgardeToEnhancedProfile();

			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

	private void stopWebview(String message, String errorcode) {
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.setVisibility(View.GONE);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			final Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			return new DatePickerDialog(this, datePickerListener, year, month, day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			String month_len = String.valueOf(month + 1);
			if (month_len.length() == 1) {
				month_len = "0" + month_len;
			}
			card_month = month_len;
			card_year = String.valueOf(year);
			String year_len = String.valueOf(year);
			year_len = year_len.substring(2);
			expire_date.setText(new StringBuilder().append(month_len).append("/").append(year_len).append(" "));

		}
	};

	private void upgardeToEnhancedProfile() {
		String url = AppConstants.BASE_URL + "registration/updatetype";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(mContext)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									"Successfully Updated to Enhanced Profile.");
							GlobalMethods.storeValuetoPreference(ProfileScreen.this, GlobalMethods.STRING_PREFERENCE,
									AppConstants.ENHANCED_PROFILE, "1");
							enhanced_profile_lay.setVisibility(View.GONE);
							// eh_view.setVisibility(View.GONE);
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));
						}
					}
				});
	}

	private void callSendRequestService() {

		String url = AppConstants.BASE_URL + "friend/sendfrindrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_user_id", Friend_UserID);
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							FriendsResponse obj = new Gson().fromJson(json.toString(), FriendsResponse.class);
							onRequestSuccess(obj);
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));
						}

					}
				});
	}

	private void onRequestSuccess(FriendsResponse response) {

		DialogManager.showCustomAlertDialog(this, this, response.getMsg());
	}

	public static class Utility {
		public static void setListViewHeightBasedOnChildren(ListView listView) {
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}

			int totalHeight = 0;
			int desiredWidth = MeasureSpec.makeMeasureSpec(listView.getWidth(), MeasureSpec.AT_MOST);
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			listView.setLayoutParams(params);
			listView.requestLayout();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {

			if (resultCode == RESULT_OK) {

				if (requestCode == ProfileImageSelectionUtil.CAMERA
						|| requestCode == ProfileImageSelectionUtil.GALLERY) {

					Bitmap image = ProfileImageSelectionUtil.getImage(data, this);

					if (image != null) {
						if (requestCode == ProfileImageSelectionUtil.CAMERA) {
							if (ProfileImageSelectionUtil.isUriTrue) {
								image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, data.getData(),
										image);
							} else {
								image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, image);
							}
						} else {

							Uri selectedImage = data.getData();

							image = ProfileImageSelectionUtil.getCorrectOrientationImage(this, selectedImage, image);
						}

						mBitmapFromDevice = image;
						updateProfileImage();
						setImage(mImgID, mBitmapFromDevice);
					} else {
						DialogManager.showCustomAlertDialog(this, this, "Unsupported file format");
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateProfileImage() {
		String url = AppConstants.BASE_URL + "addprofileimage";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		mBitmapFromDevice.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
		byte[] mainImageData = (oututStream).toByteArray();
		params.put("profile_image", mainImageData);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							System.out.println(json);
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									"Profile Image updated Successfully.");
						} else {
							DialogManager.showCustomAlertDialog(ProfileScreen.this, ProfileScreen.this,
									getString(R.string.server_error));
						}
					}
				});
	}

	@Override
	public void onOkclick() {

		super.onOkclick();
		getProfileDetails();
	}

	private void setImage(int mImgID, Bitmap mBitmap) {

		mBitmapFromDevice = mBitmap;
		ByteArrayOutputStream oututStream = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, oututStream);
		byte[] mainImageData = (oututStream).toByteArray();

		if (mImgID == R.id.list_img11) {
			mPropImg1 = mainImageData;
			mListImg1.setImageBitmap(mBitmap);
		}
	}

	public void onUserClick(View v) {
		onMenuUserNameClick(v);
	}

	public void onbuyMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuBuyClick(v);
			slide_holder.toggle();
		}
	}

	public void onSellerMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuSellerclick(v);
			slide_holder.toggle();
		}
	}

	public void onAgentMenuClick(View v) {
		if (AppConstants.view_id == v.getId()) {
			slide_holder.toggle();
		} else {
			onMenuAgentClick(v);
			slide_holder.toggle();
		}
	}

	public static void showImageVideoDialog(String file_type, String file, String desc, String time) {

		((ProfileScreen) mContext).showCustomViewDialog(file_type, file, desc, time);
	}

	private void showCustomViewDialog(String file_type, String file, String desc, String time) {
		final VideoView profile_video;
		RelativeLayout adap_vidl1;
		final ProgressBar video_prog;
		FrameLayout frame;
		MediaController controller;
		d2 = new Dialog(ProfileScreen.this, android.R.style.Theme_Translucent_NoTitleBar);
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
		RelativeLayout main_lay = (RelativeLayout) d2.findViewById(R.id.parent_layout);
		pause_btn = (Button) d2.findViewById(R.id.adap_video_pause_btn1);

		// time, description and profile Details
		TextView desc_text = (TextView) d2.findViewById(R.id.desc_text);
		TextView time_txt = (TextView) d2.findViewById(R.id.date_time);
		TextView user_name = (TextView) d2.findViewById(R.id.user_name);
		TextView friends_count = (TextView) d2.findViewById(R.id.friends_count);
		TextView reviews_count = (TextView) d2.findViewById(R.id.reviews_count);
		TextView photos_count = (TextView) d2.findViewById(R.id.photos_count);
		ImageView profile_img = (ImageView) d2.findViewById(R.id.profile_img);

		aq().id(profile_img).image(profile_list.getResult().getUser().get(0).getUser_pic(), true, true, 0, 0, null, 0,
				1.0f / 1.0f);

		user_name.setText(profile_list.getResult().getUser().get(0).getUser_name());
		friends_count.setText(profile_list.getResult().getFriendscount());
		reviews_count.setText(profile_list.getResult().getReviewscount());
		photos_count.setText(profile_list.getResult().getPhotocount());

		time_txt.setText("Latest updated " + GlobalMethods.timeDiff(time));
		desc_text.setText(desc);

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
					Toast.makeText(ProfileScreen.this, "Sorry! Video Cannot be Played.", Toast.LENGTH_SHORT).show();
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

	class ImageLoadTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

			try {
				URL urlConnection = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) urlConnection.openConnection();
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
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		AppConstants.prop_friend_id = "";
	}

}
