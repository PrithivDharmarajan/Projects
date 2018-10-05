package com.smaat.jolt.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.JoltPlansListEntity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class Checkout extends BaseFragment {

	private Button refill_img, btnBuy, editCard,btnCouponCode;
	boolean isrefillClick = false;
	public static JoltPlansListEntity joltPlans;
	private TextView includeTxt, noofdrinks, totalCost, cardNumber, total,
			activeCreditCard, refillAuto, termsConditions;
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.checkout, container, false);
		setupUI(rootview);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
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

		setdata();

	}

	private void initComponents(View view) {

		refill_img = (Button) view.findViewById(R.id.refill_img);

		btnCouponCode = (Button) view.findViewById(R.id.btnCouponCode);

		cardNumber = (TextView) view.findViewById(R.id.cardNumber);
		includeTxt = (TextView) view.findViewById(R.id.includeTxt);
		noofdrinks = (TextView) view.findViewById(R.id.noofdrinks);
		total = (TextView) view.findViewById(R.id.total);
		totalCost = (TextView) view.findViewById(R.id.totalCost);
		activeCreditCard = (TextView) view
				.findViewById(R.id.active_credit_card);
		refillAuto = (TextView) view.findViewById(R.id.refill_auto);
		termsConditions = (TextView) view.findViewById(R.id.Terms_Conditions);
		webView = (WebView) view.findViewById(R.id.webView);

		noofdrinks.setTypeface(HomeScreen.helveticaNeueBold);
		includeTxt.setTypeface(HomeScreen.helveticaNeueLight);
		total.setTypeface(HomeScreen.helveticaNeueBold);
		totalCost.setTypeface(HomeScreen.helveticaNeueMedium);
		activeCreditCard.setTypeface(HomeScreen.helveticaNeueBold);
		cardNumber.setTypeface(HomeScreen.helveticaNeueMedium);
		refillAuto.setTypeface(HomeScreen.helveticaNeueBold);
		termsConditions.setTypeface(HomeScreen.helveticaNeueLight);

		btnBuy = (Button) view.findViewById(R.id.btnBuy);
		btnBuy.setOnClickListener(new OnClickListener() {

			@SuppressLint("SetJavaScriptEnabled")
			@Override
			public void onClick(View v) {

				if (cardNumberValue.isEmpty()) {

					DialogManager.showToast(getActivity(),
							getString(R.string.add_card_alert));
				} else {
					String refilOn;
					if (isrefillClick) {
						refilOn = "1";
					} else {
						refilOn = "0";
					}

					String planId;

					if (JoltPlansFragment.planType
							.equalsIgnoreCase(AppConstants.ALL_DRINKS)) {
						planId = joltPlans.getAllDrinksPlansID();
					} else {
						planId = joltPlans.getClassicPlansID();
					}
					String url = AppConstants.PAYMENT_BASE_URL
							+ "PurchasePlan.php?" + "PlanID=" + planId
							+ "&PlanType=" + JoltPlansFragment.planType
							+ "&RefillON=" + refilOn + "&UserID="
							+ GlobalMethods.getUserID(getActivity());

					DialogManager.showProgress(getActivity());

					webView.setWebViewClient(new MyWebViewClient());
					webView.getSettings().setJavaScriptEnabled(true);
					webView.loadUrl(url);
					webView.requestFocus();
					webView.clearCache(true);
					android.webkit.CookieManager.getInstance()
							.removeAllCookie();
				}

			}
		});
		editCard = (Button) view.findViewById(R.id.editCard);
		editCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
				HomeScreen.moveBackFragment = new Checkout();
				((HomeScreen) getActivity()).updateDisplayFragment(
						new AddCreditCard(), true);

			}
		});

		refill_img.setBackgroundResource(R.drawable.puchase_uncheck);
		refill_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isrefillClick) {
					refill_img.setBackgroundResource(R.drawable.purchase_check);
					isrefillClick = true;
				} else {
					refill_img
							.setBackgroundResource(R.drawable.puchase_uncheck);
					isrefillClick = false;
				}
			}
		});

		btnCouponCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
				HomeScreen.moveBackFragment = new Checkout();
				((HomeScreen) getActivity()).updateDisplayFragment(
						new CouponCodeFragment(), true);

			}
		});

	}

	String cardNumberValue;

	private void setdata() {
		cardNumberValue = ((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.CARD_NUMBER)).trim();

		if (cardNumberValue.isEmpty()) {
			editCard.setText(getString(R.string.add));
			cardNumber.setText(getString(R.string.no_credit_card_associated));

		} else {

			editCard.setText(getString(R.string.edit));

			String card = getString(R.string.XXXX_XXXX)
					+ cardNumberValue.substring(cardNumberValue.length() - 4);
			cardNumber.setText(card);

		}

		noofdrinks.setText(joltPlans.getDrinks() + " "
				+ getString(R.string.drinks));

		totalCost.setText(" $" + joltPlans.getCost());

		String temp = joltPlans.getExpiration() + " "
				+ getString(R.string.expiration_txt) + ". "
				+ getString(R.string.tax_included) + ".\n"
				+ getString(R.string.include)
				+ joltPlans.getPlanIncludesDetails();

		includeTxt.setText(temp);
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
				String result = uri
						.getQueryParameter(getString(R.string.result));

				stopWebview(errorCode, message, result);

			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			stopWebview("0", getString(R.string.no_internet), "");
			super.onReceivedError(view, errorCode, description, failingUrl);

		}
	}

	private void stopWebview(String errorCode, String message, String result) {
		webView.stopLoading();
		webView.setWebChromeClient(null);
		webView.setWebViewClient(null);
		webView.setVisibility(View.GONE);

		if (errorCode.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE, AppConstants.AVAIL_DRINKS,
					result);
			DialogManager.showToast(getActivity(), message);
			HomeScreen.mHeaderLeft.setTag(HomeScreen.MAIN_SCREEN);
			((HomeScreen) getActivity()).showHomeScreenView();
			((HomeScreen) getActivity())
					.updateMenuToggleIcon(R.drawable.menu_btn);

		} else {
			DialogManager.showToast(getActivity(), message);
		}

	}
}
