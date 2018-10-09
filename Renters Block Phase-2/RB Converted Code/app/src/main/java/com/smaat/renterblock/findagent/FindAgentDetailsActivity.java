package com.smaat.renterblock.findagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.MultipleCategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONObject;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ListingActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.ui.ShareAppActivity.TwDialogListener;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.TwitterDialog;
import com.smaat.renterblock.util.TypefaceSingleton;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION_CODES;
import android.text.Html;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class FindAgentDetailsActivity extends BaseActivity implements OnClickListener {
	private TextView mReviewCommentsTxt, mReviewCommentsFullTxt, mReadmoreTxt, mUserName, mReivewUserName, mFriedsCount,
			mReviewFriendsCount, mReviewsCount, mReviewReviewsCount, mPhotosCount, mReviewPhotosCount, mAgentWith,
			mLicense, mLatestReview, mListingTxt;
	private RelativeLayout mReviewUserLay;
	private RatingBar mRatingBar, mReviewRatingBar;
	private Bundle mBundle;
	private AgentUserDetailsResult mResult;
	private ArrayList<AgentReviewsEntity> mReviewsEntityList;
	private String mUserID, mEnhance_profile, mIs_friend, mUser_name;
	private GraphicalView mChartView;
	private LinearLayout mChartLay;

	/**
	 * Twitter Share
	 */
	private static SharedPreferences mSharedPreferences;
	private ProgressDialog progress;
	private boolean isLogin = false;
	static String TWITTER_CONSUMER_KEY = "uzavtLKSl3NxxkWEkQ0SEeUPQ";
	static String TWITTER_CONSUMER_SECRET = "1TC3DuMerepZtzQvMmOqjVRe8Au1hpnM4kykzMURl1Ul5I1AD2";

	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-twitter";
	public static final String OAUTH_CALLBACK_HOST = "callback";
	public static final String CALLBACK_URL = "x-oauthflow-twitter://callback";

	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

	private static Twitter twitter;
	private static RequestToken requestToken;

	// private Session.StatusCallback statusCallback = new
	// SessionStatusCallback();

	private ImageView user_image, review_user_image;

	private TextView header_txt;
	private Button chat_icon;
	private String is_rb_user;

	private CallbackManager callbackManager;
	private LoginManager loginManager;
	ShareDialog shareDialog;
	String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_agent_details);
		FacebookSdk.sdkInitialize(getApplicationContext());
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		// Typeface mTypeface =
		// TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		initComponents();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (android.os.Build.VERSION.SDK_INT > VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy policys = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policys);
		}

		createTwitterAccess();
	}

	private void createTwitterAccess() {
		if (TWITTER_CONSUMER_KEY.trim().length() == 0 || TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			DialogManager.showCustomAlertDialog(this, this,
					"Twitter oAuth tokens,Please set your twitter oauth tokens first!");
			return;
		}
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
		if (isTwitterLoggedInAlready()) {
			isLogin = true;
		}
		if (!isTwitterLoggedInAlready()) {

			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					Editor e = mSharedPreferences.edit();

					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit();

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				} catch (Exception e) {
					Log.e("Twitter Login Error", "> " + e.getMessage());
				}
			}
		}
	}

	private boolean isTwitterLoggedInAlready() {
		return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
	}

	private void initComponents() {

		mUserName = (TextView) findViewById(R.id.user_name_txt);
		mFriedsCount = (TextView) findViewById(R.id.friends_count);
		mReviewsCount = (TextView) findViewById(R.id.reviews_count);
		mPhotosCount = (TextView) findViewById(R.id.photos_count);
		mAgentWith = (TextView) findViewById(R.id.agent_with_txt);
		mLicense = (TextView) findViewById(R.id.license_txt);
		mReadmoreTxt = (TextView) findViewById(R.id.read_more_txt);
		mRatingBar = (RatingBar) findViewById(R.id.user_ratingbar);
		mReadmoreTxt.setVisibility(View.GONE);

		chat_icon = (Button) findViewById(R.id.chat_icon);

		mReivewUserName = (TextView) findViewById(R.id.review_user_name_txt);
		mReviewCommentsTxt = (TextView) findViewById(R.id.review_comments_txt);
		mReviewCommentsFullTxt = (TextView) findViewById(R.id.review_comments_full_txt);
		mReviewFriendsCount = (TextView) findViewById(R.id.review_friends_count);
		mReviewReviewsCount = (TextView) findViewById(R.id.review_reviews_count);
		mReviewPhotosCount = (TextView) findViewById(R.id.review_photos_count);
		mReviewRatingBar = (RatingBar) findViewById(R.id.review_user_ratingbar);
		mLatestReview = (TextView) findViewById(R.id.latest_review);
		mReviewUserLay = (RelativeLayout) findViewById(R.id.review_user_lay);
		mListingTxt = (TextView) findViewById(R.id.listing_txt);

		user_image = (ImageView) findViewById(R.id.user_image);
		review_user_image = (ImageView) findViewById(R.id.review_user_image);
		review_user_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent profile = new Intent(FindAgentDetailsActivity.this, ProfileScreen.class);
				profile.putExtra("user_id", mUserID);
				profile.putExtra("call_from", "Agent");
				startActivity(profile);
			}
		});
		user_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent profile = new Intent(FindAgentDetailsActivity.this, ProfileScreen.class);
				profile.putExtra("user_id", mUserID);
				profile.putExtra("call_from", "Agent");
				startActivity(profile);
			}
		});

		mChartLay = (LinearLayout) findViewById(R.id.chart_lay);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {

			mUserID = mBundle.getString("mUserID");
			mEnhance_profile = mBundle.getString("mEnhanced_profile");
			mIs_friend = mBundle.getString("is_friend");
			mUser_name = mBundle.getString("mfr_Username");
			is_rb_user = mBundle.getString("rb_user");

			if (is_rb_user != null && is_rb_user.equalsIgnoreCase("1")) {
				chat_icon.setVisibility(View.VISIBLE);
			} else {
				chat_icon.setVisibility(View.GONE);
			}

			if (mUserID != null) {
				callGetUserDetails();
			}

		}

		if (mUserID != null && mUserID.equalsIgnoreCase(UserID)) {
			chat_icon.setVisibility(View.GONE);
		}

	}

	private void callGetUserDetails() {
		String Url = AppConstants.BASE_URL + "reviewuser/view";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("review_user_id", mUserID);

		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {

						if (json != null) {
							AgentMoreReviewsResponse response = new Gson().fromJson(json.toString(),
									AgentMoreReviewsResponse.class);
							if (response.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

								mResult = response.getResult();
								mReviewsEntityList = mResult.getReviewresult();
								setValues();
							}

						} else {
							statusErrorCode(status);
						}

					}
				});

	}

	private void setValues() {

		mUserName.setText(mResult.getUserresult().getName() + " " + mResult.getUserresult().getLast_name());
		mFriedsCount.setText(mResult.getUserresult().getFriends_count());
		mReviewsCount.setText(mResult.getUserresult().getReviews_count());
		mPhotosCount.setText(mResult.getUserresult().getPhotos_count());
		mAgentWith.setText(
				mResult.getUserresult().getUser_type() + " with " + mResult.getUserresult().getBusiness_name());
		mLicense.setText(mResult.getUserresult().getLicence());
		mListingTxt.setText("(" + mResult.getUserresult().getProperty_listing() + ")");
		float userRating = Float.parseFloat(mResult.getUserresult().getUser_avg_rating());
		mRatingBar.setRating(userRating);

		aq().id(R.id.user_image).image(mResult.getUserresult().getUser_profileImage(), false, false, 200,
				R.drawable.profile_pic, null, 0);

		if (mResult.getReviewresult().size() != 0) {

			mReviewUserLay.setVisibility(View.VISIBLE);
			mLatestReview.setVisibility(View.VISIBLE);

			mReivewUserName.setText(mResult.getReviewresult().get(0).getName());
			mReviewFriendsCount.setText(mResult.getReviewresult().get(0).getFriends_count());
			mReviewReviewsCount.setText(mResult.getReviewresult().get(0).getReviews_count());
			mReviewPhotosCount.setText(mResult.getReviewresult().get(0).getPhotos_count());

			mReviewCommentsTxt.setText(mResult.getReviewresult().get(0).getComments());
			mReviewCommentsFullTxt.setText(mResult.getReviewresult().get(0).getComments());

			if (mReviewCommentsTxt.getText().toString().length() >= 70) {
				mReadmoreTxt.setVisibility(View.VISIBLE);
			} else {
				mReadmoreTxt.setVisibility(View.GONE);
			}

			float reviewUserRating = Float.parseFloat(mResult.getReviewresult().get(0).getRating());
			mReviewRatingBar.setRating(reviewUserRating);

			aq().id(R.id.review_user_image).image(mResult.getReviewresult().get(0).getUser_profileImage(), false, false,
					200, R.drawable.profile_pic, null, 0);

		} else {
			mLatestReview.setVisibility(View.GONE);
			mReviewUserLay.setVisibility(View.GONE);
		}

		Intent intent = null;
		intent = new PieChart().execute(this, mChartLay);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				callGroupIdService();
			}
			break;
		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
			break;
		case R.id.request_button:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mResult.getUserresult().getRb_user().equalsIgnoreCase("1")) {
					Intent intent = new Intent(FindAgentDetailsActivity.this, FindAgentContactUserActivity.class);
					intent.putExtra("mAgentResult", mResult.getUserresult());
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				} else {
					String body = "<HTML><Body>Hi, I saw your available listings "
							+ "on Renter's Block and would like to learn more about "
							+ "a specific listing. Please <a href=\"goo.gl/DGDC1v\">LOGIN!</a> "
							+ "and feel free to message me through the app or website. Looking forward to connecting with you!</BODY></HTML>";
					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { mResult.getUserresult().getEmail_id() });
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Hot Lead from Renter's Block");
					emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
					emailIntent.setType("text/plain");
					startActivity(emailIntent);
				}
			}
			break;
		case R.id.review_button:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				Intent intent1 = new Intent(FindAgentDetailsActivity.this, AgentPostReviewActivity.class);
				intent1.putExtra("ReviewUserID", mResult.getUserresult().getUser_id());
				startActivity(intent1);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
		case R.id.read_more_txt:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				mReviewCommentsTxt.setVisibility(View.GONE);
				mReviewCommentsFullTxt.setVisibility(View.VISIBLE);
				mReadmoreTxt.setVisibility(View.GONE);
			}
			break;
		case R.id.more_reviews_lay:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mResult.getReviewresult().size() == 0) {
					DialogManager.showCustomAlertDialog(this, this, "No Reviews Available");
				} else {
					Intent intent2 = new Intent(FindAgentDetailsActivity.this, AgentMoreReviewsActivity.class);
					intent2.putExtra("ReviewsEntityList", mReviewsEntityList);
					startActivity(intent2);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}

			break;
		case R.id.listings_lay:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				if (mResult.getUserresult().getProperty_listing().equalsIgnoreCase("0")) {
					DialogManager.showCustomAlertDialog(this, this, "No Listing Available");
				} else {

					AppConstants.from_profile_listing = "true";
					Intent intent3 = new Intent(FindAgentDetailsActivity.this, ListingActivity.class);
					intent3.putExtra("ReviewUserID", mResult.getUserresult().getUser_id());
					startActivity(intent3);
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
			}
			break;
		case R.id.about_lay:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				Intent intent4 = new Intent(FindAgentDetailsActivity.this, FindAgentAboutActivity.class);
				intent4.putExtra("mAgentResult", mResult.getUserresult());
				intent4.putExtra("mUserID", mUserID);
				intent4.putExtra("mEnhanced_profile", mEnhance_profile);
				intent4.putExtra("is_friend", mIs_friend);
				intent4.putExtra("mfr_Username", mUser_name);
				startActivity(intent4);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
			}
			break;
		case R.id.share_profile_lay:
			showShareDialog(this);
			break;

		}

	}

	private void callGroupIdService() {
		String Url = AppConstants.BASE_URL + "group";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("owner_id", UserID);
		params.put("users_id", mUserID);
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
		Intent intent = new Intent(FindAgentDetailsActivity.this, FriendChatScreen.class);
		intent.putExtra("ids", mUserID);
		intent.putExtra("names", mUser_name);
		intent.putExtra("groupId", group_id);
		intent.putExtra("type", "group");
		intent.putExtra("enhanced_profile_ids", mEnhance_profile);
		if (mIs_friend.equalsIgnoreCase("1")) {
			intent.putExtra("from_call", "");
		} else {
			intent.putExtra("from_call", "hotleads");
		}
		startActivity(intent);
	}

	private void showShareDialog(FindAgentDetailsActivity context) {
		mDialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.popup_share);
		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		RelativeLayout mParentLay = (RelativeLayout) mDialog.findViewById(R.id.parent_layout);
		LinearLayout mMsgIcon, mMailIcon, mTwitIcon, mFbIcon;

		mMsgIcon = (LinearLayout) mDialog.findViewById(R.id.msg_icon);
		mMailIcon = (LinearLayout) mDialog.findViewById(R.id.email_icon);
		mTwitIcon = (LinearLayout) mDialog.findViewById(R.id.twitter_icon);
		mFbIcon = (LinearLayout) mDialog.findViewById(R.id.facebook_icon);

		content = "Here's the email contact to your match made in realty heaven" + mResult.getUserresult().getName()
				+ " " + mResult.getUserresult().getLast_name() + " " + mResult.getUserresult().getEmail_id()
				+ " Download the Renter's Block app now goo.gl/DGDC1v";

		mMsgIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.setData(Uri.parse("sms:" + ""));
				smsIntent.putExtra("sms_body", "Renters Block App");
				startActivity(smsIntent);
			}
		});
		mMailIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mailintent = new Intent(Intent.ACTION_SENDTO);
				mailintent.setType("text/plain");
				mailintent.putExtra(Intent.EXTRA_SUBJECT, "Renter's Block App");
				mailintent.setData(Uri.parse("mailto:" + "smaatapps@gmail.com"));
				mailintent.putExtra(Intent.EXTRA_TEXT, content);
				mailintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mailintent);
			}
		});
		mTwitIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isLogin) {
					new LoginTwitter().execute("");
				} else {
					new updateTwitterStatus().execute("FFFF");
				}
			}
		});
		mFbIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// setFBPermissions();
				sharewithFB();
			}
		});
		mParentLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mDialog.dismiss();

			}
		});
		mDialog.show();
	}

	private void sharewithFB() {
		callbackManager = CallbackManager.Factory.create();

		shareDialog = new ShareDialog(this);
		if (ShareDialog.canShow(ShareLinkContent.class)) {
			ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle("Renter's Block App")
					.setContentDescription(content).setContentUrl(Uri.parse("goo.gl/DGDC1v"))
					.setImageUrl(Uri.parse("http://release.smaatapps.com/RB/rb_logo.png")).build();

			shareDialog.show(linkContent);
		}

	}

	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {

			DialogManager.showToast(FindAgentDetailsActivity.this,
					"There was some error in authenticating with Twitter.");
		}

		public void onComplete(String value) {

			Uri uri = Uri.parse(value);
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
				String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					// Shared Preferences
					Editor e = mSharedPreferences.edit();

					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
					// Store login status - true
					e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
					e.commit(); // save changes

					Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};

	class LoginTwitter extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(FindAgentDetailsActivity.this);
			progress.setMessage(getString(R.string.please_wait));
			progress.setIndeterminate(false);
			progress.setCancelable(false);
			progress.show();
		}

		protected String doInBackground(String... args) {
			// Check if already logged in
			if (!isTwitterLoggedInAlready()) {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
				builder.setUseSSL(true);
				Configuration configuration = builder.build();

				TwitterFactory factory = new TwitterFactory(configuration);
				twitter = factory.getInstance();

				try {
					requestToken = twitter.getOAuthRequestToken(CALLBACK_URL);

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							progress.dismiss();

							new TwitterDialog(FindAgentDetailsActivity.this, requestToken.getAuthenticationURL(),
									mTwLoginDialogListener).show();
						}
					});

				} catch (TwitterException e) {
					e.printStackTrace();
					return "exception";
				}
			} else {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (progress != null) {
							progress.dismiss();
						}
					}
				});
				logoutFromTwitter();

			}
			return "";

		}

		private boolean isTwitterLoggedInAlready() {
			// return twitter login status from Shared Preferences
			return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
		}

		protected void onPostExecute(final String file_url) {

			if (file_url.equalsIgnoreCase("exception")) {
				if (progress != null) {
					progress.dismiss();
				}
				DialogManager.showCustomAlertDialog(FindAgentDetailsActivity.this, FindAgentDetailsActivity.this,
						"No internet Connection.");
			} else {
				if (progress.isShowing()) {
					progress.dismiss();
				}
			}

		}
	}

	class updateTwitterStatus extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(FindAgentDetailsActivity.this);
			progress.setMessage("Updating to twitter...");
			progress.setIndeterminate(false);
			progress.setCancelable(false);
			progress.show();
		}

		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = content;
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				twitter4j.Status response = twitter.updateStatus(status);

				Log.d("Status", "> " + response.getText());

				return "success";
			} catch (TwitterException e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		protected void onPostExecute(final String file_url) {
			// dismiss the dialog after getting all products
			if (progress != null) {
				progress.dismiss();
			}
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					if (file_url != null && file_url.contains("403")) {
						Toast.makeText(getApplicationContext(), "Tweet Failed because your Tweet Limit Exceeded",
								Toast.LENGTH_SHORT).show();
					} else if (file_url != null && file_url.equalsIgnoreCase("success")) {
						Toast.makeText(getApplicationContext(), "Status tweeted successfully", Toast.LENGTH_SHORT)
								.show();
					} else if (file_url != null && file_url.contains("duplicate")) {
						Toast.makeText(getApplicationContext(), "Tweet Failed because of duplicate message.",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(), "Failed to Tweet", Toast.LENGTH_SHORT).show();
					}
					finish();
				}
			});
		}

	}

	private void logoutFromTwitter() {
		// Clear the shared preferences
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();
	}

	public class PieChart {

		public Intent execute(Context context, LinearLayout parent) {
			parent.removeAllViews();
			List<double[]> values = new ArrayList<double[]>();
			values.add(new double[] { 47, 53 });
			List<String[]> titles = new ArrayList<String[]>();
			titles.add(new String[] { "Sellers", "Buyers" });
			int[] colors = new int[] { Color.GREEN, Color.BLUE };

			DefaultRenderer renderer = new DefaultRenderer();

			renderer.setExternalZoomEnabled(true);
			renderer.setDisplayValues(false);
			renderer.setZoomEnabled(true);
			renderer.setShowLabels(false);
			renderer.setStartAngle(90);
			renderer.setPanEnabled(false);
			renderer.setMargins(new int[] { 10, 10, 10, 10 });
			renderer.setScale((float) 1.4);
			for (int color : colors) {
				SimpleSeriesRenderer r = new SimpleSeriesRenderer();
				r.setColor(color);
				renderer.addSeriesRenderer(r);
			}

			MultipleCategorySeries series = new MultipleCategorySeries("");
			int k = 0;
			for (double[] value : values) {
				series.add(titles.get(k), value);
				k++;
			}
			mChartView = ChartFactory.getDoughnutChartView(context, series, renderer);

			parent.addView(mChartView);

			return ChartFactory.getDoughnutChartIntent(context, series, renderer, null);
		}

	}

	@Override
	protected void onResume() {
		if (AppConstants.isAPI) {
			AppConstants.isAPI = false;
			callGetUserDetails();
		} else {
		}
		// Session.getActiveSession().removeCallback(statusCallback);
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();
		// Session.getActiveSession().removeCallback(statusCallback);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Session session = Session.getActiveSession();
		// session.close();
	}
}
