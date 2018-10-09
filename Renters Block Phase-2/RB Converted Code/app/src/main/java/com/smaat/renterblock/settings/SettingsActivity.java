package com.smaat.renterblock.settings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.SlideHolder;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ToggleButton;

public class SettingsActivity extends BaseActivity implements OnClickListener {
	/**
	 * Slide Menu Declaration
	 */
	private SlideHolder slide_holder;
	private LinearLayout mBuyRentView, mAgentBrokerView, mSellerView;
	private String selectedType;

	/**
	 * Settings Declaration
	 */
	private boolean boolNewSavedSearch = true, boolUpdateResultOnMap = true;
	private ToggleButton togNewSavedSearch, togUpdateResultOnMap;
	private EditText mSearchCountEdit;
	private Typeface mTypefaceBold;
	private TextView mHeaderTxt;
	private String mURL;
	private String mNEW_SAVED_SEARCH_MATCHES = "", mSEARCH_RESULT_COUNT = "", mUPDATE_RESULT_AS_MPA_MOVES = "";
	private Context mcontext;

	private ArrayList<String> email_ids_ar = new ArrayList<String>();
	private Button sync_contacts;

	private String sync_con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.activity_settings);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		setFont(root, mTypeface);
		setupUI(root);
		mNotification_bell = (ImageView) findViewById(R.id.notification_bell);
		initComponents();
		setGoogleAnalytics(this);
		sync_contacts = (Button) findViewById(R.id.sync_contacts);
		mcontext = SettingsActivity.this;
		sync_con = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SYNC_CONTACTS);
		// checkTimeforUpdation();
		if (sync_con.equals("0")) {
			sync_contacts.setBackgroundResource(R.drawable.toggle_off);
		} else {
			// checkTimeforUpdation();
			sync_contacts.setBackgroundResource(R.drawable.toggle_on);
		}
	}

	private void checkTimeforUpdation() {
		SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String cDateTime = dateFormats.format(new Date());
		GlobalMethods.storeValuetoPreference(SettingsActivity.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.Settings_current_time, cDateTime);
	}

	private void initComponents() {
		/**
		 * Settings Initialization
		 */
		togNewSavedSearch = (ToggleButton) findViewById(R.id.tog_new_saved_search);
		togUpdateResultOnMap = (ToggleButton) findViewById(R.id.tog_update_results);
		mSearchCountEdit = (EditText) findViewById(R.id.search_count_edit);
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
			setBuyBackground(R.id.buy_settings);
			AppConstants.view_id = R.id.buy_settings;
		} else if (selectedType.equalsIgnoreCase(AppConstants.SELLER)) {
			mSellerView.setVisibility(View.VISIBLE);
			setSellBackground(R.id.sell_settings);
			AppConstants.view_id = R.id.sell_settings;
		} else if (selectedType.equalsIgnoreCase(AppConstants.AGENT)
				|| selectedType.equalsIgnoreCase(AppConstants.BROKER)) {
			mAgentBrokerView.setVisibility(View.VISIBLE);
			setAgentBackground(R.id.agent_settings);
			AppConstants.view_id = R.id.agent_settings;
		} else {
			mBuyRentView.setVisibility(View.VISIBLE);
			setBuyBackground(R.id.buy_settings);
			AppConstants.view_id = R.id.buy_settings;
		}

		slide_holder = (SlideHolder) findViewById(R.id.slideHolder);

		slide_holder.setAllowInterceptTouch(false);

		mNEW_SAVED_SEARCH_MATCHES = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.NEW_SAVED_SEARCH_MATCHES);

		mSEARCH_RESULT_COUNT = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SEARCH_RESULT_COUNT);

		mUPDATE_RESULT_AS_MPA_MOVES = (String) GlobalMethods.getValueFromPreference(this,
				GlobalMethods.STRING_PREFERENCE, AppConstants.UPDATE_RESULT_AS_MAP_MOVES);

		if (mNEW_SAVED_SEARCH_MATCHES.equalsIgnoreCase("OFF")) {
			togNewSavedSearch.setBackgroundResource(R.drawable.toggle_off);
		} else {
			togNewSavedSearch.setBackgroundResource(R.drawable.toggle_on);
		}
		if (mUPDATE_RESULT_AS_MPA_MOVES.equalsIgnoreCase("OFF")) {
			togUpdateResultOnMap.setBackgroundResource(R.drawable.toggle_off);
		} else {
			togUpdateResultOnMap.setBackgroundResource(R.drawable.toggle_on);
		}
		mSearchCountEdit.setText(mSEARCH_RESULT_COUNT);
		mSearchCountEdit.setOnEditorActionListener(new OnEditorActionListener() {
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					GlobalMethods.storeValuetoPreference(SettingsActivity.this, GlobalMethods.STRING_PREFERENCE,
							AppConstants.SEARCH_RESULT_COUNT, v.getText().toString());
					hideSoftKeyboard(SettingsActivity.this);
					return true;
				}
				return false;
			}
		});
		mSearchCountEdit.setFilters(new InputFilter[] { new InputFilterMinMax(1, 50) });

	}

	// mSearchCountEdit.setFilters(new InputFilter[]{new
	// InputFilterMax(getActivity(),"50")});
	class InputFilterMinMax implements InputFilter {

		private int min, max;

		public InputFilterMinMax(int min, int max) {
			this.min = min;
			this.max = max;
		}

		public InputFilterMinMax(String min, String max) {
			this.min = Integer.parseInt(min);
			this.max = Integer.parseInt(max);
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			try {
				int input = Integer.parseInt(dest.toString() + source.toString());
				// int input = Integer.parseInt(source.toString());
				if (isInRange(min, max, input))
					return null;
			} catch (NumberFormatException nfe) {
			}
			return "";
		}

		private boolean isInRange(int a, int b, int c) {
			return b > a ? c >= a && c <= b : c >= b && c <= a;
		}
	}

	private void callAlertSearchApi(String on_off) {
		String Url = AppConstants.BASE_URL + "setting";

		GsonTransformer t = new GsonTransformer();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("setting", on_off);
		aq().transformer(t).ajax(Url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					System.out.println(json + "");
				} else {
					statusErrorCode(status);
				}
			}
		});
	}

	private void getContactEmails(Context context) {

		try {
			String emailIdOfContact = null;
			int emailType = Email.TYPE_WORK;
			String contactName = null;

			ContentResolver cr = context.getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					String id = cur.getString(cur.getColumnIndex(BaseColumns._ID));
					contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					// Log.i(TAG,"....contact name....." +
					// contactName);

					cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);

					Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + id, null, null);
					while (emails.moveToNext()) {
						emailIdOfContact = emails.getString(emails.getColumnIndex(Email.DATA));
						email_ids_ar.add("'" + emailIdOfContact + "'");

						emailType = emails.getInt(emails.getColumnIndex(Phone.TYPE));

					}
					emails.close();

				}
			}
			cur.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (email_ids_ar.size() != 0) {
			updateEmailContacts();
		}

	}

	private void updateEmailContacts() {

		String email_ids = email_ids_ar.toString();
		email_ids = email_ids.replace("[", "");
		email_ids = email_ids.replace("]", "");

		String url = AppConstants.BASE_URL + "contact";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("emails", email_ids);

		aq().transformer(t).ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {

			@Override
			public void callback(String url, JSONObject json, AjaxStatus status) {

				if (json != null) {
					System.out.println("updated successfully");
				} else {
					statusErrorCode(status);
				}

			}
		});
	}

	@Override
	public void onClick(View v) {
		sync_con = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.SYNC_CONTACTS);
		switch (v.getId()) {
		case R.id.sync_contacts:
			if (sync_con.equals("0")) {
				sync_contacts.setBackgroundResource(R.drawable.toggle_on);
				GlobalMethods.storeValuetoPreference(SettingsActivity.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.SYNC_CONTACTS, "1");
				checkTimeforUpdation();
				// getContactEmails(mcontext);
			} else {
				sync_contacts.setBackgroundResource(R.drawable.toggle_off);
				GlobalMethods.storeValuetoPreference(SettingsActivity.this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.SYNC_CONTACTS, "0");
			}
			break;
		case R.id.menu_icon:
			slide_holder.toggle();
			break;
		case R.id.tog_new_saved_search:
			String on_off = "";
			if (boolNewSavedSearch) {
				togNewSavedSearch.setBackgroundResource(R.drawable.toggle_on);
				GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.NEW_SAVED_SEARCH_MATCHES, "ON");
				boolNewSavedSearch = false;
				on_off = "1";
			} else {
				togNewSavedSearch.setBackgroundResource(R.drawable.toggle_off);
				GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.NEW_SAVED_SEARCH_MATCHES, "OFF");
				boolNewSavedSearch = true;
				on_off = "0";
			}
			callAlertSearchApi(on_off);
			break;
		case R.id.tog_update_results:
			if (boolUpdateResultOnMap) {
				togUpdateResultOnMap.setBackgroundResource(R.drawable.toggle_off);
				GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.UPDATE_RESULT_AS_MAP_MOVES, "OFF");
				boolUpdateResultOnMap = true;
			} else {
				togUpdateResultOnMap.setBackgroundResource(R.drawable.toggle_on);
				GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE,
						AppConstants.UPDATE_RESULT_AS_MAP_MOVES, "ON");
				boolUpdateResultOnMap = false;

			}
			break;
		case R.id.terms_lay:
			// moveToCommonActivity(getString(R.string.terms_of_use),
			// "content/tc");
			showTermsConditionsDialog(getString(R.string.terms_of_use), "content/tc");
			break;
		case R.id.privacy_lay:
			// moveToCommonActivity(getString(R.string.privacy_policy),
			// "content/pp");
			showTermsConditionsDialog(getString(R.string.privacy_policy), "content/pp");

			break;
		case R.id.about_lay:
			moveToCommonActivity(getString(R.string.about_renters_block), "content/arb");
			break;
		case R.id.copy_rights_lay:
			moveToCommonActivity(getString(R.string.copy_rights), "content/cp");
			break;

		}
	}

	private void moveToCommonActivity(String HeaderText, String mURL) {
		Intent intent = new Intent(SettingsActivity.this, SettingsCommonActivity.class);
		intent.putExtra("HEADERTXT", HeaderText);
		intent.putExtra("URL", mURL);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

	}

	public void showTermsConditionsDialog(String headertext, String url) {

		mDialog = new Dialog(this);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.activity_settings_common);

		mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		mDialog.setCancelable(true);
		LinearLayout mBack = (LinearLayout) mDialog.findViewById(R.id.back_icon);
		TextView mContentText = (TextView) mDialog.findViewById(R.id.content_txt);
		mContentText.setMovementMethod(new ScrollingMovementMethod());
		TextView mHeaderTxt = (TextView) mDialog.findViewById(R.id.header_txt);
		mHeaderTxt.setText(Html.fromHtml(headertext));

		getDetails(url, mContentText);
		mBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
				mDialog.dismiss();
			}
		});
		mDialog.show();
	}

	private void getDetails(String url, final TextView mContentText) {

		String URL = AppConstants.BASE_URL + url;

		GsonTransformer t = new GsonTransformer();
		aq().transformer(t).progress(DialogManager.getProgressDialog(this)).ajax(URL, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json, AjaxStatus status) {
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
								mContentText.setText(mResponse.getResult().toString());
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

	public static String html2text(String html) {
		return android.text.Html.fromHtml(html).toString();
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
