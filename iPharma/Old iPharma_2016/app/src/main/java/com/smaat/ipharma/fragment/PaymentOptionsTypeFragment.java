package com.smaat.ipharma.fragment;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

public class PaymentOptionsTypeFragment extends BaseFragment implements
		OnClickListener {

	private EditText mCardNumber, mMonth, mYear, mCvv, mCardName;
	private Button mPayNow;
	private TextView mPurchaseAmount;
	private ImageView mCheckBox;
	private String mTempCurrentMoney;
	private boolean isClicked = false;
	private String cardNumber, month, year, cvv, cardname;
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(
				R.layout.fragment_payment_options_type, container, false);
		setupUI(rootview);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				getActivity());
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());
		initComponents(view);
		AppConstants.FRAG = AppConstants.PAYMENT_OPTION_SCREEN;
		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				HomeScreen.onBackMove(getActivity());

			}
		});
	}

	private void initComponents(View view) {
		mCardNumber = (EditText) view.findViewById(R.id.card_number);
		mCardNumber.addTextChangedListener(new FourDigitCardFormatWatcher());
		mMonth = (EditText) view.findViewById(R.id.mm);
		mYear = (EditText) view.findViewById(R.id.yyyy);
		mCvv = (EditText) view.findViewById(R.id.cvv);
		mCardName = (EditText) view.findViewById(R.id.name_on_the_card);
		mCheckBox = (ImageView) view.findViewById(R.id.check_box);
		mPayNow = (Button) view.findViewById(R.id.pay_now);
		mPayNow.setTypeface(HomeScreen.mHelveticaBold);
		mPurchaseAmount = (TextView) view.findViewById(R.id.purchase_amount);
		mTempCurrentMoney = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.IPHARMA_MONEY);
		if (mTempCurrentMoney.contains(getString(R.string.rupee_sym))) {
			mPurchaseAmount.setText(mTempCurrentMoney);
		} else {
			mPurchaseAmount.setText(getString(R.string.rupee_sym)
					+ mTempCurrentMoney);
		}
		webView = (WebView) view.findViewById(R.id.webView);
		setClickListener();
	}

	private void setClickListener() {
		mCheckBox.setOnClickListener(this);
		mPayNow.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_box:
			if (isClicked) {
				isClicked = false;
				mCheckBox.setImageResource(R.drawable.ipharma_tick_icon);
			} else {
				isClicked = true;
				mCheckBox.setImageResource(R.drawable.ipharma_tick_box_icon);
			}
			break;
		case R.id.pay_now:
			cardNumber = mCardNumber.getText().toString();

			month = mMonth.getText().toString().trim();
			year = mYear.getText().toString().trim();
			cvv = mCvv.getText().toString().trim();
			cardname = mCardName.getText().toString().trim();
			if (isClicked) {
				// Testing data
				AppConstants.from_my_order = AppConstants.FAILURE_CODE;
				HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				HomeScreen.mBottombar.setVisibility(View.GONE);
				HomeScreen.mHeaderLeft
						.setBackgroundResource(R.drawable.back_butto);
				((HomeScreen) getActivity()).replaceFragment(
						new IpharmaMoneyFragment(), false);
			} else {
				if (!isCreditCardValid(cardNumber)) {
					DialogManager.showCustomAlertDialog(getActivity(), this,
							getString(R.string.payment_failed),
							getString(R.string.enter_card_number));
				} else if (month.equals("")) {
					DialogManager.showCustomAlertDialog(getActivity(), this,
							getString(R.string.payment_failed),
							getString(R.string.enter_month));

				} else if (year.equals("")) {
					DialogManager.showCustomAlertDialog(getActivity(), this,
							getString(R.string.payment_failed),
							getString(R.string.enter_year));

				} else if (cvv.equals("") || cvv.length() < 3) {
					DialogManager.showCustomAlertDialog(getActivity(), this,
							getString(R.string.payment_failed),
							getString(R.string.enter_cvv));
				} else if (cardname.equals("")) {
					DialogManager.showCustomAlertDialog(getActivity(), this,
							getString(R.string.payment_failed),
							getString(R.string.enter_card_name));
				} else {

					cardNumber = cardNumber.replaceAll(" ", "");
					char first = month.charAt(0);
					char second = month.charAt(1);

					DialogManager.showProgress(getActivity());
					String url = AppConstants.PAYMENT_BASE_URL
							+ "AddCreditCard.php?" + "CardNumber="
							+ "4242424242424242" + "&CardExpiryMonth=" + first
							+ "&CardExpiryYear=" + second + "&CardCVV=" + cvv
							+ "&UserID="
							+ GlobalMethods.getUserID(getActivity());

					webView.setWebViewClient(new MyWebViewClient());
					webView.getSettings().setJavaScriptEnabled(true);
					webView.loadUrl(url);
					webView.requestFocus();
					webView.clearCache(true);
					android.webkit.CookieManager.getInstance()
							.removeAllCookie();
				}
			}
			break;
		default:
			break;
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

	@SuppressWarnings("unused")
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

			stopWebview("0", getString(R.string.no_network));
			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

	private void stopWebview(String errorCode, String message) {
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.setVisibility(View.GONE);

		if (errorCode.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

			// String[] month = month;
			// try {
			// GlobalMethods.updateCardDetails(getActivity(), cardNumber,
			// month[0], month[1], cvv);
			// } catch (Exception e) {
			// GlobalMethods.updateCardDetails(getActivity(), cardNumber,
			// "12", "2020", cvv);
			// }
			AppConstants.from_my_order = AppConstants.FAILURE_CODE;
			HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
			HomeScreen.mHeaderText.setText(R.string.ipharma_money);
			HomeScreen.mBottombar.setVisibility(View.GONE);
			HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
			((HomeScreen) getActivity()).replaceFragment(
					new IpharmaMoneyFragment(), true);

		} else {
			DialogManager.showCustomAlertDialog(getActivity(), this, message);
		}

	}
}
