package com.smaat.ipharma.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;

public class AboutFragment extends BaseFragment implements DialogMangerCallback {
	private WebView mAboutWebView;
	private String url;

	// private String UserID;
	// private ProgressDialog progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_about, container,
				false);
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
		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// if (((String) GlobalMethods.getValueFromPreference(
				// getActivity(), GlobalMethods.STRING_PREFERENCE,
				// AppConstants.WEB_SCREEN))
				// .equalsIgnoreCase(AppConstants.MORE_SCREEN)) {
				// HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				// HomeScreen.mHeaderText.setText(R.string.more);
				// HomeScreen.mBottombar.setVisibility(View.GONE);
				// HomeScreen.mHeaderLeft
				// .setBackgroundResource(R.drawable.back_butto);
				//
				// ((HomeScreen) getActivity())
				// .replaceFragment(new MoreFragment());
				// }
				//
				// if (((String) GlobalMethods.getValueFromPreference(
				// getActivity(), GlobalMethods.STRING_PREFERENCE,
				// AppConstants.WEB_SCREEN))
				// .equalsIgnoreCase(AppConstants.HOME_SCREEN)) {
				// HomeScreen.onBackMove(getActivity());
				// }
				HomeScreen.onBackMove(getActivity());
			}
		});

		initComponents(view);
		// callAbounUsService();

	}

//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.remove(this).commit();
//	}

	private void initComponents(View view) {
		DialogManager.showProgress(getActivity());
		mAboutWebView = (WebView) view.findViewById(R.id.about);
		if (((String) GlobalMethods.getValueFromPreference(getActivity(),
				GlobalMethods.STRING_PREFERENCE, AppConstants.WEB_SCREEN))
				.equalsIgnoreCase(AppConstants.MORE_SCREEN)) {
			AppConstants.FRAG = AppConstants.MORESCREEN;
			url = AppConstants.ABOUT_LINK;

		}
		if (((String) GlobalMethods.getValueFromPreference(getActivity(),
				GlobalMethods.STRING_PREFERENCE, AppConstants.WEB_SCREEN))
				.equalsIgnoreCase(AppConstants.HOME_SCREEN))

		{

			AppConstants.FRAG = AppConstants.MAP_SCREEN;
			url = AppConstants.DISCLAIMER_LINK;
		}
		mAboutWebView.setWebViewClient(new MyWebViewClient());
		mAboutWebView.getSettings().setJavaScriptEnabled(true);
		mAboutWebView.loadUrl(url);
		mAboutWebView.requestFocus();
		mAboutWebView.clearCache(true);
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
			stopWebview("0", getString(R.string.no_network), "");
			super.onReceivedError(view, errorCode, description, failingUrl);

		}

	}

	private void stopWebview(String errorCode, String message, String result) {
		// TODO Auto-generated method stub

		mAboutWebView.stopLoading();
		mAboutWebView.setWebChromeClient(null);
		mAboutWebView.setWebViewClient(null);
		mAboutWebView.setVisibility(View.GONE);
		showToast(getActivity(), message);
	}

	// private void callAbounUsService() {
	// // TODO Auto-generated method stub
	// RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
	// AppConstants.Base_Url).build();
	// APICommonInterface interfaces = restAdapter
	// .create(APICommonInterface.class);
	//
	// interfaces.getAboutUs(UserID, new Callback<AboutUsResponse>() {
	//
	// public void failure(RetrofitError arg0) {
	// if (progress != null) {
	// progress.dismiss();
	// }
	// DialogManager.showCustomAlertDialog(getActivity(),
	// AboutFragment.this,
	// "Please Check your Intenet Connection.");
	// }
	//
	// public void success(AboutUsResponse response, Response obj) {
	// if (response.getStatus().equalsIgnoreCase("1")) {
	// // mAboutWebView.setText(response.getResult().get(0).getAboutsUsText());
	// if (progress != null) {
	// progress.dismiss();
	// }
	// } else {
	// if (progress != null) {
	// progress.dismiss();
	// }
	// }
	// }
	//
	// });
	//
	// }

	@Override
	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
