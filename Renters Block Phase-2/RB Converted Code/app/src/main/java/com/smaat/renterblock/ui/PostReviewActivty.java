package com.smaat.renterblock.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.json.JSONObject;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.ShareAppActivity.TwDialogListener;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TwitterDialog;
import com.smaat.renterblock.util.TypefaceSingleton;

public class PostReviewActivty extends BaseActivity implements OnClickListener,
		DialogMangerCallback {

	private TextView mReviewText, mClose, mPost;
	private Bundle mBundle;
	private String mComments = "", mRating = "0.0", mUserID = "",
			mPropertyId = "", mPropertyReviewId = "", mPropertyCommentId = "";
	private String mReviewType;
	private RatingBar mRatingBar;
	private String mCallBack = "";
	private ToggleButton twitter_share, facebook_share;
	private String property_type;
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

	/**
	 * Facebook Share
	 */
	// private Session.StatusCallback statusCallback = new
	// SessionStatusCallback();

	// WebDialog builder;
	private boolean is_opened = false;
	private ProgressDialog progressFacebook;

	// Facebook sharing
	private CallbackManager callbackManager;
	private LoginManager loginManager;
	ShareDialog shareDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postreview);
		FacebookSdk.sdkInitialize(getApplicationContext());
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		initComponents();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		if (android.os.Build.VERSION.SDK_INT > VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy policys = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policys);
		}
		createTwitterAccess();
		// Session session = Session.getActiveSession();
		// if (session == null) {
		// createFBSession(savedInstanceState);
		// } else {
		// session = null;
		// createFBSession(savedInstanceState);
		// }
	}

	private void initComponents() {

		mReviewText = (TextView) findViewById(R.id.review_text);
		mReviewText.setMovementMethod(new ScrollingMovementMethod());
		mClose = (TextView) findViewById(R.id.close);
		mClose.setTypeface(helvetica_bold);
		mPost = (TextView) findViewById(R.id.post);
		mPost.setTypeface(helvetica_bold);
		mRatingBar = (RatingBar) findViewById(R.id.review_ratingbar);
		twitter_share = (ToggleButton) findViewById(R.id.twitter_share);
		facebook_share = (ToggleButton) findViewById(R.id.fb_share);

		mRatingBar.setIsIndicator(false);
		mClose.setOnClickListener(this);
		mPost.setOnClickListener(this);

		mUserID = GlobalMethods.getUserID(this);
		mBundle = getIntent().getExtras();

		if (mBundle != null) {
			mReviewType = mBundle.getString("ReviewType");
			mComments = mBundle.getString("ReviewText");
			mReviewText.setText(mComments);
			mPropertyId = mBundle.getString("PropertyId");
			mPropertyReviewId = mBundle.getString("PropertyReviewId");
			mPropertyCommentId = mBundle.getString("PropertyCommentId");
			mRating = mBundle.getString("Rating");
			float rating = Float.parseFloat(mRating);
			property_type = mBundle.getString("PropType");
			mRatingBar.setRating(rating);

		}
		mRatingBar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {

						mRating = (Float.toString(mRatingBar.getRating()));

						System.out.println(mRating + "rATING"
								+ ratingBar.getRating() + "float" + rating);

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.post:
			if (mRating.equals("0.0")) {
				mCallBack = "Alert";
				DialogManager.showCustomAlertDialog(this, this,
						"Please select Rating");
			} else {
				if (mReviewType.equalsIgnoreCase("Edit")) {
					postEditReview();
				} else if (mReviewType.equalsIgnoreCase("Update")) {
					postUpdateReview();
				} else if (mReviewType.equalsIgnoreCase("Post")) {
					postAddReview();
				}
			}

			break;

		case R.id.close:

			Intent newIntent = new Intent(PostReviewActivty.this,
					WriteReviewActivity.class);
			newIntent.putExtra("ReviewType", mReviewType);
			newIntent.putExtra("Comments", mComments);
			newIntent.putExtra("PropertyId", mPropertyId);
			newIntent.putExtra("PropertyReviewId", mPropertyReviewId);
			newIntent.putExtra("PropertyCommentId", mPropertyCommentId);
			newIntent.putExtra("Rating", mRating);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			startActivity(newIntent);
			break;

		case R.id.twitter_share:
			twitter_share.setBackgroundResource(R.drawable.twit_share_enable);
			if (!isLogin) {
				new LoginTwitter().execute("");
			} else {
				new updateTwitterStatus().execute(mReviewText.getText()
						.toString());
			}
			break;
		case R.id.fb_share:
			facebook_share.setBackgroundResource(R.drawable.fb_share_enable);
			// setFBPermissions();
			sharewithFB();
			break;
		}
	}

	private void sharewithFB() {
		callbackManager = CallbackManager.Factory.create();

		shareDialog = new ShareDialog(this);
		if (ShareDialog.canShow(ShareLinkContent.class)) {
			ShareLinkContent linkContent = new ShareLinkContent.Builder()
					.setContentTitle("Renter's Block")
					.setContentDescription(mReviewText.getText().toString())
					.setContentUrl(
							Uri.parse("https://play.google.com/store/apps/details?id=com.smaat.renterblock"))
					.setImageUrl(
							Uri.parse("http://release.smaatapps.com/RB/rb_logo.png"))
					.build();

			shareDialog.show(linkContent);
		}

	}

	@Override
	protected void onResume() {
		// Session.getActiveSession().removeCallback(statusCallback);
		super.onResume();
	}

	private void postAddReview() {
		String Url = AppConstants.BASE_URL + "reviewproperty";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("review_user_id", mUserID);
		params.put("comments", mComments);
		params.put("rating", mRating);
		params.put("property_id", mPropertyId);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse response = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									if (response.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mCallBack = "Post";
										DialogManager.showCustomAlertDialog(
												PostReviewActivty.this,
												PostReviewActivty.this,
												response.getResult());

									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	private void postEditReview() {
		String Url;
		if (mPropertyCommentId.equals("")) {
			Url = AppConstants.BASE_URL + "reviewproperty";
		} else {
			Url = AppConstants.BASE_URL + "reviewproperty/edit";
		}
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("review_user_id", mUserID);
		params.put("property_review_id", mPropertyReviewId);
		params.put("property_review_comment_id", mPropertyCommentId);
		params.put("comments", mComments);
		params.put("rating", mRating);
		params.put("property_id", mPropertyId);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse response = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									if (response.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mCallBack = "Post";
										DialogManager.showCustomAlertDialog(
												PostReviewActivty.this,
												PostReviewActivty.this,
												response.getResult());

									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	private void postUpdateReview() {
		String Url = AppConstants.BASE_URL + "reviewproperty/update";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("review_user_id", mUserID);
		params.put("property_review_id", mPropertyReviewId);
		params.put("comments", mComments);
		params.put("rating", mRating);
		params.put("property_id", mPropertyId);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse response = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									if (response.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mCallBack = "Post";
										DialogManager.showCustomAlertDialog(
												PostReviewActivty.this,
												PostReviewActivty.this,
												response.getResult());

									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		if (AppConstants.fromPropert) {
			// launchActivity(PropertyDetailsActivity.class);
			Intent inte = new Intent(PostReviewActivty.this,
					PropertyDetailsActivity.class);
			inte.putExtra("PropertyId", mPropertyId);
			inte.putExtra("PropType", property_type);
			AppConstants.fromPropert = false;
			startActivity(inte);
		} else if (mCallBack.equalsIgnoreCase("Post")) {
			launchActivity(MyReviewActivity.class);
		} else if (mCallBack.endsWith("Alert")) {
			/**
			 * Close Popup
			 */
		} else {
			launchActivity(MyReviewActivity.class);
		}
	}

	private void createTwitterAccess() {
		if (TWITTER_CONSUMER_KEY.trim().length() == 0
				|| TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			DialogManager
					.showCustomAlertDialog(this, this,
							"Twitter oAuth tokens,Please set your twitter oauth tokens first!");
			return;
		}
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref", 0);
		if (isTwitterLoggedInAlready()) {
			isLogin = true;
		}
		if (!isTwitterLoggedInAlready()) {

			Uri uri = getIntent().getData();
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
				// oAuth verifier
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					Editor e = mSharedPreferences.edit();

					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
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

	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {

			DialogManager.showToast(PostReviewActivty.this,
					"There was some error in authenticating with Twitter.");
		}

		public void onComplete(String value) {

			Uri uri = Uri.parse(value);
			if (uri != null && uri.toString().startsWith(CALLBACK_URL)) {
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
				try {
					AccessToken accessToken = twitter.getOAuthAccessToken(
							requestToken, verifier);

					// Shared Preferences
					Editor e = mSharedPreferences.edit();

					e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
					e.putString(PREF_KEY_OAUTH_SECRET,
							accessToken.getTokenSecret());
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
			progress = new ProgressDialog(PostReviewActivty.this);
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

							new TwitterDialog(PostReviewActivty.this,
									requestToken.getAuthenticationURL(),
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
				DialogManager.showCustomAlertDialog(PostReviewActivty.this,
						PostReviewActivty.this, "No internet Connection.");
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
			progress = new ProgressDialog(PostReviewActivty.this);
			progress.setMessage("Updating to twitter...");
			progress.setIndeterminate(false);
			progress.setCancelable(false);
			progress.show();
		}

		protected String doInBackground(String... args) {
			Log.d("Tweet Text", "> " + args[0]);
			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);
				StatusUpdate data = new StatusUpdate(status.trim());
				Bitmap bm = Bitmap.createBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.sharinglogo));
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.PNG, 0, bos);
				byte[] bitmapdata = bos.toByteArray();
				ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
				data.setMedia("sharinglogo.png", bs);
				twitter.updateStatus(data);
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
						Toast.makeText(
								getApplicationContext(),
								"Tweet Failed because your Tweet Limit Exceeded",
								Toast.LENGTH_SHORT).show();
					} else if (file_url != null
							&& file_url.equalsIgnoreCase("success")) {
						Toast.makeText(getApplicationContext(),
								"Status tweeted successfully",
								Toast.LENGTH_SHORT).show();
					} else if (file_url != null
							&& file_url.contains("duplicate")) {
						Toast.makeText(getApplicationContext(),
								"Tweet Failed because of duplicate message.",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Failed to Tweet", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

	}

	private void logoutFromTwitter() {
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.remove(PREF_KEY_TWITTER_LOGIN);
		e.commit();
	}

	// private void createFBSession(Bundle savedInstanceState) {
	// Session session = Session.getActiveSession();
	// if (session == null) {
	// if (savedInstanceState != null) {
	// session = Session.restoreSession(PostReviewActivty.this, null,
	// statusCallback, savedInstanceState);
	// }
	// if (session == null) {
	// session = new Session(this);
	// }
	// Session.setActiveSession(session);
	// if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	// session.openForRead(new Session.OpenRequest(this)
	// .setCallback(statusCallback));
	// }
	// }
	// }

	// private void setFBPermissions() {
	//
	// Session session = Session.getActiveSession();
	//
	// if (!session.isOpened() && !session.isClosed()) {
	// OpenRequest op = new Session.OpenRequest((Activity) this);
	// op.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
	// op.setCallback(statusCallback);
	//
	// List<String> permissions = new ArrayList<String>();
	// permissions.add("email");
	// permissions.add("user_birthday");
	// permissions.add("user_location");
	//
	// op.setPermissions(permissions);
	//
	// Session.setActiveSession(session);
	// session.openForRead(op);
	// } else {
	// Log.d("INSIDE ", "B");
	// Session.openActiveSession(this, true, statusCallback);
	// }
	// }

	// private class SessionStatusCallback implements Session.StatusCallback {
	// @Override
	// public void call(Session session, SessionState state,
	// Exception exception) {
	// getUserDetailFromFB();
	// }
	// }

	// public void getUserDetailFromFB() {
	// Session session = Session.getActiveSession();
	// if (session.isOpened()) {
	// if (progress != null) {
	// progress.setCancelable(false);
	// progress.setMessage("Facebook");
	// progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// progress.show();
	// }
	//
	// Request.newMeRequest(session, new Request.GraphUserCallback() {
	//
	// @Override
	// public void onCompleted(final GraphUser user,
	// final Response response) {
	//
	// if (user != null) {
	//
	// publishFeedDialog();
	// } else {
	// progress.dismiss();
	// }
	// }
	// }).executeAsync();
	// }
	// }

	// private void publishFeedDialog() {
	//
	// if (!is_opened) {
	// Bundle params = new Bundle();
	// params.putString("description", mReviewText.getText().toString());
	// params.putString("picture",
	// "http://release.smaatapps.com/RB/rb_logo.png");
	//
	// builder = (new WebDialog.FeedDialogBuilder(this,
	// Session.getActiveSession(), params)).setOnCompleteListener(
	// new OnCompleteListener() {
	//
	// @Override
	// public void onComplete(Bundle values,
	// FacebookException error) {
	// if (error != null) {
	// Session.getActiveSession().removeCallback(
	// statusCallback);
	//
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// }
	// if (error == null) {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// final String postId = values
	// .getString("post_id");
	// if (postId != null) {
	// } else {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// Session.getActiveSession().removeCallback(
	// statusCallback);
	// }
	// } else if (error instanceof FacebookOperationCanceledException) {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// Session.getActiveSession().removeCallback(
	// statusCallback);
	// } else {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// Toast.makeText(PostReviewActivty.this,
	// "Error posting story",
	// Toast.LENGTH_SHORT).show();
	//
	// }
	// builder.dismiss();
	// is_opened = false;
	// }
	// }).build();
	//
	// builder.show();
	// is_opened = true;
	// }
	//
	// }

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
