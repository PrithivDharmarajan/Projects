package com.smaat.jolt.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.TermsEntity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.DialogManager;

public class TermsAndConditions extends BaseFragment {
	private TermsEntity terms;
	private ScrollView mTermsScr;
	private WebView mTemsWebView;
	private String url;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.terms_conditions_fragment,
				container, false);
		setupUI(rootview);

		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());

		initComponents(view);
		// Api call
		// APIRequestHandler.getInstance().getTermsAndConditions(
		// GlobalMethods.getUserID(getActivity()), this);

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initComponents(View view) {

		 DialogManager.showProgress(getActivity());
		mTemsWebView = (WebView) view.findViewById(R.id.tems_condition);
		url = "file:///android_asset/jolt_terms.html";
		mTemsWebView.setInitialScale(1);
		mTemsWebView.setWebViewClient(new MyWebViewClient());
		mTemsWebView.getSettings().setJavaScriptEnabled(true);
		mTemsWebView.getSettings().setLoadWithOverviewMode(true);
		mTemsWebView.getSettings().setUseWideViewPort(true);
		mTemsWebView.getSettings().setMinimumFontSize(40);
		mTemsWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		mTemsWebView.setScrollbarFadingEnabled(false);
		mTemsWebView.loadUrl(url);

		mTemsWebView.clearCache(true);
		android.webkit.CookieManager.getInstance().removeAllCookie();
		
		
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
			DialogManager.hideProgress(getActivity());
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

			DialogManager.hideProgress(getActivity());
			stopWebview("0", getString(R.string.no_internet), "");
			super.onReceivedError(view, errorCode, description, failingUrl);

		}

	}

	private void stopWebview(String errorCode, String message, String result) {
		// TODO Auto-generated method stub

		mTemsWebView.stopLoading();
		mTemsWebView.setWebChromeClient(null);
		mTemsWebView.setWebViewClient(null);
		mTemsWebView.setVisibility(View.GONE);
		// gshowToast(getActivity(), message);
	}
}
