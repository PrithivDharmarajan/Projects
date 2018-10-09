package com.smaat.renterblock.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FeedbackActivity extends BaseActivity implements OnClickListener,
		DialogMangerCallback {
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;

	/**
	 * Share App Declaration
	 */
	private TextView mSubjectTxt, mAndroidVersionTxt, mAppVersionTxt;
	private EditText mMessageEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_feedback);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		getDeviceVersion();
		getAppVersion();
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {

		}

	}

	private void getDeviceVersion() {
		StringBuilder builder = new StringBuilder();
		builder.append(Build.VERSION.RELEASE);
		mAndroidVersionTxt.setText(builder.toString());
	}

	private void getAppVersion() {
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			String version = info.versionName;
			mAppVersionTxt.setText("v" + version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {
		/**
		 * ShareApp Initialization
		 */
		mAndroidVersionTxt = (TextView) findViewById(R.id.and_ver_txt);
		mSubjectTxt = (TextView) findViewById(R.id.subject_txt);
		mAppVersionTxt = (TextView) findViewById(R.id.app_ver_txt);

		mMessageEdit = (EditText) findViewById(R.id.message_edit);
		mMessageEdit.setMovementMethod(new ScrollingMovementMethod());

		/**
		 * Slide Menu Intialization
		 */

		slideUserNameComponents();
		agentSlidemenuComponents();
		sellSlidemenuComponents();
		buySlidemenuComponents();

		mBuyRentView = (LinearLayout) findViewById(R.id.buyer_renter_view);
		mSellerView = (LinearLayout) findViewById(R.id.seller_view);
		mAgentBrokerView = (LinearLayout) findViewById(R.id.agent_broker_view);

		selectedType = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.pref_type);
		if (selectedType.equalsIgnoreCase(AppConstants.BUYER)
				|| selectedType.equalsIgnoreCase(AppConstants.RENTER)) {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_feedback);
			AppConstants.view_id = R.id.buy_feedback;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_feedback);
			AppConstants.view_id = R.id.sell_feedback;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_feedback);
			AppConstants.view_id = R.id.agent_feedback;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_feedback);
			AppConstants.view_id = R.id.buy_feedback;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_icon:
			slide_holder.toggle();
			break;
		case R.id.send_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				sendFeedback();
			}

			break;
		}
	}

	private void sendFeedback() {

		String message = mMessageEdit.getText().toString().trim();
		String Url = AppConstants.BASE_URL + "feedback";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("message", message);

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

										DialogManager.showCustomAlertDialog(
												FeedbackActivity.this,
												FeedbackActivity.this,
												mResponse.getMsg());

									}
								}

							}

						});

	}

	@Override
	public void onOkclick() {
		/**
		 * Close Dialog
		 */
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

}
