package com.smaat.renterblock.findagent;

import java.util.HashMap;

import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FindAgentContactUserActivity extends BaseActivity implements
		OnClickListener, DialogMangerCallback {
	private Typeface mTypeface;
	private EditText mMessageEdit;
	private String mMessage = "", mUserID = "", mAgentUserID = "";
	private Bundle mBundle;
	private AgentUserDetailsEntity mResult;
	private TextView mUserName, mFriedsCount, mReviewsCount, mPhotosCount,
			mAgentWith, mLicense;
	private RatingBar mRatingBar;
	private String mCallApi = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_find_agent_contact_user);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		mUserID = GlobalMethods.getUserID(this);
		initComponents();

	}

	private void initComponents() {

		mMessageEdit = (EditText) findViewById(R.id.message_edit);
		mMessageEdit.setMovementMethod(new ScrollingMovementMethod());
		mMessageEdit.setTypeface(helvetica_normal);

		mUserName = (TextView) findViewById(R.id.user_name_txt);
		mFriedsCount = (TextView) findViewById(R.id.friends_count);
		mReviewsCount = (TextView) findViewById(R.id.reviews_count);
		mPhotosCount = (TextView) findViewById(R.id.photos_count);
		mAgentWith = (TextView) findViewById(R.id.agent_with_txt);
		mLicense = (TextView) findViewById(R.id.license_txt);
		mRatingBar = (RatingBar) findViewById(R.id.user_ratingbar);

		mBundle = getIntent().getExtras();
		if (mBundle != null) {

			mResult = (AgentUserDetailsEntity) mBundle
					.getSerializable("mAgentResult");
			if (mResult != null) {
				setValues();
			}

		}
	}

	private void setValues() {

		mAgentUserID = mResult.getUser_id();

		mUserName.setText(mResult.getName());
		mFriedsCount.setText(mResult.getFriends_count());
		mReviewsCount.setText(mResult.getReviews_count());
		mPhotosCount.setText(mResult.getPhotos_count());
		mAgentWith.setText(mResult.getUser_type() + " with "
				+ mResult.getBusiness_name());
		mLicense.setText(mResult.getLicence());

		float userRating = Float.parseFloat(mResult.getUser_avg_rating());
		mRatingBar.setRating(userRating);

		aq().id(R.id.user_image).image(mResult.getUser_profileImage(), false,
				false, 200, R.drawable.profile_pic, null, 0);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;

		case R.id.send_btn:
			mMessage = mMessageEdit.getText().toString().trim();
			if (mMessage.equals("") || mMessage.length() <= 0) {
				mCallApi = "Alert";
				DialogManager.showCustomAlertDialog(this, this,
						getString(R.string.please_enter_message));
			} else {
				callSendMessage();
			}
			break;

		}

	}

	private void callSendMessage() {

		String Url = AppConstants.BASE_URL + "contactagentmail";

		GsonTransformer t = new GsonTransformer();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mUserID);
		params.put("agent_id", mAgentUserID);
		params.put("message", mMessage);

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
										mCallApi = "CallApi";
										DialogManager
												.showCustomAlertDialog(
														FindAgentContactUserActivity.this,
														FindAgentContactUserActivity.this,
														mResponse.getMsg());

									}

								} else {
									statusErrorCode(status);
								}
							}

						});
	}

	@Override
	public void onOkclick() {
		if (mCallApi.equals("CallApi")) {
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
		} else {
			/**
			 * Close Dialog
			 */
		}
	}
}
