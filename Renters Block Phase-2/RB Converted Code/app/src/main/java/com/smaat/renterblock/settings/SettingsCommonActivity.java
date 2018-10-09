package com.smaat.renterblock.settings;

import org.json.JSONObject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;
import com.smaat.renterblock.R;
import com.smaat.renterblock.model.CommonResponse;
import com.smaat.renterblock.ui.BaseActivity;
import com.smaat.renterblock.util.AppConstants;
import com.smaat.renterblock.util.DialogManager;
import com.smaat.renterblock.util.TypefaceSingleton;

public class SettingsCommonActivity extends BaseActivity implements
		OnClickListener {
	private Bundle mBundle;
	private TextView mHeaderTxt, mContentText;
	private Typeface mTypefaceBold;
	private String mURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_common);
		ViewGroup root = (ViewGroup) findViewById(R.id.parent_layout);
		mTypefaceBold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		setFont(root, mTypefaceBold);
		setupUI(root);
		initComponents();
	}

	private void initComponents() {

		mHeaderTxt = (TextView) findViewById(R.id.header_txt);
		mContentText = (TextView) findViewById(R.id.content_txt);
		mContentText.setMovementMethod(new ScrollingMovementMethod());
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mHeaderTxt.setText(mBundle.getString("HEADERTXT"));
			mURL = mBundle.getString("URL");
			if (mURL != null) {
				getDetails(mURL);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_icon:
			finish();
			overridePendingTransition(R.anim.slide_out_right,
					R.anim.slide_in_left);
			break;
		}
	}

	private void getDetails(String url) {

		String URL = AppConstants.BASE_URL + url;

		GsonTransformer t = new GsonTransformer();
		aq().transformer(t).progress(DialogManager.getProgressDialog(this))
				.ajax(URL, JSONObject.class, new AjaxCallback<JSONObject>() {

					@Override
					public void callback(String url, JSONObject json,
							AjaxStatus status) {
						if (json != null) {
							CommonResponse mResponse = new Gson().fromJson(
									json.toString(), CommonResponse.class);
							if (mResponse.getError_code().equalsIgnoreCase(
									AppConstants.SUCCESS_CODE)) {
								mContentText.setText(mResponse.getResult()
										.toString());
							}
						} else {
							statusErrorCode(status);
						}
					}

				});

	}

}
