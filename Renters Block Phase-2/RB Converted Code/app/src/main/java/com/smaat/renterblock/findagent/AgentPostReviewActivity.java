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
import android.widget.RatingBar.OnRatingBarChangeListener;
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

public class AgentPostReviewActivity extends BaseActivity implements
		OnClickListener, DialogMangerCallback {

	private Typeface mTypeface;
	private EditText mReviewText;
	private TextView mClose, mContinue, mReviewHeaderText;
	private Bundle mBundle;
	private String mCommentStr = "", mRating = "0.0", mUserID = "",
			mReviewUserID = "";
	private RatingBar mRatingBar;
	private String mCallBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_write_review);

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
		mReviewText = (EditText) findViewById(R.id.review_edit);
		mReviewText.setHint(getString(R.string.find_an_agent_rev_hint));
		mRatingBar = (RatingBar) findViewById(R.id.review_ratingbar);

		mReviewHeaderText = (TextView) findViewById(R.id.review_head_txt);
		mReviewHeaderText.setTypeface(helvetica_bold);
		mReviewHeaderText.setText("Post Review");

		mClose = (TextView) findViewById(R.id.close);

		mContinue = (TextView) findViewById(R.id.continue_txt);
		mContinue.setText("Post");

		mReviewText.setMovementMethod(new ScrollingMovementMethod());

		mReviewText.setCursorVisible(true);

		mClose.setOnClickListener(this);
		mContinue.setOnClickListener(this);

		mReviewText.post(new Runnable() {
			@Override
			public void run() {
				mReviewText.setSelection(mReviewText.getText().toString()
						.length());
			}
		});
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
		mBundle = getIntent().getExtras();

		if (mBundle != null) {

			mReviewUserID = mBundle.getString("ReviewUserID");
			if (mReviewUserID == null) {
				mReviewUserID = "";
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.continue_txt:
			if (mRating.equals("0.0")) {
				mCallBack = "Alert";
				DialogManager.showCustomAlertDialog(this, this,
						"Please select Rating");
			} else {
				callPostReview();
			}
			break;
		case R.id.close:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;
		}
	}

	private void callPostReview() {
		mCommentStr = mReviewText.getText().toString();

		String Url = AppConstants.BASE_URL + "reviewuser";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", mUserID);
		params.put("comments", mCommentStr);
		params.put("rating", mRating);
		params.put("review_user_id", mReviewUserID);

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
												AgentPostReviewActivity.this,
												AgentPostReviewActivity.this,
												response.getMsg());

									}
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	@Override
	public void onOkclick() {
		if (mCallBack.equalsIgnoreCase("Post")) {
			AppConstants.isAPI = true;
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
		} else {
			/**
			 * Close Popup
			 */
		}
	}
}
