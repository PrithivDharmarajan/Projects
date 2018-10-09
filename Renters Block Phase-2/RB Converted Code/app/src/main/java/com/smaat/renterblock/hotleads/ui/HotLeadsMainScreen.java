package com.smaat.renterblock.hotleads.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.hotleads.adapter.HotLeadsPropertyListAdapter;
import com.smaat.renterblock.hotleads.entity.HotLeadsModelEntity;
import com.smaat.renterblock.hotleads.entity.HotLeadsPropertyEntity;
import com.smaat.renterblock.hotleads.entity.LeadsActiveEntity;
import com.smaat.renterblock.hotleads.entity.LeadsListEntity;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class HotLeadsMainScreen extends BaseActivity {

	private ImageView edit_view, slide_menu;
	private TextView header_txt;
	private ListView hot_leads_list;
	private HotLeadsPropertyListAdapter hot_leads_adapter;
	private String UserID = "";
	private ProgressBar show_progress;
	static Context mContext;
	public static String prop_id = "";

	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;
	// private Button mBuySettings, mAgentSettings, mSellSettings;
	// private ArrayList<HotLeadsPropertyEntity> hot_leads_list_arr;
	HotLeadsPropertyEntity hot_leads_ent;

	private ArrayList<LeadsActiveEntity> leads_arr_list = new ArrayList<LeadsActiveEntity>();

	private ArrayList<HotLeadsPropertyEntity> hot_leads_arr_list = new ArrayList<HotLeadsPropertyEntity>();

	LeadsListEntity leads_ent;

	public static String Current_server_time = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hot_leads_home_activity);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);
		mContext = HotLeadsMainScreen.this;
		UserID = GlobalMethods.getUserID(this);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initializeViews();
		setGoogleAnalytics(this);
		if (UserID.equalsIgnoreCase("") || UserID.equalsIgnoreCase("0")) {
			moveToLogin();
		} else {
			callHotLeadsApi();
		}

	}

	private void initializeViews() {
		edit_view = (ImageView) findViewById(R.id.filter);
		slide_menu = (ImageView) findViewById(R.id.slide);
		slide_menu.setImageResource(R.drawable.slide_button);
		header_txt = (TextView) findViewById(R.id.how);
		header_txt.setTypeface(helvetica_bold);
		header_txt.setText(getString(R.string.hot_leads));
		edit_view.setVisibility(View.INVISIBLE);
		hot_leads_list = (ListView) findViewById(R.id.hot_leads_list);
		show_progress = (ProgressBar) findViewById(R.id.show_progress);

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
			setBuyBackground(R.id.agent_hot_lead);
			AppConstants.view_id = R.id.agent_hot_lead;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.agent_hot_lead);
			AppConstants.view_id = R.id.agent_hot_lead;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_hot_lead);
			AppConstants.view_id = R.id.agent_hot_lead;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.agent_hot_lead);
			AppConstants.view_id = R.id.agent_hot_lead;
		}
		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);
	}

	private void callHotLeadsApi() {
		String Url = AppConstants.BASE_URL + "getmyhotleads";

		GsonTransformer t = new GsonTransformer();
		show_progress.setVisibility(View.VISIBLE);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("userId", UserID);
		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						if (json != null) {
							HotLeadsModelEntity mResponse = new Gson()
									.fromJson(json.toString(),
											HotLeadsModelEntity.class);
							Current_server_time = mResponse
									.getCurrentdatetime();
							show_progress.setVisibility(View.GONE);
							onRequest(mResponse);
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	private void onRequest(HotLeadsModelEntity mResponse) {
		if (mResponse.getResult() != null && mResponse.getResult().size() != 0) {
			hot_leads_adapter = new HotLeadsPropertyListAdapter(
					HotLeadsMainScreen.this, R.layout.hot_leads_adapter_view,
					mResponse.getResult());
			hot_leads_list.setAdapter(hot_leads_adapter);
			hot_leads_adapter.notifyDataSetChanged();
		}

	}

	public static void leadsView(LeadsListEntity leadslist) {
		Intent leads = new Intent(mContext, LeadsActivity.class);
		leads.putExtra("leadsList", leadslist);
		mContext.startActivity(leads);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide:
			slide_holder.toggle();
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent inte = new Intent(HotLeadsMainScreen.this,
				MapFragmentActivity.class);
		startActivity(inte);
		finish();
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
