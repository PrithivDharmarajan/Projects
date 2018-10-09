package com.smaat.renterblock.savedsearch;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class SavedSearchEditView extends BaseActivity implements
		OnClickListener, DialogMangerCallback {

	private boolean isem_Clicked = false;
	private boolean isin_Click = false;
	private Button email_notification, inquiry;
	static Context context;
	Bundle mBundle;
	String getfilter_name;
	String prefgetfilter_name;
	// TextView mSearchName;
	SavedSearchResponseEntity search_ent;

	TextView mSearchName, mHeaderText;
	Button btnUpdate;
	int eMailnotificationvalue, inquiryvalue;

	String searchid, filtername, email_notif, m_inquiry, m_type;
	private String mAlert = "Alert";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.saved_search_edit_view);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);

		context = SavedSearchEditView.this;
		UserID = GlobalMethods.getUserID(this);

		initComponents();

	}

	private void initComponents() {

		mSearchName = (TextView) findViewById(R.id.search_name);
		mHeaderText = (TextView) findViewById(R.id.header_txt);
		mHeaderText.setText("Saved Searches");
		email_notification = (Button) findViewById(R.id.email_notification_btn);
		inquiry = (Button) findViewById(R.id.inquiry_button);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(this);

		searchid = (String) GlobalMethods.getValueFromPreference(context,
				GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_Save_Search_id);

		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			search_ent = new SavedSearchResponseEntity();

			email_notif = mBundle.getString("email_notification");
			m_inquiry = mBundle.getString("inquiry");
			m_type = mBundle.getString("filter_type");
			if (email_notif.equals("1")) {
				email_notification.setBackgroundResource(R.drawable.toggle_on);
				isem_Clicked = false;
				eMailnotificationvalue = 1;
			} else if (email_notif.equals("0")) {
				email_notification.setBackgroundResource(R.drawable.toggle_off);
				isem_Clicked = true;
				eMailnotificationvalue = 0;
			}

			if (m_inquiry.equals("1")) {
				inquiry.setBackgroundResource(R.drawable.toggle_on);
				isin_Click = false;
				inquiryvalue = 1;
			} else if (m_inquiry.equals("0")) {
				inquiry.setBackgroundResource(R.drawable.toggle_off);
				isin_Click = true;
				inquiryvalue = 0;
			}

			getfilter_name = mBundle.getString("filter_name");

			mSearchName.setText(getfilter_name);

			search_ent = (SavedSearchResponseEntity) mBundle
					.getSerializable("Search_object");
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.name_lay:
			showSaveSearchDialog();
			break;
		case R.id.email_notification_btn:
			if (!isem_Clicked) {
				email_notification.setBackgroundResource(R.drawable.toggle_off);
				isem_Clicked = true;
				eMailnotificationvalue = 0;
				System.out.println(eMailnotificationvalue);

			} else {
				email_notification.setBackgroundResource(R.drawable.toggle_on);
				isem_Clicked = false;
				eMailnotificationvalue = 1;
				System.out.println(eMailnotificationvalue);
			}

			break;
		case R.id.inquiry_button:

			if (!isin_Click) {
				inquiry.setBackgroundResource(R.drawable.toggle_off);
				isin_Click = true;
				inquiryvalue = 0;
				System.out.println(inquiryvalue);
			} else {
				inquiry.setBackgroundResource(R.drawable.toggle_on);
				isin_Click = false;
				inquiryvalue = 1;
				System.out.println(inquiryvalue);
			}
			break;

		case R.id.btnUpdate:
			search_ent.setFilter_name(mSearchName.getText().toString());
			CallUpdateService();
			break;
		case R.id.back_icon:
			// AppConstants.isAPI = true;
			launchActivity(SavedSearchActivity.class);
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;

		}
	}

	private void showSaveSearchDialog() {
		mDialog = new Dialog(SavedSearchEditView.this,
				android.R.style.Theme_Translucent_NoTitleBar);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.save_search_dialog);
		mDialog.setCancelable(false);
		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		Button save = (Button) mDialog.findViewById(R.id.save);
		final EditText mLocation = (EditText) mDialog
				.findViewById(R.id.enter_search_name);
		mLocation.setText(mSearchName.getText().toString().trim());

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mDialog.dismiss();

			}
		});
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mSearchName.setText(mLocation.getText().toString().trim());
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	private void CallUpdateService() {

		String Url = AppConstants.BASE_URL + "savesearch/update";
		GsonTransformer t = new GsonTransformer();
		Gson gson = new Gson();
		prefgetfilter_name = gson.toJson(search_ent);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("filter_object", prefgetfilter_name);
		params.put("save_search_id", searchid);
		params.put("email_notification", eMailnotificationvalue);
		params.put("inquiry", inquiryvalue);
		params.put("filter_type", m_type);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(Url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									mAlert = "CallApi";
									DialogManager.showCustomAlertDialog(
											SavedSearchEditView.this,
											SavedSearchEditView.this,
											"Updated Successfully.");
								} else {
									statusErrorCode(status);
								}
							}
						});
	}

	@Override
	public void onOkclick() {
		if (mAlert.equalsIgnoreCase("CallApi")) {
			// AppConstants.isAPI = true;
			mAlert = "Alert";
			launchActivity(SavedSearchActivity.class);
			overridePendingTransition(R.anim.slide_out_left,
					R.anim.slide_in_right);

		} else {

		}

	}
}
