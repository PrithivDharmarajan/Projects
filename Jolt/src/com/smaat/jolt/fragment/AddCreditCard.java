package com.smaat.jolt.fragment;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.smaat.jolt.R;
import com.smaat.jolt.model.AddCreditCardResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.DialogMangerCallback;
import com.smaat.jolt.util.GlobalMethods;

public class AddCreditCard extends BaseFragment implements OnClickListener,
		DialogMangerCallback {

	private Button mAddCardBtn;
	private static EditText mCardsNumberEditTxt, mMmYyEditTxt, mCvvEditTxt;
	Fragment mFragment;
	String CardNo;
	private String cardNumber, monthYear, cvv;
	private WebView webView;
	@SuppressWarnings("unused")
	private int month, year;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.enter_credit_card_details,
				container, false);
		setupUI(rootview);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view.findViewById(R.id.Parent_lay);
		hideSoftKeyboard(getActivity());
		setupUI(mViewGroup);

		initComponents(view);
	}

	private void initComponents(View view) {
		mAddCardBtn = (Button) view.findViewById(R.id.continue_txt);
		mCardsNumberEditTxt = (EditText) view
				.findViewById(R.id.cards_number_edit_txt);

		mCardsNumberEditTxt
				.addTextChangedListener(new FourDigitCardFormatWatcher());
		mMmYyEditTxt = (EditText) view.findViewById(R.id.mm_yy_edit_txt);
		mMmYyEditTxt.addTextChangedListener(new MonthYearValidation());

		mCvvEditTxt = (EditText) view.findViewById(R.id.cvv_edit_txt);

		webView = (WebView) view.findViewById(R.id.webView);
		setClickListener();

		setData();
		getCurrentMonthYear();
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

			if (url.contains(getString(R.string.apirequestsuccess))) {
				DialogManager.hideProgress(getActivity());
				Uri uri = Uri.parse(url);
				String errorCode = uri
						.getQueryParameter(getString(R.string.error_code));
				String message = uri
						.getQueryParameter(getString(R.string.message));

				stopWebview(errorCode, message);

			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			stopWebview("0", getString(R.string.no_internet));
			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

	private void stopWebview(String errorCode, String message) {
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.setVisibility(View.GONE);

		if (errorCode.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

			String[] month = monthYear.split("/");
			try {
				GlobalMethods.updateCardDetails(getActivity(), cardNumber,
						month[0], month[1], cvv);
			} catch (Exception e) {
				GlobalMethods.updateCardDetails(getActivity(), cardNumber,
						"12", "2020", cvv);
			}

			if (HomeScreen.moveBackFragment instanceof ProfileFragment) {
				HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
				((HomeScreen) getActivity()).updateDisplayFragment(
						new ProfileFragment(), false);
			}
			if (HomeScreen.moveBackFragment instanceof AvailableDrinks) {
				HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
				((HomeScreen) getActivity()).updateDisplayFragment(
						new AvailableDrinks(), false);
			}

			if (HomeScreen.moveBackFragment instanceof MapScreenFragment) {
				HomeScreen.mHeaderLeft.setTag(HomeScreen.MAIN_SCREEN);
				((HomeScreen) getActivity()).showHomeScreenView();
			}

		} else {
			DialogManager.showToast(getActivity(), message);
		}

	}

	private void getCurrentMonthYear() {
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);

	}

	private void setData() {
		String cardNumberValue = ((String) GlobalMethods
				.getValueFromPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.CARD_NUMBER)).trim();

		if (cardNumberValue.isEmpty()) {
			mAddCardBtn.setText(getString(R.string.add_card));

		} else {
			String month = ((String) GlobalMethods.getValueFromPreference(
					getActivity(), GlobalMethods.STRING_PREFERENCE,
					AppConstants.CARD_EXPIRY_MONTH)).trim();

			String year = ((String) GlobalMethods.getValueFromPreference(
					getActivity(), GlobalMethods.STRING_PREFERENCE,
					AppConstants.CARD_EXPIRY_YEAR)).trim();
			String cvv = ((String) GlobalMethods.getValueFromPreference(
					getActivity(), GlobalMethods.STRING_PREFERENCE,
					AppConstants.CARD_CVV)).trim();
			mAddCardBtn.setText(getString(R.string.update_card));

			mCardsNumberEditTxt.setText(cardNumberValue);
			mMmYyEditTxt.setText(month + "/" + year);
			mCvvEditTxt.setText(cvv);
		}
	}

	public void setClickListener() {
		mAddCardBtn.setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.continue_txt:
			hideSoftKeyboard(getActivity());
			cardNumber = mCardsNumberEditTxt.getText().toString();

			monthYear = mMmYyEditTxt.getText().toString();
			cvv = mCvvEditTxt.getText().toString();

			if (!isCreditCardValid(cardNumber)) {
				DialogManager.showToast(getActivity(),
						getString(R.string.enter_card_number));
			} else if (monthYear.equals("")) {
				DialogManager.showToast(getActivity(),
						getString(R.string.enter_month));

			} else if (cvv.equals("") || cvv.length() < 3) {
				DialogManager.showToast(getActivity(),
						getString(R.string.enter_cvv));
			} else {

				cardNumber = cardNumber.replaceAll(" ", "");
				String[] month = monthYear.split("/");

				DialogManager.showProgress(getActivity());
				String url = AppConstants.PAYMENT_BASE_URL
						+ "AddCreditCard.php?" + "CardNumber="
						+ "4242424242424242" + "&CardExpiryMonth=" + month[0]
						+ "&CardExpiryYear=" + month[1] + "&CardCVV=" + cvv
						+ "&UserID=" + GlobalMethods.getUserID(getActivity());

				webView.setWebViewClient(new MyWebViewClient());
				webView.getSettings().setJavaScriptEnabled(true);
				webView.loadUrl(url);
				webView.requestFocus();
				webView.clearCache(true);
				android.webkit.CookieManager.getInstance().removeAllCookie();
			}
		}

	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof AddCreditCardResponse) {
		}
	}

	public static class FourDigitCardFormatWatcher implements TextWatcher {

		// Change this to what you want... ' ', '-' etc..
		private static final char space = ' ';

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// Remove spacing char
			if (s.length() > 0 && (s.length() % 5) == 0) {
				final char c = s.charAt(s.length() - 1);
				if (space == c) {
					s.delete(s.length() - 1, s.length());
				}
			}
			// Insert char where needed.
			if (s.length() > 0 && (s.length() % 5) == 0) {
				char c = s.charAt(s.length() - 1);
				// Only if its a digit where there should be a space we insert a
				// space
				if (Character.isDigit(c)
						&& TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
					s.insert(s.length() - 1, String.valueOf(space));
				}
			}
		}
	}

	static String mLastInput = "";

	public static class MonthYearValidation implements TextWatcher {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			String input = s.toString();

			if (s.length() == 2 && !mLastInput.endsWith("/")) {
				int month = Integer.parseInt(input);

				if (month <= 12) {
					mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
							+ "/");
					mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
							.length());
				} else {
					mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
							.substring(0, s.length() - 1));
					mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
							.length());
				}
			} else if (s.length() == 2 && mLastInput.endsWith("/")) {
				int month = Integer.parseInt(input);
				if (month <= 12) {
					mMmYyEditTxt.setText(mMmYyEditTxt.getText().toString()
							.substring(0, 1));
					mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
							.length());
				} else {
					mMmYyEditTxt.setText("");
					mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
							.length());

				}
			} else if (s.length() == 1) {
				int month = Integer.parseInt(input);
				if (month > 1) {
					mMmYyEditTxt.setText("0"
							+ mMmYyEditTxt.getText().toString() + "/");
					mMmYyEditTxt.setSelection(mMmYyEditTxt.getText().toString()
							.length());
				}
			} else {

			}
			mLastInput = mMmYyEditTxt.getText().toString();
			return;

		}
	}

	public static boolean isCreditCardValid(String cardNumber) {
		String digitsOnly = cardNumber.replaceAll(" ", "");
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;

		if (digitsOnly.length() < 16) {
			return false;
		}
		for (int i = digitsOnly.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(digitsOnly.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}

		int modulus = sum % 10;
		return modulus == 0;

	}

	@Override
	public void onOkclick() {

	}

}
