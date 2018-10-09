package com.smaat.renterblock.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.alerts.AlertsActivity;
import com.smaat.renterblock.findagent.FindAgentActivity;
import com.smaat.renterblock.friends.ui.FriendsMainScreen;
import com.smaat.renterblock.hotleads.ui.HotLeadsMainScreen;
import com.smaat.renterblock.model.BoardsResponse;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.model.FavouriteReponse;
import com.smaat.renterblock.model.NotificationResponse;
import com.smaat.renterblock.model.PropertyPostedUserDetails;
import com.smaat.renterblock.model.PropertyResponse;
import com.smaat.renterblock.myfavourite.MyFavouriteActivity;
import com.smaat.renterblock.savedsearch.SavedSearchActivity;
import com.smaat.renterblock.scheduling.SchedulingActivity;
import com.smaat.renterblock.settings.SettingsActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.OptionDialogInterfaceListener;
import com.smaat.renterblock.util.TypefaceSingleton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.HashMap;

public class BaseActivity extends FragmentActivity implements
		DialogMangerCallback {

	private static AlertDialog alertDialog;
	private AQuery aq;
	protected static Activity _activity;
	public static Dialog mDialog;
	// private QBUser currentUser;

	public static final int FIRST_USER_ID = 65421;
	public static final String FIRST_USER_LOGIN = "videoChatUser1";
	public static final String FIRST_USER_PASSWORD = "videoChatUser1pass";
	//
	public static final int SECOND_USER_ID = 65422;
	public static final String SECOND_USER_LOGIN = "videoChatUser2";
	public static final String SECOND_USER_PASSWORD = "videoChatUser2pass";
	public static ImageView mNotification_bell;

	private RelativeLayout agent_exclusives, agent_listings, agent_hot_lead,
			agent_reviews, agent_friends, agent_scheduling, agent_profile,
			agent_homes_for_rent, agent_homes_for_sale, agent_saved_searches,
			agent_my_favourites, agent_alerts, agent_open_house,
			agent_find_agent, agent_feedback, agent_rate_app, agent_share_app,
			agent_settings;

	private RelativeLayout buy_exclusives, buy_homes_for_rent,
			buy_homes_for_sale, buy_saved_searches, buy_my_favourites,
			buy_alerts, buy_open_house, buy_find_agent, buy_reviews,
			buy_friends, buy_scheduling, buy_profile, buy_feedback,
			buy_rate_app, buy_share_app, buy_settings;

	private RelativeLayout sell_exclusives, sell_properties, sell_hot_lead,
			sell_reviews, sell_friends, sell_scheduling, sell_profile,
			sell_homes_for_rent, sell_homes_for_sale, sell_saved_searches,
			sell_my_favourites, sell_alerts, sell_open_house, sell_find_agent,
			sell_feedback, sell_rate_app, sell_share_app, sell_settings;
	
	private LinearLayout main_lay;
	
	protected String UserID, UserName, Friend_UserID = "", mEnhancedProfile,
			mUser_Type;
	protected TextView mUserName;
	private Dialog imageDialog;
	private Intent intent = null;
	private String mSelectedType;
	private String mAlert = "Alert";
	private String enhance_Profile;
	public static Typeface helvetica_normal, helvetica_bold, helvetica_light;
	private ImageView dia_imageview;
	private EasyTracker easyTracker = null;
	private static GoogleAnalytics mGa = null;
	private static final int GA_DISPATCH_PERIOD = 30;
	private static final boolean GA_IS_DRY_RUN = false;
	private static final LogLevel GA_LOG_VERBOSITY = LogLevel.VERBOSE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		FacebookSdk.sdkInitialize(getApplicationContext());
		super.onCreate(savedInstanceState);
		_activity = this;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		UserID = GlobalMethods.getUserID(_activity);

		mEnhancedProfile = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.ENHANCED_PROFILE);

		mUser_Type = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);

		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(this);
		getNotification();
		// setGoogleAnalytics(_activity);
	}

	public void setGoogleAnalytics(Context context) {
		easyTracker = EasyTracker.getInstance(context);
		mGa = GoogleAnalytics.getInstance(this);

		GAServiceManager.getInstance().setLocalDispatchPeriod(
				GA_DISPATCH_PERIOD);
		mGa.setDryRun(GA_IS_DRY_RUN);
		mGa.getLogger().setLogLevel(GA_LOG_VERBOSITY);

	}

	private void getNotification() {

		String Url = AppConstants.BASE_URL + "notification";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							NotificationResponse mResponse = new Gson()
									.fromJson(json.toString(),
											NotificationResponse.class);

							if (mResponse.getError_code().equalsIgnoreCase(
									AppConstants.SUCCESS_CODE)) {
								try {
									if (mResponse.getResult().size() != 0) {
										mNotification_bell
												.setImageResource(R.drawable.notification_blue_icon);
									} else {
										mNotification_bell
												.setImageResource(R.drawable.notification_icon);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						} else {
							// statusErrorCode(status);
						}
					}

				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		_activity = this;

		easyTracker = EasyTracker.getInstance(_activity);
	}

	public void copy(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public static void copyFileOrDirectory(String srcDir, String dstDir) {

		try {
			File src = new File(srcDir);
			File dst = new File(dstDir, src.getName());

			if (src.isDirectory()) {

				String files[] = src.list();
				int filesLength = files.length;
				for (int i = 0; i < filesLength; i++) {
					String src1 = (new File(src, files[i]).getPath());
					String dst1 = dst.getPath();
					copyFileOrDirectory(src1, dst1);

				}
			} else {
				copyFile(src, dst);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void copyFile(File sourceFile, File destFile)
			throws IOException {
		if (!destFile.getParentFile().exists())
			destFile.getParentFile().mkdirs();

		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void showAlert(final Activity activity) {
		alertDialog = new AlertDialog.Builder(activity).create();
		alertDialog.setTitle("No Internet Connection");
		alertDialog.setMessage("You don't have internet connection.");
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();

	}

	public void setupUI(View view) {

		if (!(view instanceof EditText)) {

			view.setOnTouchListener(new OnTouchListener() {

				public boolean onTouch(View v, MotionEvent event) {
					hideSoftKeyboard(BaseActivity.this);
					return false;
				}

			});
		}
		if (view instanceof ViewGroup) {

			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

				View innerView = ((ViewGroup) view).getChildAt(i);

				setupUI(innerView);
			}
		}
	}

	public void hideSoftKeyboard(Activity activity) {
		try {
			if (activity != null && !activity.isFinishing()) {
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);

				if (activity.getCurrentFocus() != null
						&& activity.getCurrentFocus().getWindowToken() != null) {
					inputMethodManager.hideSoftInputFromWindow(activity
							.getCurrentFocus().getWindowToken(), 0);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void hideSoftKeyboardDialog(Activity activity) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(getWindow()
					.getDecorView().getWindowToken(), 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ImageViewer(String url) {
		LayoutInflater inflater = (LayoutInflater) _activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.chat_imageview, null);
		imageDialog = new Dialog(_activity,
				android.R.style.Theme_Translucent_NoTitleBar);
		imageDialog.setContentView(layout);

		ImageView closeBtn = (ImageView) imageDialog
				.findViewById(R.id.closeBtn);
		dia_imageview = (ImageView) imageDialog.findViewById(R.id.imageView1);
		Bitmap myBitmap = BitmapFactory.decodeFile(url);
		if (myBitmap != null) {
			dia_imageview.setImageBitmap(myBitmap);
		} else {
			new downloadImage().execute(url);
		}

		closeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageDialog.dismiss();
			}
		});
		imageDialog.show();

	}

	class downloadImage extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			try {
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap myBitmap = BitmapFactory.decodeStream(input);
				return myBitmap;
			} catch (IOException e) {
				// Log exception
				return null;
			}
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dia_imageview.setImageBitmap(result);
		}
	}

	public void statusErrorCode(AjaxStatus status) {
		if (status.getCode() == 500) {
			DialogManager.showCustomAlertDialog(_activity, BaseActivity.this,
					"Server is busy or down. Try again!");
		} else if (status.getCode() == 404) {
			DialogManager.showCustomAlertDialog(_activity, BaseActivity.this,
					"Resource not found!");
		} else {
			DialogManager
					.showCustomAlertDialog(_activity, BaseActivity.this,
							"No Internet Connection" + "\n"
									+ "Please try again later.");
		}
	}

	public AQuery aq() {
		if (aq == null) {
			aq = new AQuery(_activity);
		}
		return aq;
	}

	public String connectToWebService(String url) {

		String result = "";
		try {
			url = url.replaceAll(" ", "%20");
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpGet httpget = new HttpGet(url);
			httpget.addHeader("Accept", "application/json");
			HttpResponse response;

			try {
				response = httpclient.execute(httpget);
				if (response != null) {
					HttpEntity entity = response.getEntity();

					if (entity != null) {
						InputStream instream = entity.getContent();
						result = convertStreamToString(instream);
						instream.close();
					}
				} else {
					return null;
				}

			} catch (Exception exception) {

				exception.printStackTrace();

				return null;
			} finally {

				httpclient.getConnectionManager().shutdown();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		StringBuilder sb = new StringBuilder();

		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	public String getStringValueFromJsonObject(JSONObject json_object,
			String key)

	{
		try {
			if (json_object.getString(key) == null)

			{
				return null;
			} else {
				return json_object.getString(key);
			}

		} catch (JSONException jSONException)

		{

			return null;

		}

	}

	public void setFont(ViewGroup group, Typeface font) {
		int count = group.getChildCount();
		View v;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button /* etc. */)
				((TextView) v).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont((ViewGroup) v, font);
		}
	}

	public void launchActivity(Class<?> clazz) {
		Intent intent = new Intent(_activity, clazz);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TASK
				| Intent.FLAG_ACTIVITY_NO_HISTORY);
		_activity.startActivity(intent);

		_activity.overridePendingTransition(R.anim.slide_in_right,
				R.anim.slide_out_left);
		_activity.finish();
	}

	@Override
	protected void onPause() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (alertDialog != null && alertDialog.isShowing()) {
			alertDialog.dismiss();
		}
		super.onDestroy();
	}

	public void updateTimeValue(String string, Calendar calendar_fragment,
			int id) {

	}

	public void updateDateTimeValue(String string, Calendar calendar_fragment,
			int id) {

	}

	protected float[] calculateData(float[] data, String[] color) {
		float total = 0;
		for (int i = 0; i < data.length; i++) {
			total += data[i];
		}
		for (int i = 0; i < data.length; i++) {
			data[i] = 360 * (data[i] / total);
		}
		return data;
	}

	public class MyGraphview extends View {
		private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		private float[] value_degree;
		private String[] color_matches;
		RectF rectf = new RectF(120, 40, 380, 300);
		float temp = 0;

		public MyGraphview(Context context, float[] values, String[] color) {
			super(context);
			value_degree = new float[values.length];
			for (int i = 0; i < values.length; i++) {
				value_degree[i] = values[i];

			}
			System.out.println(value_degree);
			color_matches = new String[color.length];
			for (int color_id = 0; color_id < color.length; color_id++) {
				color_matches[color_id] = color[color_id];
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			for (int i = 0; i < value_degree.length; i++) {
				int color = 0;
				if (i == 0) {
					try {
						color = Color.parseColor(color_matches[i]);
					} catch (Exception e) {
						color = Color.GRAY;
					}
					paint.setColor(color);
					canvas.drawArc(rectf, temp, value_degree[i], true, paint);
				} else {
					temp += value_degree[i - 1];
					if (i == 1) {
						try {
							color = Color.parseColor(color_matches[i]);
						} catch (Exception e) {
							color = Color.GRAY;
						}
					} else {
						try {
							color = Color.parseColor(color_matches[i]);
						} catch (Exception e) {
							color = Color.GRAY;
						}
					}
					paint.setColor(color);
					canvas.drawArc(rectf, temp, value_degree[i], true, paint);
				}
			}
		}
	}

	public void onRequestSuccess(Object responseObj) {

	}

	public void onRequestFailure(String errorCode) {
		if (errorCode == null) {
			DialogManager.showCustomAlertDialog(_activity, BaseActivity.this,
					getString(R.string.data_issue));
		} else if (errorCode.equals("-101")) {
			DialogManager.showCustomAlertDialog(_activity, BaseActivity.this,
					getString(R.string.no_network));
		} else {
			DialogManager.showCustomAlertDialog(_activity, BaseActivity.this,
					getString(R.string.server_unreachable));
		}
	}

	public static class GsonTransformer implements Transformer {

		public <T> T transform(String url, Class<T> type, String encoding,
				byte[] data, AjaxStatus status) {
			Gson g = new Gson();
			return g.fromJson(new String(data), type);
		}
	}

	public void onRequestSuccess(FavouriteReponse responseObj) {

	}

	public void onRequestSuccess(PropertyResponse responseObj) {

	}

	public void onRequestSuccess(BoardsResponse responseObj) {

	}

	public void onReqtSuccess(PropertyPostedUserDetails obj) {

	}

	public void onReqFailure(Object object) {

	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	public void trackButtons(String mEventCategory, String mEventAction) {

		easyTracker.send(MapBuilder.createEvent(mEventCategory, mEventAction,
				mEventCategory, null).build());

	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			// if (SplashScreen.mTargetClass != null) {
			// launchActivity(SplashScreen.mTargetClass);
			// } else {
			launchActivity(MapFragmentActivity.class);
			// }
			mAlert = "Alert";
		} else {
			/**
			 * Close Dialog
			 */
		}

	}

	public void onMenuBuyClick(View v) {
		switch (v.getId()) {
		case R.id.buy_exclusives:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			AppConstants.buy_home_type = "exclusive";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Exclusive");
			startActivity(intent);
			trackButtons(
					getString(R.string.com_smaat_renterblock_MapFragmentActivity),
					"");
			// finish();
			break;
		case R.id.buy_homes_for_rent:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			AppConstants.buy_home_type = "rent";
			AppConstants.agent_home_type = "rent";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Rent");
			startActivity(intent);
			// finish();
			break;
		case R.id.buy_homes_for_sale:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			AppConstants.buy_home_type = "sale";
			AppConstants.agent_home_type = "sale";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Sale");
			startActivity(intent);
			// finish();
			break;
		case R.id.buy_saved_searches:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = SavedSearchActivity.class;
			intent = new Intent(_activity, SavedSearchActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_my_favourites:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = MyFavouriteActivity.class;
			intent = new Intent(_activity, MyFavouriteActivity.class);
			AppConstants.CALL_MAP = "true";
			startActivity(intent);
			finish();
			break;
		case R.id.buy_alerts:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = AlertsActivity.class;
			intent = new Intent(_activity, AlertsActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_open_house:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			AppConstants.buy_home_type = "openhouse";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Rent");
			startActivity(intent);
			finish();
			break;
		case R.id.buy_find_agent:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = FindAgentActivity.class;
			intent = new Intent(_activity, FindAgentActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_reviews:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = MyReviewActivity.class;
			intent = new Intent(_activity, MyReviewActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_friends:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = FriendsMainScreen.class;
			intent = new Intent(_activity, FriendsMainScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_scheduling:
			enhance_Profile = (String) GlobalMethods.getValueFromPreference(
					this, GlobalMethods.STRING_PREFERENCE,
					AppConstants.ENHANCED_PROFILE);
			if (enhance_Profile.equals("1")) {
				AppConstants.view_id = v.getId();
				setBuyBackground(v.getId());
				SplashScreen.mTargetClass = SchedulingActivity.class;
				intent = new Intent(_activity, SchedulingActivity.class);
				startActivity(intent);
				finish();
			} else {
				DialogManager.showDialogAlert(_activity,
						getString(R.string.enhance_txt),
						getString(R.string.close), getString(R.string.enhanc),
						ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						SplashScreen.mTargetClass, this);
			}
			break;
		case R.id.buy_profile:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = ProfileScreen.class;
			intent = new Intent(_activity, ProfileScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_feedback:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = FeedbackActivity.class;
			intent = new Intent(_activity, FeedbackActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_rate_app:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			final String appPackageName = getPackageName(); 
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/details?id="
								+ appPackageName)));
			}
			setGoogleAnalytics(this);
			break;
		case R.id.buy_share_app:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = ShareAppActivity.class;
			intent = new Intent(_activity, ShareAppActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.buy_settings:
			AppConstants.view_id = v.getId();
			setBuyBackground(v.getId());
			SplashScreen.mTargetClass = SettingsActivity.class;
			intent = new Intent(_activity, SettingsActivity.class);
			startActivity(intent);
			finish();
			break;

		}
	}

	public void onMenuSellerclick(View v) {
		// setSellBackground(v.getId());
		switch (v.getId()) {
		case R.id.sell_exclusives:
			AppConstants.view_id = v.getId();
			setSellBackground(v.getId());
			AppConstants.sell_home_type = "exclusive";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Exclusive");
			startActivity(intent);
			// finish();
			break;
		case R.id.sell_properties:
			AppConstants.view_id = v.getId();
			setSellBackground(v.getId());
			SplashScreen.mTargetClass = ListingActivity.class;
			intent = new Intent(_activity, ListingActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_hot_lead:
			enhance_Profile = (String) GlobalMethods.getValueFromPreference(
					this, GlobalMethods.STRING_PREFERENCE,
					AppConstants.ENHANCED_PROFILE);
			if (enhance_Profile.equals("1")) {
				AppConstants.view_id = v.getId();
				setSellBackground(v.getId());
				SplashScreen.mTargetClass = HotLeadsMainScreen.class;
				intent = new Intent(_activity, HotLeadsMainScreen.class);
				startActivity(intent);
				finish();
			} else {
				DialogManager.showDialogAlert(_activity,
						getString(R.string.enhance_txt),
						getString(R.string.close), getString(R.string.enhanc),
						ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						SplashScreen.mTargetClass, this);
			}
			break;
		case R.id.sell_reviews:
			AppConstants.view_id = v.getId();
			setSellBackground(v.getId());
			SplashScreen.mTargetClass = MyReviewActivity.class;
			intent = new Intent(_activity, MyReviewActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_friends:
			AppConstants.view_id = v.getId();
			setSellBackground(v.getId());
			SplashScreen.mTargetClass = FriendsMainScreen.class;
			intent = new Intent(_activity, FriendsMainScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_scheduling:

			// AppConstants.view_id = v.getId();
			// SplashScreen.mTargetClass = SchedulingActivity.class;
			// intent = new Intent(_activity, SchedulingActivity.class);
			// startActivity(intent);
			// finish();

			if (mEnhancedProfile.equals("1")) {
				AppConstants.view_id = v.getId();
				setSellBackground(v.getId());
				SplashScreen.mTargetClass = SchedulingActivity.class;
				intent = new Intent(_activity, SchedulingActivity.class);
				startActivity(intent);
				finish();
			} else {
				DialogManager.showDialogAlert(_activity,
						getString(R.string.enhance_txt),
						getString(R.string.close), getString(R.string.enhanc),
						ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						SplashScreen.mTargetClass, this);
			}
			break;
		case R.id.sell_profile:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = ProfileScreen.class;
			intent = new Intent(_activity, ProfileScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_homes_for_rent:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			AppConstants.sell_home_type = "rent";
			AppConstants.agent_home_type = "rent";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Rent");
			startActivity(intent);
			// finish();
			break;
		case R.id.sell_homes_for_sale:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			AppConstants.sell_home_type = "sale";
			AppConstants.agent_home_type = "sale";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Sale");
			startActivity(intent);
			// finish();
			break;
		case R.id.sell_saved_searches:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = SavedSearchActivity.class;
			intent = new Intent(_activity, SavedSearchActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_my_favourites:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = MyFavouriteActivity.class;
			intent = new Intent(_activity, MyFavouriteActivity.class);
			AppConstants.CALL_MAP = "true";
			startActivity(intent);
			finish();
			break;
		case R.id.sell_alerts:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = AlertsActivity.class;
			intent = new Intent(_activity, AlertsActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_open_house:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			AppConstants.sell_home_type = "openhouse";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "OpenHouse");
			startActivity(intent);
			finish();
			break;
		case R.id.sell_find_agent:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = FindAgentActivity.class;
			intent = new Intent(_activity, FindAgentActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_feedback:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = FeedbackActivity.class;
			intent = new Intent(_activity, FeedbackActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_rate_app:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			final String appPackageName = getPackageName(); 
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/details?id="
								+ appPackageName)));
			}
			setGoogleAnalytics(this);
			break;
		case R.id.sell_share_app:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = ShareAppActivity.class;
			intent = new Intent(_activity, ShareAppActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.sell_settings:
			setSellBackground(v.getId());
			AppConstants.view_id = v.getId();
			SplashScreen.mTargetClass = SettingsActivity.class;
			intent = new Intent(_activity, SettingsActivity.class);
			startActivity(intent);
			finish();

			break;
		}
	}

	public void onMenuAgentClick(View v) {

		switch (v.getId()) {

		case R.id.agent_exclusives:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			AppConstants.agent_home_type = "exclusive";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Exclusive");
			startActivity(intent);
			trackButtons(
					getString(R.string.com_smaat_renterblock_MapFragmentActivity),
					"");
			// finish();
			break;
		case R.id.agent_listings:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = ListingActivity.class;
			intent = new Intent(_activity, ListingActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_hot_lead:
			enhance_Profile = (String) GlobalMethods.getValueFromPreference(
					this, GlobalMethods.STRING_PREFERENCE,
					AppConstants.ENHANCED_PROFILE);
			if (enhance_Profile.equals("1")) {
				AppConstants.view_id = v.getId();
				setAgentBackground(v.getId());
				SplashScreen.mTargetClass = HotLeadsMainScreen.class;
				intent = new Intent(_activity, HotLeadsMainScreen.class);
				startActivity(intent);
				finish();
			} else {
				DialogManager.showDialogAlert(_activity,
						getString(R.string.enhance_txt),
						getString(R.string.close), getString(R.string.enhanc),
						ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						SplashScreen.mTargetClass, this);
			}
			break;
		case R.id.agent_reviews:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = MyReviewActivity.class;
			intent = new Intent(_activity, MyReviewActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_friends:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = FriendsMainScreen.class;
			intent = new Intent(_activity, FriendsMainScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_scheduling:
			enhance_Profile = (String) GlobalMethods.getValueFromPreference(
					this, GlobalMethods.STRING_PREFERENCE,
					AppConstants.ENHANCED_PROFILE);
			if (enhance_Profile.equals("1")) {
				AppConstants.view_id = v.getId();
				setAgentBackground(v.getId());
				SplashScreen.mTargetClass = SchedulingActivity.class;
				intent = new Intent(_activity, SchedulingActivity.class);
				startActivity(intent);
				finish();
			} else {
				DialogManager.showDialogAlert(_activity,
						getString(R.string.enhance_txt),
						getString(R.string.close), getString(R.string.enhanc),
						ProfileScreen.class, R.anim.slide_in_right,
						R.anim.slide_out_left, ProfileScreen.class,
						SplashScreen.mTargetClass, this);
			}
			break;
		case R.id.agent_profile:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = ProfileScreen.class;
			intent = new Intent(_activity, ProfileScreen.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_homes_for_rent:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			AppConstants.agent_home_type = "rent";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Rent");
			startActivity(intent);
			// finish();
			break;
		case R.id.agent_homes_for_sale:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			AppConstants.agent_home_type = "sale";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "Sale");
			startActivity(intent);
			// finish();
			break;
		case R.id.agent_saved_searches:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = SavedSearchActivity.class;
			intent = new Intent(_activity, SavedSearchActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_my_favourites:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = MyFavouriteActivity.class;
			intent = new Intent(_activity, MyFavouriteActivity.class);
			AppConstants.CALL_MAP = "true";
			startActivity(intent);
			finish();
			break;
		case R.id.agent_alerts:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = AlertsActivity.class;
			intent = new Intent(_activity, AlertsActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_open_house:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			AppConstants.agent_home_type = "openhouse";
			SplashScreen.mTargetClass = MapFragmentActivity.class;
			intent = new Intent(BaseActivity.this, MapFragmentActivity.class);
			intent.putExtra("mSlideClick", "OpenHouse");
			startActivity(intent);
			finish();
			break;
		case R.id.agent_find_agent:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = FindAgentActivity.class;
			intent = new Intent(_activity, FindAgentActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_feedback:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = FeedbackActivity.class;
			intent = new Intent(_activity, FeedbackActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_rate_app:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			final String appPackageName = getPackageName(); 
			try {
				startActivity(new Intent(Intent.ACTION_VIEW,
						Uri.parse("market://details?id=" + appPackageName)));
			} catch (android.content.ActivityNotFoundException anfe) {
				startActivity(new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("https://play.google.com/store/apps/details?id="
								+ appPackageName)));
			}
			setGoogleAnalytics(this);
			break;
		case R.id.agent_share_app:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = ShareAppActivity.class;
			intent = new Intent(_activity, ShareAppActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.agent_settings:
			AppConstants.view_id = v.getId();
			setAgentBackground(v.getId());
			SplashScreen.mTargetClass = SettingsActivity.class;
			intent = new Intent(_activity, SettingsActivity.class);
			startActivity(intent);
			finish();
			break;

		}
	}

	protected void slideUserNameComponents() {
		UserName = (String) GlobalMethods.getValueFromPreference(_activity,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_user_name);
		mUserName = (TextView) findViewById(R.id.username);
		main_lay = (LinearLayout) findViewById(R.id.main_layout);
		mUserName.setTypeface(helvetica_bold);
		if (UserName.equalsIgnoreCase("") || UserName.equalsIgnoreCase("0")) {
			mUserName.setText("Login");
		} else {
			mUserName.setText(UserName);
		}
	}

	public void onMenuUserNameClick(View v) {
		AppConstants.view_id = v.getId();
		switch (v.getId()) {
		case R.id.user_name_layout:
			main_lay.setBackgroundColor(Color.parseColor("#000000"));
			if (mUserName.getText().toString().trim().equalsIgnoreCase("Login")) {
				AppConstants.Login_From_Map = "true";
				intent = new Intent(_activity, LoginActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			} else {
				logout();
			}
			break;
		case R.id.notification_icon:
			if (UserID.equals("") || UserID.equals("0")) {
				moveToLogin();
			} else {
				intent = new Intent(_activity, NotificationActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;
		}

		mSelectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		if (mSelectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| mSelectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			setBuyBackground(v.getId());
		} else if (mSelectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			setSellBackground(v.getId());
		} else if (mSelectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| mSelectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			setAgentBackground(v.getId());
		} else {
			setBuyBackground(v.getId());
		}
	}

	protected void buySlidemenuComponents() {

		buy_exclusives = (RelativeLayout) findViewById(R.id.buy_exclusives_rel);
		buy_homes_for_rent = (RelativeLayout) findViewById(R.id.buy_homes_for_rent_rel);
		buy_homes_for_sale = (RelativeLayout) findViewById(R.id.buy_homes_for_sale_rel);
		buy_saved_searches = (RelativeLayout) findViewById(R.id.buy_saved_searches_rel);
		buy_my_favourites = (RelativeLayout) findViewById(R.id.buy_my_favourites_rel);
		buy_alerts = (RelativeLayout) findViewById(R.id.buy_alerts_rel);
		buy_open_house = (RelativeLayout) findViewById(R.id.buy_open_house_rel);
		buy_find_agent = (RelativeLayout) findViewById(R.id.buy_find_agent_rel);
		buy_reviews = (RelativeLayout) findViewById(R.id.buy_reviews_rel);
		buy_friends = (RelativeLayout) findViewById(R.id.buy_friends_rel);
		buy_scheduling = (RelativeLayout) findViewById(R.id.buy_scheduling_rel);
		buy_profile = (RelativeLayout) findViewById(R.id.buy_profile_rel);
		buy_feedback = (RelativeLayout) findViewById(R.id.buy_feedback_rel);
		buy_rate_app = (RelativeLayout) findViewById(R.id.buy_rate_app_rel);
		buy_share_app = (RelativeLayout) findViewById(R.id.buy_share_app_rel);
		buy_settings = (RelativeLayout) findViewById(R.id.buy_settings_rel);

		TextView buy_search_txt = (TextView) findViewById(R.id.search_text);
		TextView buy_my_block_txt = (TextView) findViewById(R.id.my_block);
		TextView buy_more_txt = (TextView) findViewById(R.id.more);

		buy_search_txt.setTypeface(helvetica_bold);
		buy_my_block_txt.setTypeface(helvetica_bold);
		buy_more_txt.setTypeface(helvetica_bold);

	}

	protected void sellSlidemenuComponents() {
		sell_exclusives = (RelativeLayout) findViewById(R.id.sell_exclusives_rel);
		sell_properties = (RelativeLayout) findViewById(R.id.sell_properties_rel);
		sell_hot_lead = (RelativeLayout) findViewById(R.id.sell_hot_lead_rel);
		sell_reviews = (RelativeLayout) findViewById(R.id.sell_reviews_rel);
		sell_friends = (RelativeLayout) findViewById(R.id.sell_friends_rel);
		sell_scheduling = (RelativeLayout) findViewById(R.id.sell_scheduling_rel);
		sell_profile = (RelativeLayout) findViewById(R.id.sell_profile_rel);
		sell_homes_for_rent = (RelativeLayout) findViewById(R.id.sell_homes_for_rent_rel);
		sell_homes_for_sale = (RelativeLayout) findViewById(R.id.sell_homes_for_sale_rel);
		sell_saved_searches = (RelativeLayout) findViewById(R.id.sell_saved_searches_rel);
		sell_my_favourites = (RelativeLayout) findViewById(R.id.sell_my_favourites_rel);
		sell_alerts = (RelativeLayout) findViewById(R.id.sell_alerts_rel);
		sell_open_house = (RelativeLayout) findViewById(R.id.sell_open_house_rel);
		sell_find_agent = (RelativeLayout) findViewById(R.id.sell_find_agent_rel);
		sell_feedback = (RelativeLayout) findViewById(R.id.sell_feedback_rel);
		sell_rate_app = (RelativeLayout) findViewById(R.id.sell_rate_app_rel);
		sell_share_app = (RelativeLayout) findViewById(R.id.sell_share_app_rel);
		sell_settings = (RelativeLayout) findViewById(R.id.sell_settings_rel);

		TextView sell_search_txt = (TextView) findViewById(R.id.search_text_seller);
		TextView sell_my_block_txt = (TextView) findViewById(R.id.my_block_seller);
		TextView sell_more_txt = (TextView) findViewById(R.id.more_sell);

		sell_search_txt.setTypeface(helvetica_bold);
		sell_my_block_txt.setTypeface(helvetica_bold);
		sell_more_txt.setTypeface(helvetica_bold);
	}

	protected void agentSlidemenuComponents() {
		agent_exclusives = (RelativeLayout) findViewById(R.id.agent_exclusives_rel);
		agent_listings = (RelativeLayout) findViewById(R.id.agent_listings_rel);
		agent_hot_lead = (RelativeLayout) findViewById(R.id.agent_hot_lead_rel);
		agent_reviews = (RelativeLayout) findViewById(R.id.agent_reviews_rel);
		agent_friends = (RelativeLayout) findViewById(R.id.agent_friends_rel);
		agent_scheduling = (RelativeLayout) findViewById(R.id.agent_scheduling_rel);
		agent_profile = (RelativeLayout) findViewById(R.id.agent_profile_rel);
		agent_homes_for_rent = (RelativeLayout) findViewById(R.id.agent_homes_for_rent_rel);
		agent_homes_for_sale = (RelativeLayout) findViewById(R.id.agent_homes_for_sale_rel);
		agent_saved_searches = (RelativeLayout) findViewById(R.id.agent_saved_searches_rel);
		agent_my_favourites = (RelativeLayout) findViewById(R.id.agent_my_favourites_rel);
		agent_alerts = (RelativeLayout) findViewById(R.id.agent_alerts_rel);
		agent_open_house = (RelativeLayout) findViewById(R.id.agent_open_house_rel);
		agent_find_agent = (RelativeLayout) findViewById(R.id.agent_find_agent_rel);
		agent_feedback = (RelativeLayout) findViewById(R.id.agent_feedback_rel);
		agent_rate_app = (RelativeLayout) findViewById(R.id.agent_rate_app_rel);
		agent_share_app = (RelativeLayout) findViewById(R.id.agent_share_app_rel);
		agent_settings = (RelativeLayout) findViewById(R.id.agent_settings_rel);

		TextView agent_search_txt = (TextView) findViewById(R.id.search_text_agent);
		TextView agent_my_block_txt = (TextView) findViewById(R.id.my_block_agent);
		TextView agent_more_txt = (TextView) findViewById(R.id.more_agent);

		agent_search_txt.setTypeface(helvetica_bold);
		agent_my_block_txt.setTypeface(helvetica_bold);
		agent_more_txt.setTypeface(helvetica_bold);
	}

	protected void setBuyBackground(int id) {

		buy_exclusives
				.setBackgroundResource(id == R.id.buy_exclusives ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_homes_for_rent
				.setBackgroundResource(id == R.id.buy_homes_for_rent ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_homes_for_sale
				.setBackgroundResource(id == R.id.buy_homes_for_sale ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_saved_searches
				.setBackgroundResource(id == R.id.buy_saved_searches ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_my_favourites
				.setBackgroundResource(id == R.id.buy_my_favourites ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_alerts
				.setBackgroundResource(id == R.id.buy_alerts ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_open_house
				.setBackgroundResource(id == R.id.buy_open_house ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_find_agent
				.setBackgroundResource(id == R.id.buy_find_agent ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_reviews
				.setBackgroundResource(id == R.id.buy_reviews ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_friends
				.setBackgroundResource(id == R.id.buy_friends ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_scheduling
				.setBackgroundResource(id == R.id.buy_scheduling ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_profile
				.setBackgroundResource(id == R.id.buy_profile ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_feedback
				.setBackgroundResource(id == R.id.buy_feedback ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_rate_app
				.setBackgroundResource(id == R.id.buy_rate_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_share_app
				.setBackgroundResource(id == R.id.buy_share_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		buy_settings
				.setBackgroundResource(id == R.id.buy_settings ? R.drawable.menu_bar
						: R.color.slide_grey);

	}

	protected void setSellBackground(int id) {

		sell_exclusives
				.setBackgroundResource(id == R.id.sell_exclusives ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_properties
				.setBackgroundResource(id == R.id.sell_properties ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_hot_lead
				.setBackgroundResource(id == R.id.sell_hot_lead ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_reviews
				.setBackgroundResource(id == R.id.sell_reviews ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_friends
				.setBackgroundResource(id == R.id.sell_friends ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_scheduling
				.setBackgroundResource(id == R.id.sell_scheduling ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_profile
				.setBackgroundResource(id == R.id.sell_profile ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_homes_for_rent
				.setBackgroundResource(id == R.id.sell_homes_for_rent ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_homes_for_sale
				.setBackgroundResource(id == R.id.sell_homes_for_sale ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_saved_searches
				.setBackgroundResource(id == R.id.sell_saved_searches ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_my_favourites
				.setBackgroundResource(id == R.id.sell_my_favourites ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_alerts
				.setBackgroundResource(id == R.id.sell_alerts ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_open_house
				.setBackgroundResource(id == R.id.sell_open_house ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_find_agent
				.setBackgroundResource(id == R.id.sell_find_agent ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_feedback
				.setBackgroundResource(id == R.id.sell_feedback ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_rate_app
				.setBackgroundResource(id == R.id.sell_rate_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_share_app
				.setBackgroundResource(id == R.id.sell_share_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		sell_settings
				.setBackgroundResource(id == R.id.sell_settings ? R.drawable.menu_bar
						: R.color.slide_grey);

	}

	protected void setAgentBackground(int id) {

		agent_exclusives
				.setBackgroundResource(id == R.id.agent_exclusives ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_listings
				.setBackgroundResource(id == R.id.agent_listings ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_hot_lead
				.setBackgroundResource(id == R.id.agent_hot_lead ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_reviews
				.setBackgroundResource(id == R.id.agent_reviews ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_friends
				.setBackgroundResource(id == R.id.agent_friends ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_scheduling
				.setBackgroundResource(id == R.id.agent_scheduling ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_profile
				.setBackgroundResource(id == R.id.agent_profile ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_homes_for_rent
				.setBackgroundResource(id == R.id.agent_homes_for_rent ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_homes_for_sale
				.setBackgroundResource(id == R.id.agent_homes_for_sale ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_saved_searches
				.setBackgroundResource(id == R.id.agent_saved_searches ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_my_favourites
				.setBackgroundResource(id == R.id.agent_my_favourites ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_alerts
				.setBackgroundResource(id == R.id.agent_alerts ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_open_house
				.setBackgroundResource(id == R.id.agent_open_house ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_find_agent
				.setBackgroundResource(id == R.id.agent_find_agent ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_feedback
				.setBackgroundResource(id == R.id.agent_feedback ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_rate_app
				.setBackgroundResource(id == R.id.agent_rate_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_share_app
				.setBackgroundResource(id == R.id.agent_share_app ? R.drawable.menu_bar
						: R.color.slide_grey);
		agent_settings
				.setBackgroundResource(id == R.id.agent_settings ? R.drawable.menu_bar
						: R.color.slide_grey);

	}

	public void moveToLogin() {
		intent = new Intent(_activity, LoginActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

	}

	private void logout() {
		GlobalMethods.showOptionDialogListener(_activity,
				getString(R.string.app_name), getString(R.string.alert_logout),
				getString(R.string.sign_out), getString(R.string.cancel),
				new OptionDialogInterfaceListener() {

					@Override
					public void okClick() {

						GlobalMethods.storeValuetoPreference(_activity,
								GlobalMethods.STRING_PREFERENCE,
								AppConstants.pref_userId, "");
						GlobalMethods.storeValuetoPreference(_activity,
								GlobalMethods.STRING_PREFERENCE,
								AppConstants.pref_user_name, "");
						GlobalMethods.storeValuetoPreference(_activity,
								GlobalMethods.STRING_PREFERENCE,
								AppConstants.SYNC_CONTACTS, "0");

						callLogoutApi();
						LoginManager.getInstance().logOut();
//						Session session = Session.getActiveSession();
//						if (session != null) {
//							session.close();
//							session.closeAndClearTokenInformation();
//							Session.setActiveSession(null);
//						}
						LoginActivity.signOutFromGplus();
					}

					@Override
					public void cancelClick() {

					}
				});
	}

	private void callLogoutApi() {
		String Url = AppConstants.BASE_URL + "logout";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									CommonResponse mResponse = new Gson()
											.fromJson(json.toString(),
													CommonResponse.class);
									if (mResponse.getError_code()
											.equalsIgnoreCase(
													AppConstants.SUCCESS_CODE)) {
										mUserName.setText("Login");
										UserID = "";
										mAlert = "CallApi";
										DialogManager
												.showCustomAlertDialog(
														_activity,
														BaseActivity.this,
														"You are logged out successfully");
									}
								} else {
									statusErrorCode(status);
								}
							}

						});

	}

	@Override
	protected void onStart() {
		super.onStart();
		EasyTracker.getInstance(_activity).activityStart(_activity);

	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(_activity).activityStop(_activity);
	}

	public void showToast(Activity activity, String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	public void sendDeviceID(String mUserID, String DeviceID, String Device) {

		String Url = AppConstants.BASE_URL + "device";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mUserID);
		params.put("device_id", DeviceID);
		params.put("device", Device);

		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							System.out.println(json.toString());
						} else {
							statusErrorCode(status);
						}
					}

				});
	}
}
