package com.smaat.renterblock.findagent;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.ui.FriendChatScreen;
import com.smaat.renterblock.model.ChatSendResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FindAgentAboutActivity extends BaseActivity implements
		OnClickListener {

	private Typeface mTypeface;
	private Bundle mBundle;
	private AgentUserDetailsEntity mResult;
	private TextView mUserName, mFriedsCount, mReviewsCount, mPhotosCount,
			mAgentWith, mLicense, mDescription;
	private RatingBar mRatingBar;
	private TextView header_txt;

	private String mUserID, mEnhance_profile, mIs_friend, mUser_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_find_agent_about);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		header_txt = (TextView) findViewById(R.id.header_txt);
		header_txt.setTypeface(helvetica_bold);
		initComponents();

	}

	private void initComponents() {

		mUserName = (TextView) findViewById(R.id.user_name_txt);
		mUserName.setTypeface(helvetica_bold);
		mFriedsCount = (TextView) findViewById(R.id.friends_count);
		mReviewsCount = (TextView) findViewById(R.id.reviews_count);
		mPhotosCount = (TextView) findViewById(R.id.photos_count);
		mAgentWith = (TextView) findViewById(R.id.agent_with_txt);
		mLicense = (TextView) findViewById(R.id.license_txt);
		mRatingBar = (RatingBar) findViewById(R.id.user_ratingbar);

		mDescription = (TextView) findViewById(R.id.description);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {

			mResult = (AgentUserDetailsEntity) mBundle
					.getSerializable("mAgentResult");

			mUserID = mBundle.getString("mUserID");
			mEnhance_profile = mBundle.getString("mEnhanced_profile");
			mIs_friend = mBundle.getString("is_friend");
			mUser_name = mBundle.getString("mfr_Username");

			if (mResult != null) {
				setValues();
			}

		}
	}

	private void setValues() {

		mUserName.setText(mResult.getName());
		mFriedsCount.setText(mResult.getFriends_count());
		mReviewsCount.setText(mResult.getReviews_count());
		mPhotosCount.setText(mResult.getPhotos_count());
		mAgentWith.setText(mResult.getUser_type() + " with "
				+ mResult.getBusiness_name());
		mLicense.setText(mResult.getLicence());
		mDescription.setText(mResult.getDescription());
		mDescription.setMovementMethod(new ScrollingMovementMethod());
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
		case R.id.user_image:
			Intent profile = new Intent(FindAgentAboutActivity.this,
					ProfileScreen.class);
			profile.putExtra("user_id", mResult.getUser_id());
			profile.putExtra("call_from", "Agent");
			startActivity(profile);
			break;
		case R.id.chat_icon:
			callGroupIdService();
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
		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									ChatSendResponse obj = new Gson().fromJson(
											json.toString(),
											ChatSendResponse.class);
									// onSuccessRequest(obj);
									callChatService(obj.result);
								} else {
									statusErrorCode(status);
								}
							}
						});
	}

	private void callChatService(String group_id) {
		Intent intent = new Intent(FindAgentAboutActivity.this,
				FriendChatScreen.class);
		intent.putExtra("ids", mResult.getUser_id());
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

}
