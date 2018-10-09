package com.smaat.renterblock.friends.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.adapter.SendFriendRequestAdapter;
import com.smaat.renterblock.friends.entity.SendRequestEntity;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.model.SendRequestModel;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.ProfileScreen;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;

public class FriendSearchScreen extends BaseActivity implements
		DialogMangerCallback {

	static EditText search_field;
	private Button clear_txt;
	private SendFriendRequestAdapter send_adapter;
	private ListView friends_list;
	private ArrayList<SendRequestEntity> send_req_ent;
	SendRequestEntity send_entity;
	static AQuery aq1;
	static Context mContext;
	private static String UserID;
	private static String mOkClick = "";

	Button btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.friend_send_request_screen);

		UserID = (String) GlobalMethods.getValueFromPreference(
				FriendSearchScreen.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);

		search_field = (EditText) findViewById(R.id.search_name);
		search_field.setTypeface(helvetica_bold);
		clear_txt = (Button) findViewById(R.id.clear_text);
		friends_list = (ListView) findViewById(R.id.friends_list);

		btnSearch = (Button) findViewById(R.id.search_icon);

		mContext = FriendSearchScreen.this;

		search_field.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				btnSearch.setVisibility(View.VISIBLE);
				clear_txt.setVisibility(View.VISIBLE);
				String keyword = v.getText().toString();
				callSearchFriendService(keyword);

				return false;
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String keyword = ((TextView) v).getText().toString();
				callSearchFriendService(keyword);
				btnSearch.setVisibility(View.GONE);
				clear_txt.setVisibility(View.GONE);
			}
		});

		search_field.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				clear_txt.setVisibility(View.VISIBLE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (search_field.getText().toString().equals("")) {
					clear_txt.setVisibility(View.GONE);
				} else {
					clear_txt.setVisibility(View.VISIBLE);
				}
			}
		});

		clear_txt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				search_field.setText("");
				clear_txt.setVisibility(View.GONE);
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bac_btn:
			if (AppConstants.from_profile_friends.equals("true")) {
				Intent inte = new Intent(FriendSearchScreen.this,
						ProfileScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				AppConstants.from_profile_friends = "false";
			} else {
				Intent inte = new Intent(FriendSearchScreen.this,
						FriendsMainScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;

		default:
			break;
		}

	}

	public static void callSendRequestService(String user_id) {
		String url = AppConstants.BASE_URL + "friend/sendfrindrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_user_id", user_id);

		aq1.transformer(t)
				.progress(DialogManager.getProgressDialog(mContext))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									FriendsResponse obj = new Gson().fromJson(
											json.toString(),
											FriendsResponse.class);
									onRequestSuccess(obj);
								} else {
									// statusErrorCodes(status);
								}
							}
						});
	}

	public static void onRequestSuccess(FriendsResponse response) {

		if (response.getError_code().equalsIgnoreCase("1")) {

			// showAlertDialog(response.getMsg(), mContext);
			mOkClick = "CallAPI";
			DialogManager.showCustomAlertDialog((FriendSearchScreen) mContext,
					(FriendSearchScreen) mContext, response.getMsg());
		}
	}

	public static void showAlertDialog(String message, final Context mContext2) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);

		alertDialogBuilder
				.setMessage("Request Sent Successfully.")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();

								((FriendSearchScreen) mContext2)
										.callSearchFriendService(search_field
												.getText().toString());

							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	private void callSearchFriendService(String keyword) {
		String url = AppConstants.BASE_URL + "friend/usersearch";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("search", keyword);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(this))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									SendRequestModel obj = new Gson().fromJson(
											json.toString(),
											SendRequestModel.class);
									onSuccessRequest(obj);
								} else {
									statusErrorCode(status);
								}
							}
						});

	}

	private void onSuccessRequest(SendRequestModel response) {

		if (response != null) {
			send_req_ent = new ArrayList<SendRequestEntity>();

			for (int i = 0; i < response.getResult().size(); i++) {
				send_entity = new SendRequestEntity();

				send_entity.setEmail_id(response.getResult().get(i)
						.getEmail_id());
				send_entity.setUser_name(response.getResult().get(i)
						.getUser_name());
				send_entity
						.setFriends(response.getResult().get(i).getFriends());
				send_entity.setLast_name(response.getResult().get(i)
						.getLast_name());
				send_entity.setPhotos(response.getResult().get(i).getPhotos());
				send_entity.setRating(response.getResult().get(i).getRating());
				send_entity.setReview(response.getResult().get(i).getReview());
				send_entity.setStatus(response.getResult().get(i).getStatus());
				send_entity
						.setUser_id(response.getResult().get(i).getUser_id());
				send_entity.setUser_pic(response.getResult().get(i)
						.getUser_pic());
				send_req_ent.add(send_entity);
			}
			send_adapter = new SendFriendRequestAdapter(
					FriendSearchScreen.this, R.layout.friends_list_adapter,
					send_req_ent);
			friends_list.setAdapter(send_adapter);
			send_adapter.notifyDataSetChanged();
		}

	}

	public AQuery aq() {
		if (aq1 == null) {
			aq1 = new AQuery(mContext);
		}
		return aq1;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (AppConstants.from_profile_friends.equals("true")) {
			finish();
			AppConstants.from_profile_friends = "false";
		} else {
			Intent inte = new Intent(FriendSearchScreen.this,
					FriendsMainScreen.class);
			startActivity(inte);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}

	@Override
	public void onItemclick(String SelctedItem, int pos) {

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub
		if (mOkClick.equalsIgnoreCase("CallAPI")) {

		} else {
			/**
			 * Close Dialog
			 */
		}

	}
}
