package com.smaat.renterblock.myfavourite;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.adapter.MyBoardsListAdapter;
import com.smaat.renterblock.entity.BoardsChatModelEntity;
import com.smaat.renterblock.model.SendRequestModel;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.ui.LoginActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.GlobalMethods;
import com.smaat.renterblock.util.TypefaceSingleton;

public class BoardsChatActivity extends BaseActivity {

	private String UserID = "", propID = "";
	static Context mContext;
	private ImageView edit_view, slide_menu;
	private TextView header_txt;
	private ListView boards_chat_list;
	private ProgressBar show_progress;
	private MyBoardsListAdapter myBoardsListAdapter;
	private EditText chat_text;
	private RelativeLayout edit_lay;
	String chat_txt;
	private Button post_btn;
	private LinearLayout update_review_alert_lay;
	private String isEnhance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.boards_chat_screen);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_lay);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(root, mTypeface);
		setupUI(root);

		setGoogleAnalytics(BoardsChatActivity.this);
		UserID = (String) GlobalMethods.getValueFromPreference(
				BoardsChatActivity.this, GlobalMethods.STRING_PREFERENCE,
				AppConstants.pref_userId);
		mContext = BoardsChatActivity.this;
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			propID = extras.getString("property_id");
			isEnhance = extras.getString("isEnhance");
		}
		initializeViews();

		getMessageBoardConversation();
	}

	private void initializeViews() {
		edit_view = (ImageView) findViewById(R.id.filter);
		slide_menu = (ImageView) findViewById(R.id.slide);
		slide_menu.setImageResource(R.drawable.back_arrow_white);
		header_txt = (TextView) findViewById(R.id.how);
		header_txt.setText(getString(R.string.my_boards));
		header_txt.setTypeface(helvetica_bold);
		edit_view.setVisibility(View.INVISIBLE);
		boards_chat_list = (ListView) findViewById(R.id.boards_chat_list);
		show_progress = (ProgressBar) findViewById(R.id.show_progress);
		chat_text = (EditText) findViewById(R.id.enter_chat);
		edit_lay = (RelativeLayout) findViewById(R.id.edit_lay);

		update_review_alert_lay = (LinearLayout) findViewById(R.id.update_review_alert_lay);

		post_btn = (Button) findViewById(R.id.post_btn);
		post_btn.setTypeface(helvetica_bold);

		if (AppConstants.user_property.equals("0")) {
			if (isEnhance != null && isEnhance.equalsIgnoreCase("0")) {
				edit_lay.setVisibility(View.GONE);
				update_review_alert_lay.setVisibility(View.GONE);
			} else {
				edit_lay.setVisibility(View.VISIBLE);
			}

		} else if (AppConstants.user_property.equals("1")) {
			edit_lay.setVisibility(View.GONE);
			update_review_alert_lay.setVisibility(View.GONE);
		}
	}

	private void getMessageBoardConversation() {

		String Url = AppConstants.BASE_URL + "postboards/messageview";

		GsonTransformer t = new GsonTransformer();
		show_progress.setVisibility(View.VISIBLE);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("propertyId", propID);
		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						if (json != null) {
							BoardsChatModelEntity mResponse = new Gson()
									.fromJson(json.toString(),
											BoardsChatModelEntity.class);
							show_progress.setVisibility(View.GONE);
							onRequest(mResponse);
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	private void onRequest(BoardsChatModelEntity mResponse) {

		if (mResponse.getResult().size() == 0) {
			update_review_alert_lay.setVisibility(View.VISIBLE);
		} else {
			update_review_alert_lay.setVisibility(View.GONE);
		}

		myBoardsListAdapter = new MyBoardsListAdapter(BoardsChatActivity.this,
				R.layout.myboards_chat_adapter, mResponse.getResult());
		boards_chat_list.setAdapter(myBoardsListAdapter);
		myBoardsListAdapter.notifyDataSetChanged();

	}

	private void callPostMessageBoards() {
		String Url = AppConstants.BASE_URL + "postboards";

		GsonTransformer t = new GsonTransformer();
		show_progress.setVisibility(View.VISIBLE);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", UserID);
		params.put("property_id", propID);
		params.put("message", chat_txt);
		aq().transformer(t).ajax(Url, params, JSONObject.class,
				new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {

						if (json != null) {
							SendRequestModel mResponse = new Gson().fromJson(
									json.toString(), SendRequestModel.class);
							show_progress.setVisibility(View.GONE);
							update_review_alert_lay.setVisibility(View.GONE);
							onSuccessReq(mResponse);
						} else {
							statusErrorCode(status);
						}
					}
				});
	}

	private void onSuccessReq(SendRequestModel mResponse) {

		if (mResponse.getError_code().equals("1")) {
			chat_text.setText("");
			getMessageBoardConversation();
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.slide:
			finish();
			break;
		case R.id.post_btn:
			chat_txt = chat_text.getText().toString().trim();
			if (!chat_txt.equalsIgnoreCase("")) {
				callPostMessageBoards();
			}
			break;
		default:
			break;
		}
	}
}
