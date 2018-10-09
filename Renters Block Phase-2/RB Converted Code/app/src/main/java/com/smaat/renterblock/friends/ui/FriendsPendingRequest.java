package com.smaat.renterblock.friends.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.MapFragmentActivity;
import com.smaat.renterblock.R;
import com.smaat.renterblock.friends.adapter.FriendPendingRequestAdapter;
import com.smaat.renterblock.friends.entity.FriendDetailsEntity;
import com.smaat.renterblock.model.FriendsResponse;
import com.smaat.renterblock.model.PendingRequestResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.DialogMangerCallback;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class FriendsPendingRequest extends BaseActivity implements
		DialogMangerCallback {

	static FriendPendingRequestAdapter recentadapter;
	private ListView friends_list;
	private ArrayList<FriendDetailsEntity> fre_ent;
	FriendDetailsEntity fr_entity;
	static AQuery aq1;
	static Context mContext;
	private static String UserID;
	private TextView mHeaderTxt;
	private String mCallFrom = null;
	private Bundle mBundle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_pending_request_main_screen);

		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		Typeface mTypefaceBold = TypefaceSingleton.getInstance()
				.getHelveticaBold(this);
		setFont(root, mTypeface);
		setupUI(root);
		friends_list = (ListView) findViewById(R.id.friend_req_list);
		mContext = FriendsPendingRequest.this;

		UserID = (String) GlobalMethods.getValueFromPreference(
				FriendsPendingRequest.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);
		mHeaderTxt = (TextView) findViewById(R.id.header_txt);
		mHeaderTxt.setText("Friends");
		mHeaderTxt.setTypeface(mTypefaceBold);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mCallFrom = mBundle.getString("CallFrom");
			if (mCallFrom == null) {
				mCallFrom = "";
			}
		}
		getPendingFriendList();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
				Intent inte = new Intent(FriendsPendingRequest.this,
						FriendsMainScreen.class);
				startActivity(inte);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
				finish();
				AppConstants.push_notification_call = "false";
			}
			// if (mCallFrom != null && mCallFrom.equalsIgnoreCase("GCM")) {
			// launchActivity(MapFragmentActivity.class);
			// overridePendingTransition(R.anim.slide_out_right,
			// R.anim.slide_in_left);
			// }
			else {
				AppConstants.isAPI = true;
				finish();
				overridePendingTransition(R.anim.slide_out_right,
						R.anim.slide_in_left);
			}

			break;

		default:
			break;
		}
	}

	private void getPendingFriendList() {

		String url = AppConstants.BASE_URL + "friend/friendsrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);

		aq().transformer(t)
				.progress(DialogManager.getProgressDialog(mContext))
				.ajax(url, params, JSONObject.class,
						new AjaxCallback<JSONObject>() {

							@Override
							public void callback(String url, JSONObject json,
									AjaxStatus status) {

								if (json != null) {
									PendingRequestResponse obj = new Gson().fromJson(
											json.toString(),
											PendingRequestResponse.class);
									onSuccessRequest(obj);
								} else {
									statusErrorCode(status);
								}

							}
						});
	}

	private void onSuccessRequest(PendingRequestResponse response) {
		if (response != null) {
			fre_ent = new ArrayList<FriendDetailsEntity>();
			for (int i = 0; i < response.getResult().size(); i++) {
				if (response.getResult().get(i).getFriends_details() != null
						&& !response.getResult().get(i).getFriends_details()
								.isEmpty()) {
					fr_entity = new FriendDetailsEntity();
					fr_entity.setUser_name(response.getResult().get(i)
							.getFriends_details().get(0).getUser_name());
					fr_entity.setFriends(response.getResult().get(i)
							.getFriends_details().get(0).getFriends());
					fr_entity.setIsnew(response.getResult().get(i)
							.getFriends_details().get(0).getIsnew());
					fr_entity.setLast_name(response.getResult().get(i)
							.getFriends_details().get(0).getLast_name());
					fr_entity.setPhotos(response.getResult().get(i)
							.getFriends_details().get(0).getPhotos());
					fr_entity.setRating(response.getResult().get(i)
							.getFriends_details().get(0).getRating());
					fr_entity.setReview(response.getResult().get(i)
							.getFriends_details().get(0).getReview());
					fr_entity.setStatus(response.getResult().get(i)
							.getFriends_details().get(0).getStatus());
					fr_entity.setUser_friend_id(response.getResult().get(i)
							.getFriends_details().get(0).getUser_friend_id());
					fr_entity.setUser_pic(response.getResult().get(i)
							.getFriends_details().get(0).getUser_pic());
					fre_ent.add(fr_entity);
				}
			}

			recentadapter = new FriendPendingRequestAdapter(
					FriendsPendingRequest.this,
					R.layout.pending_friend_list_adapter, fre_ent);
			friends_list.setAdapter(recentadapter);
			recentadapter.notifyDataSetChanged();
		}
	}

	public static void acceptFriendRequest(String friend_user_id) {
		String url = AppConstants.BASE_URL + "friend/acceptfrindrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_user_id", friend_user_id);

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

		DialogManager.showCustomAlertDialog((FriendsPendingRequest) mContext,
				(FriendsPendingRequest) mContext, response.getMsg());
	}

	@Override
	public void onOkclick() {
		AppConstants.isAPI = true;
		finish();
		overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
	}

	public static void showAlertDialog(String message, final Context mContext2) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mContext);

		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								((FriendsPendingRequest) mContext2)
										.getPendingFriendList();
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public static void rejectFriendRequest(String friend_user_id) {
		String url = AppConstants.BASE_URL + "friend/rejectfrindrequest";
		GsonTransformer t = new GsonTransformer();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("friend_user_id", friend_user_id);

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
		if (AppConstants.push_notification_call.equalsIgnoreCase("true")) {
			Intent inte = new Intent(FriendsPendingRequest.this,
					FriendsMainScreen.class);
			startActivity(inte);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
			finish();
			AppConstants.push_notification_call = "false";
		} else {
			AppConstants.isAPI = true;
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
		}
	}
}
