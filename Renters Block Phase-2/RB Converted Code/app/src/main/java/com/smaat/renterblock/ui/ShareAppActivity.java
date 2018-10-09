package com.smaat.renterblock.ui;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.smaat.renterblock.R;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TwitterDialog;
import com.smaat.renterblock.util.TypefaceSingleton;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class ShareAppActivity extends BaseActivity implements OnClickListener, DialogMangerCallback {
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;

	/**
	 * Share App Declaration
	 */
	private TextView mHeaderTxt;
	private Typeface mTypefaceBold;

	/**
	 * Twitter Share
	 */
	private static SharedPreferences mSharedPreferences;
	private ProgressDialog progress;
	private ProgressDialog progressFacebook;
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
	ShareDialog shareDialog;

	/**
	 * Facebook Share
	 */
	// private Session.StatusCallback statusCallback = new
	// SessionStatusCallback();
	Bundle extras;
	private Button share_slide_menu_icon;
	// private Session session;
	// Facebook facebook;
	// AsyncFacebookRunner mAsyncRunner;
	static Context mContext;
	private Handler mRunOnUi = new Handler();
	// WebDialog builder;
	private boolean is_opened = false;

	// Facebook sharing
	private CallbackManager callbackManager;
	private LoginManager loginManager;
	private String call_from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share_app);
		FacebookSdk.sdkInitialize(getApplicationContext());
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		setFont(root, mTypeface);
		setupUI(root);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		mContext = this;
		initComponents();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);


		if (android.os.Build.VERSION.SDK_INT > VERSION_CODES.GINGERBREAD) {
			StrictMode.ThreadPolicy policys = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policys);
		}
		createTwitterAccess();
	}

	private void initComponents() {
		/**
		 * ShareApp Initialization
		 */
		mHeaderTxt = (TextView) findViewById(R.id.header_txt);
		mHeaderTxt.setTypeface(mTypefaceBold);
		/**
		 * Slide Menu Intialization
		 */
		UserID = GlobalMethods.getUserID(this);

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);

		selectedType = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER) || selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_share_app);
			AppConstants.view_id = R.id.buy_share_app;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_share_app);
			AppConstants.view_id = R.id.sell_share_app;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_share_app);
			AppConstants.view_id = R.id.agent_share_app;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_share_app);
			AppConstants.view_id = R.id.buy_share_app;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

		share_slide_menu_icon = (Button) findViewById(R.id.share_slide_menu_icon);

		extras = getIntent().getExtras();
		if (extras != null) {
			call_from = extras.getString("call_from");
			mHeaderTxt.setText("Share this Property");
			share_slide_menu_icon.setBackgroundResource(R.drawable.back_arrow_white);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.menu_icon:
				if (extras != null) {
					// if (session == null) {
					// Session session1 = Session.getActiveSession();
					// } else {
					// Session.getActiveSession().addCallback(statusCallback);
					// }
					finish();
					overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				} else {
					slide_holder.toggle();
				}
				break;
			case R.id.msg_icon:
				Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
				smsIntent.setType("vnd.android-dir/mms-sms");
				smsIntent.setData(Uri.parse("sms:" + ""));
				String shareTextss = "";
				if (extras != null && call_from.equalsIgnoreCase("prop_detail")) {

					smsIntent.putExtra("sms_body","http://rentersblock.com/index.php/property/share/"+AppConstants.Prop_det_share_text);
				} else {
					shareTextss = AppConstants.Prop_det_share_text;
					smsIntent.putExtra("sms_body",
							"Your future lifestyle awaits, check out the Renter's Block app to find your next home."
									+ "http://rentersblock.com/index.php/property/share/"+shareTextss);
				}
				startActivity(smsIntent);
				break;
			case R.id.email_icon:
				String share_address = "";
				share_address = AppConstants.prop_det_share_address;
				Intent mailintent = new Intent(Intent.ACTION_SENDTO);
				mailintent.setType("text/plain");
				mailintent.putExtra(Intent.EXTRA_SUBJECT, share_address);
				String shareTexts = "";
				if (extras != null && call_from.equalsIgnoreCase("prop_detail")) {

					mailintent.putExtra(Intent.EXTRA_TEXT, "http://rentersblock.com/index.php/property/share/"+AppConstants.Prop_det_share_text);
				} else {
					shareTexts = AppConstants.Prop_det_share_text;
					mailintent.putExtra(Intent.EXTRA_TEXT,
							"Your future lifestyle awaits, check out the Renter's Block app to find your next home."
									+ "http://rentersblock.com/index.php/property/share/"+shareTexts);
				}
				mailintent.setData(Uri.parse("mailto:" + ""));
				mailintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mailintent);
				break;
			case R.id.twitter_icon:
				if (!isLogin) {
					new LoginTwitter().execute("");
				} else {
					String share_addresss = "";
					String shareText = "";
					if (extras != null && call_from.equalsIgnoreCase("prop_detail")) {
						share_addresss = AppConstants.prop_det_share_address;
						shareText = AppConstants.Prop_det_share_text;
					} else {
						shareText = share_addresss
								+ " http://rentersblock.com/index.php/property/share/"+shareText;
					}
					new updateTwitterStatus().execute(shareText);
				}
				break;
			case R.id.facebook_icon:
				sharewithFB();
				break;
		}

	}

	private void sharewithFB() {
		callbackManager = CallbackManager.Factory.create();

		shareDialog = new ShareDialog(this);
		if (ShareDialog.canShow(ShareLinkContent.class)) {
			String shareText = "";
			String share_address="";
			if (extras != null && call_from.equalsIgnoreCase("prop_detail")) {
				shareText = AppConstants.Prop_det_share_text;
				share_address = AppConstants.prop_det_share_address;
			} else {
				shareText = "Renter's Block App \n Your future lifestyle awaits, check out the Renter's Block app to find your next home."
						+ " Download now goo.gl/DGDC1v";
			}
			ShareLinkContent linkContent = new ShareLinkContent.Builder().setContentTitle(shareText)
					.setContentDescription(
							share_address)
//					.setContentUrl(Uri.parse("goo.gl/DGDC1v"))

					.setContentTitle(share_address)
//					.setContentDescription(
//							"The 'Hello Facebook' sample  showcases simple Facebook integration")
					.setContentUrl(Uri.parse("http://rentersblock.com/web_dev/index.php/property/share/"+shareText))
					.build();
//.setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//					.setImageUrl(Uri.parse(AppConstants.PROPERTY_IMG_URL.isEmpty()?
//							"http://release.smaatapps.com/RB/rb_logo.png":AppConstants.PROPERTY_IMG_URL)).build();
//	.setImageUrl(Uri.parse(AppConstants.PROPERTY_IMG_URL)).build();

//			ShareLinkContent linkContent = new ShareLinkContent.Builder()
//					.setContentTitle("Game Result Highscore")
//					.setContentDescription("Your future lifestyle awaits, check out the Renter's Block app to find your next home."
//									+ " Hello World")
//					.setContentUrl(Uri.parse("goo.gl/DGDC1v"))
//					//.setQuote("Connect on a global scale.")
//					.setShareHashtag(new ShareHashtag.Builder()
//							.setHashtag("#ConnectTheWorld")
//							.build())
//					//.setImageUrl(Uri.parse("android.resource://de.ginkoboy.flashcards/" + R.drawable.logo_flashcards_pro))
//					.setImageUrl(Uri.parse(AppConstants.PROPERTY_IMG_URL))
//					.build();
//
//
//			SharePhoto photo1 = new SharePhoto.Builder()
//					.setImageUrl(Uri.parse(AppConstants.PROPERTY_IMG_URL.isEmpty()?
//						"http://release.smaatapps.com/RB/rb_logo.png":AppConstants.PROPERTY_IMG_URL))
//					.build();
//
//			ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
//					.putString("og:type", "healthsynergy.photo")
//					.putString("og:title", shareText)
////					.putString("og:description", "This is a wonderful food.")
//					.putPhoto("og:image",photo1)
//					.build();
//
//
//			ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
//					.setActionType("healthsynergy.publish_actions")
//					.putObject("photo", object)
//					.build();
//			ShareOpenGraphContent content1 = new ShareOpenGraphContent.Builder()
//					.setPreviewPropertyName("photo")
//					.setAction(action)
//					.build();


//			shareDialog.show(content1);



			shareDialog.show(linkContent);
		}

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

	public interface TwDialogListener {
		public void onComplete(String value);

		public void onError(String value);
	}

	private TwDialogListener mTwLoginDialogListener = new TwDialogListener() {

		public void onError(String value) {

			DialogManager.showToast(ShareAppActivity.this, "There was some error in authenticating with Twitter.");
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
			progress = new ProgressDialog(ShareAppActivity.this);
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

							new TwitterDialog(ShareAppActivity.this, requestToken.getAuthenticationURL(),
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
				DialogManager.showCustomAlertDialog(ShareAppActivity.this, ShareAppActivity.this,
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
			progress = new ProgressDialog(ShareAppActivity.this);
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

				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
				StatusUpdate data = new StatusUpdate(status.trim());
				Bitmap bm = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sharinglogo));
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

	// private void callFB() {
	//
	// session = new Session(this);
	// Session.setActiveSession(session);
	// if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
	// session.openForRead(new Session.OpenRequest(this)
	// .setCallback(statusCallback));
	// }
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
	//
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
	// if (progressFacebook != null) {
	// progressFacebook = new ProgressDialog(ShareAppActivity.this);
	// progressFacebook.setCancelable(false);
	// progressFacebook.setMessage("Please Wait");
	// progressFacebook.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	// progressFacebook.show();
	// }
	//
	// Request.newMeRequest(session, new Request.GraphUserCallback() {
	//
	// @Override
	// public void onCompleted(final GraphUser user,
	// final Response response) {
	//
	// if (user != null) {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// try {
	// publishFeedDialog();
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// } else {
	// if (progressFacebook != null) {
	// progressFacebook.dismiss();
	// }
	// DialogManager.showCustomAlertDialog(
	// ShareAppActivity.this, ShareAppActivity.this,
	// response.getError().toString());
	// }
	// }
	// }).executeAsync();
	// }
	// }

	// private void publishFeedDialog() throws FileNotFoundException {
	// if (!is_opened) {
	// Bundle params = new Bundle();
	// params.putString("link",
	// "https://play.google.com/store/apps/details?id=com.smaat.renterblock&hl=en");
	// params.putString("description",
	// "https://play.google.com/store/apps/details?id=com.smaat.renterblock&hl=en");
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
	// Toast.makeText(ShareAppActivity.this,
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
		// if (session == null) {
		// Session session1 = Session.getActiveSession();
		// } else {
		// Session.getActiveSession().addCallback(statusCallback);
		// }
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
		// if (session != null) {
		// Session session = Session.getActiveSession();
		// session.close();
		// }
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

	public static void callUpdateStatus() {
		((ShareAppActivity) mContext).updateStatus();
	}

	private void updateStatus() {
		new updateTwitterStatus()
				.execute("Your future lifestyle awaits, check out the Renter's Block app to find your next home."
						+ " Download now goo.gl/DGDC1v");
	}
}
