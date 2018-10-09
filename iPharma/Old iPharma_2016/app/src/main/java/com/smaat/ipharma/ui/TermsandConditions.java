package com.smaat.ipharma.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.entity.TermsConditionsEntity;
import com.smaat.ipharma.main.BaseActivity;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.TypefaceSingleton;

import retrofit.RetrofitError;

public class TermsandConditions extends BaseActivity implements
		DialogMangerCallback {

	Typeface helvetica_normal, helvetica_bold, helvetica_light, hightower;
	protected Object mRegId;
	private TextView header_txt;
	private Button mContinue_btn, mTermsCheck;
	private boolean isClicked = false;
	private WebView mTemsWebView;
	private String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_terms_conditions);
		helvetica_normal = TypefaceSingleton.getInstance().getHelvetica(this);
		helvetica_bold = TypefaceSingleton.getInstance().getHelveticaBold(this);
		helvetica_light = TypefaceSingleton.getInstance().getHelveticaLight(
				this);

		hightower = TypefaceSingleton.getInstance().getHighTower(this);
		ViewGroup mViewGroup = (ViewGroup) findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(this);
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		initview();
		// callTermsAndconditionsService();
	}

//	private void callTermsAndconditionsService() {
//
//		String Userid = GlobalMethods.getUserID(this);
//		APIRequestHandler.getInstance().getTermsandConditions(Userid, this);
//	}

	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof TermsConditionsEntity) {
			TermsConditionsEntity mTermsConditionsResponse = (TermsConditionsEntity) responseObj;
			if (mTermsConditionsResponse.getStatus().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
//				about_txt.setText(mTermsConditionsResponse.getResult().get(0)
//						.getDisclaimerText());
			}
		}
		super.onRequestSuccess(responseObj);
	}

	public void onRequestFailure(RetrofitError errorCode) {
		super.onRequestFailure(errorCode);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initview() {
		header_txt = (TextView) findViewById(R.id.header_text);
		header_txt.setTypeface(helvetica_bold);
		mContinue_btn = (Button) findViewById(R.id.continue_btn);
		mContinue_btn.setTypeface(helvetica_bold);
		mTermsCheck = (Button) findViewById(R.id.terms_check);
		DialogManager.showProgress(TermsandConditions.this);
		mTemsWebView = (WebView) findViewById(R.id.tems_condition);
		url = AppConstants.TERMS_LINK;
		mTemsWebView.setWebViewClient(new MyWebViewClient());
		mTemsWebView.getSettings().setJavaScriptEnabled(true);
		mTemsWebView.loadUrl(url);
		mTemsWebView.requestFocus();
		mTemsWebView.clearCache(true);
		android.webkit.CookieManager.getInstance().removeAllCookie();
		//
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			DialogManager.hideProgress(TermsandConditions.this);
			if (url.contains(getString(R.string.apirequestsuccess))) {
				// DialogManager.hideProgress(getActivity());
				Uri uri = Uri.parse(url);
				String errorCode = uri
						.getQueryParameter(getString(R.string.error_code));
				String message = uri
						.getQueryParameter(getString(R.string.message));
				String result = uri
						.getQueryParameter(getString(R.string.result));

				stopWebview(errorCode, message, result);

			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			DialogManager.hideProgress(TermsandConditions.this);
			stopWebview(AppConstants.FAILURE_CODE,
					getString(R.string.no_network), "");
			super.onReceivedError(view, errorCode, description, failingUrl);

		}

	}

	private void stopWebview(String errorCode, String message, String result) {
		// TODO Auto-generated method stub

		mTemsWebView.stopLoading();
		mTemsWebView.setWebChromeClient(null);
		mTemsWebView.setWebViewClient(null);
		mTemsWebView.setVisibility(View.GONE);
		// showToast(TermsandConditions.this, message);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.terms_check:
			if (isClicked) {
				isClicked = false;
				mTermsCheck.setBackgroundResource(R.drawable.ipharma_tick_icon);
				mContinue_btn
						.setBackgroundResource(R.drawable.ipahrma_login_button);
			} else {
				isClicked = true;
				mTermsCheck
						.setBackgroundResource(R.drawable.ipharma_tick_box_icon);
				mContinue_btn
						.setBackgroundResource(R.drawable.ipahrma_login_gray_button);
			}
			break;
		case R.id.continue_btn:
			if (isClicked) {

			} else {
				Intent in = new Intent(TermsandConditions.this,
						HomeScreen.class);
				startActivity(in);
				finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
			break;
		default:
			break;
		}
	}

	public void onItemclick(String SelctedItem, int pos) {

	}

	public void onOkclick() {

	}

}
