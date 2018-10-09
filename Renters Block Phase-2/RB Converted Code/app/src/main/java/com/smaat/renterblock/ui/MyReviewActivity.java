package com.smaat.renterblock.ui;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.MyReviewAdapter;
import com.smaat.renterblock.entity.MyReviewsResult;
import com.smaat.renterblock.model.MyReviewResponse;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class MyReviewActivity extends BaseActivity implements OnClickListener,
		DialogMangerCallback {

	private LinearLayout mMenuIocn, mEditIcon;
	private ListView mReviewList;
	private MyReviewAdapter mMyReviewAdapter;
	private MyReviewsResult mMyReviewsResult;
	private String UserID;
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	private Button header_left;
	private Bundle extras;
	private TextView header_txt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myreviews);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance().getHelvetica(
				this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		setGoogleAnalytics(MyReviewActivity.this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			getMyReview();
		}
		extras = getIntent().getExtras();
		if (extras != null) {
			mEditIcon.setVisibility(View.INVISIBLE);
		}
	}

	private void initComponents() {

		mMenuIocn = (LinearLayout) findViewById(R.id.menu_icon);
		mEditIcon = (LinearLayout) findViewById(R.id.edit_icon);
		mReviewList = (ListView) findViewById(R.id.my_reviews_list);

		header_txt = (TextView) findViewById(R.id.how_edit);
		header_txt.setTypeface(helvetica_bold);

		mMenuIocn.setOnClickListener(this);
		mEditIcon.setOnClickListener(this);

		header_left = (Button) findViewById(R.id.header_left_btn);
		if (AppConstants.from_profile_reviews.equals("true")
				|| AppConstants.from_map_reviews.equals("true")) {
			header_left.setBackgroundResource(R.drawable.back_arrow_white);
		} else {
			header_left.setBackgroundResource(R.drawable.slide_menu_icon);
		}
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
			setBuyBackground(R.id.buy_reviews);
			AppConstants.view_id = R.id.buy_reviews;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_reviews);
			AppConstants.view_id = R.id.sell_reviews;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_reviews);
			AppConstants.view_id = R.id.agent_reviews;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_reviews);
			AppConstants.view_id = R.id.buy_reviews;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

		mReviewList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (extras != null) {
				
				} else {
					Intent intent = new Intent(MyReviewActivity.this,
							EditUpdateReviewActivity.class);
					intent.putExtra("mMyReviewsResult", mMyReviewsResult);
					intent.putExtra("pos", pos);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				}
			}
		});

	}

	private void setReviewAdapter(MyReviewsResult result) {
		mMyReviewAdapter = new MyReviewAdapter(this, result);
		mReviewList.setAdapter(mMyReviewAdapter);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.menu_icon:
			if (AppConstants.from_profile_reviews.equals("true")) {
				if (extras != null) {
					finish();
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
				} else {
					Intent inte = new Intent(MyReviewActivity.this,
							ProfileScreen.class);
					startActivity(inte);
					overridePendingTransition(R.anim.slide_in_right,
							R.anim.slide_out_left);
					AppConstants.from_profile_reviews = "false";
				}
			} else if (AppConstants.from_map_reviews.equals("true")) {
				Intent inte = new Intent(MyReviewActivity.this,
						MapFragmentActivity.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				AppConstants.from_map_reviews = "false";
			} else {
				slide_holder.toggle();
			}
			break;

		case R.id.edit_icon:
			if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
				moveToLogin();
			} else {
				AppConstants.from_map_list = false;
				Intent inte = new Intent(MyReviewActivity.this,
						MapFragmentActivity.class);
				inte.putExtra("review", "review");
				startActivity(inte);
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);

			}

			break;
		case R.id.username:
			break;
		}

	}

	private void getMyReview() {

		String Url = AppConstants.BASE_URL + "reviewproperty/myreview";
		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("review_user_id", UserID);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									MyReviewResponse obj = new Gson().fromJson(
											json.toString(),
											MyReviewResponse.class);
									onSuccessRequest(obj);
									System.out.println(json);
								} else {
									statusErrorCode(status);
								}

							}
						});

	}

	protected void onSuccessRequest(MyReviewResponse obj) {

		if (obj.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			mMyReviewsResult = obj.getResult();
			setReviewAdapter(mMyReviewsResult);
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

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {

	}
}
