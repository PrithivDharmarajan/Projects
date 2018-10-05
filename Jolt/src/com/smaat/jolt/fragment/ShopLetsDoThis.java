package com.smaat.jolt.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.entity.ShopDetailsEntity;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;
import com.squareup.picasso.Picasso;

public class ShopLetsDoThis extends BaseFragment implements AnimationListener,
		OnClickListener {
	private LinearLayout mCoffeeAmtLay, mTipBtnLay;
	private Button oneButton, twoButton, threeButton, fourButton, fiveButton,
			sixButton, mBtnDolOne, mBtnDolTwo, mBtnDolThree, mBtnDolFour,
			mBtnDolFive;
	private ImageView mShopeLogo, slidePointer1, slidePointer2, slidePointer3,
			slidePointer4;
	private RelativeLayout mNumLay;
	private Animation mAnimBounce, mRevereseAnimBounce;
	private int numberCount = 0;
	private TextView mTxtDol, mNoOfDrinksCount, mDrinksType, mShopName,
			mShopStreet, mCureentTimeDate, mDrinksSize, mNoOfDrinks;

	private Bundle mBundle;
	public static String mCupCount, mChooseDrinks, mChooseDrinkSize;
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(
				R.layout.shope_details_lets_do_this_process, container, false);
		setupUI(rootview);

		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initComponents(view);

	}

	void initComponents(View view) {

		mShopeLogo = (ImageView) view.findViewById(R.id.shope_logo);

		mShopName = (TextView) view.findViewById(R.id.shop_name);
		mShopStreet = (TextView) view.findViewById(R.id.shop_street);
		mCureentTimeDate = (TextView) view.findViewById(R.id.cureent_time_date);
		mDrinksType = (TextView) view.findViewById(R.id.choose_drinks);
		mDrinksSize = (TextView) view.findViewById(R.id.drinks_size);
		mCureentTimeDate = (TextView) view.findViewById(R.id.cureent_time_date);
		mNoOfDrinks = (TextView) view.findViewById(R.id.No_of_drinks);
		mNoOfDrinksCount = (TextView) view
				.findViewById(R.id.No_of_drinks_count);
		mTxtDol = (TextView) view.findViewById(R.id.$_0);
		mTxtDol.setText(getString(R.string.$_0));

		mTipBtnLay = (LinearLayout) view.findViewById(R.id.tip_btn_lay);
		mCoffeeAmtLay = (LinearLayout) view.findViewById(R.id.coffee_amt_lay);
		mAnimBounce = AnimationUtils
				.loadAnimation(getActivity(), R.anim.bounce);
		mRevereseAnimBounce = AnimationUtils.loadAnimation(getActivity(),
				R.anim.reverse_bounce);
		mAnimBounce.setAnimationListener(this);
		mRevereseAnimBounce.setAnimationListener(this);

		mNumLay = (RelativeLayout) view.findViewById(R.id.num_lay);

		oneButton = (Button) view.findViewById(R.id.one);
		twoButton = (Button) view.findViewById(R.id.two);
		threeButton = (Button) view.findViewById(R.id.three);
		fourButton = (Button) view.findViewById(R.id.four);
		fiveButton = (Button) view.findViewById(R.id.five);
		sixButton = (Button) view.findViewById(R.id.six);
		mBtnDolOne = (Button) view.findViewById(R.id.$_00);
		mBtnDolTwo = (Button) view.findViewById(R.id.$_0_50);
		mBtnDolThree = (Button) view.findViewById(R.id.$_1);
		mBtnDolFour = (Button) view.findViewById(R.id.$_1_50);
		mBtnDolFive = (Button) view.findViewById(R.id.$_2);

		slidePointer1 = (ImageView) view.findViewById(R.id.slidepointer_one);
		slidePointer2 = (ImageView) view.findViewById(R.id.slidepointer_two);
		slidePointer3 = (ImageView) view.findViewById(R.id.slidepointer_three);
		slidePointer4 = (ImageView) view.findViewById(R.id.slidepointer_four);

		webView = (WebView) view.findViewById(R.id.webView);

		mShopName.setTypeface(HomeScreen.helveticaNeueBold);
		mShopStreet.setTypeface(HomeScreen.helveticaNeueLight);
		mDrinksType.setTypeface(HomeScreen.helveticaNeueBold);
		mDrinksSize.setTypeface(HomeScreen.helveticaNeueBold);
		mCureentTimeDate.setTypeface(HomeScreen.helveticaNeueLight);
		mNoOfDrinks.setTypeface(HomeScreen.helveticaNeueBold);
		mNoOfDrinksCount.setTypeface(HomeScreen.helveticaNeueLight);
		oneButton.setTypeface(HomeScreen.helveticaNeueBold);
		twoButton.setTypeface(HomeScreen.helveticaNeueBold);
		threeButton.setTypeface(HomeScreen.helveticaNeueBold);
		fourButton.setTypeface(HomeScreen.helveticaNeueBold);
		fiveButton.setTypeface(HomeScreen.helveticaNeueBold);
		sixButton.setTypeface(HomeScreen.helveticaNeueBold);
		mTxtDol.setTypeface(HomeScreen.helveticaNeueLight);
		mBtnDolOne.setTypeface(HomeScreen.helveticaNeueLight);
		mBtnDolTwo.setTypeface(HomeScreen.helveticaNeueLight);
		mBtnDolThree.setTypeface(HomeScreen.helveticaNeueLight);
		mBtnDolFour.setTypeface(HomeScreen.helveticaNeueLight);
		mBtnDolFive.setTypeface(HomeScreen.helveticaNeueLight);

		mCureentTimeDate.setText(GlobalMethods.getCurrentDate());

		mBundle = getArguments();
		if (mBundle != null) {
			mCupCount = mBundle.getString(AppConstants.POSITION);
			mChooseDrinks = mBundle.getString(AppConstants.CHOOSE_DRINKS);
			mChooseDrinkSize = mBundle
					.getString(AppConstants.CHOOSE_DRINK_SIZE);
			if (mChooseDrinkSize != null) {
				mDrinksSize.setText(mChooseDrinkSize);
			}
			if (mChooseDrinks != null) {
				mDrinksType.setText(mChooseDrinks);
			}
			if (mCupCount != null) {
				mNoOfDrinksCount.setText(mCupCount);
			}

		}
		setClickListener();
		setShopDetails();
	}

	ShopDetailsEntity shopDetails;

	private void setShopDetails() {

		shopDetails = AvailableDrinks.shopDetails;
		Picasso.with(getActivity())
				.load(AppConstants.BASE_TIMTHUMB
						+ shopDetails.getShopLogoImageUrl()
						+ "&q=200&zc=0&w=200").placeholder(
								getActivity().getResources().getDrawable(
										R.drawable.converstation_jolt_icon))
						.error(getActivity().getResources().getDrawable(
								R.drawable.converstation_jolt_icon)).into(mShopeLogo);

		mShopName.setText(shopDetails.getShopName());
		mShopStreet.setText(shopDetails.getShopStreet());

	}

	private void setClickListener() {
		mTipBtnLay.setOnClickListener(this);
		mShopeLogo.setOnClickListener(this);
		mNumLay.setOnClickListener(this);

		mBtnDolOne.setOnClickListener(this);
		mBtnDolTwo.setOnClickListener(this);
		mBtnDolThree.setOnClickListener(this);
		mBtnDolFour.setOnClickListener(this);
		mBtnDolFive.setOnClickListener(this);

		oneButton.setOnClickListener(this);
		twoButton.setOnClickListener(this);
		threeButton.setOnClickListener(this);
		fourButton.setOnClickListener(this);
		fiveButton.setOnClickListener(this);
		sixButton.setOnClickListener(this);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == mAnimBounce) {
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.num_lay:
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.tip_btn_lay:
			if (mCoffeeAmtLay.getVisibility() == View.VISIBLE) {
				mCoffeeAmtLay.startAnimation(mRevereseAnimBounce);
				mCoffeeAmtLay.setVisibility(View.GONE);
			} else {
				mCoffeeAmtLay.setVisibility(View.VISIBLE);
				mCoffeeAmtLay.startAnimation(mAnimBounce);
			}
			break;
		case R.id.$_00:
			mTxtDol.setText(getString(R.string.$_0));
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.$_0_50:
			mTxtDol.setText(getString(R.string.$_0_50));
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.$_1:
			mTxtDol.setText(getString(R.string.$_1));
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.$_1_50:
			mTxtDol.setText(getString(R.string.$_1_50));
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.$_2:
			mTxtDol.setText(getString(R.string.$_2));
			mCoffeeAmtLay.setVisibility(View.GONE);
			break;
		case R.id.one:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) oneButton.getTag());
			break;
		case R.id.two:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) twoButton.getTag());
			break;
		case R.id.three:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) threeButton.getTag());
			break;
		case R.id.four:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) fourButton.getTag());
			break;
		case R.id.five:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) fiveButton.getTag());
			break;
		case R.id.six:
			mCoffeeAmtLay.setVisibility(View.GONE);
			codeVerificationButtonClick((String) sixButton.getTag());
			break;

		default:
			break;
		}

	}

	String shopCode = "";

	private void codeVerificationButtonClick(String text) {

		numberCount++;

		shopCode = shopCode += text;

		switch (numberCount) {
		case 1:
			slidePointer1.setBackgroundResource(R.drawable.keypad_selection_on);
			break;
		case 2:
			slidePointer2.setBackgroundResource(R.drawable.keypad_selection_on);
			break;
		case 3:
			slidePointer3.setBackgroundResource(R.drawable.keypad_selection_on);
			break;
		case 4:
			slidePointer4.setBackgroundResource(R.drawable.keypad_selection_on);
			break;
		default:
			break;
		}

		// Maximum code number limit is 4;
		if (numberCount == 4) {

			makePurchase(shopCode);
			slidePointer1
					.setBackgroundResource(R.drawable.keypad_selection_off);
			slidePointer2
					.setBackgroundResource(R.drawable.keypad_selection_off);
			slidePointer3
					.setBackgroundResource(R.drawable.keypad_selection_off);
			slidePointer4
					.setBackgroundResource(R.drawable.keypad_selection_off);
			shopCode = "";
			numberCount = 0;
		}
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

			HomeScreen.mHeaderCup.setText(((HomeScreen) getActivity())
					.countText());
			HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);

			((HomeScreen) getActivity()).mFragment = new CoffeWaitFragment();
			((HomeScreen) getActivity()).replaceFragment(
					((HomeScreen) getActivity()).mFragment, true);

		} else {
			DialogManager.showToast(getActivity(), message);
		}

	}

	private void makePurchase(String shopCode) {

		String cardNumberValue = ((String) GlobalMethods
				.getValueFromPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.CARD_NUMBER)).trim();
		if (cardNumberValue.isEmpty()) {

			DialogManager.showToast(getActivity(),
					getString(R.string.purchase_plan_alert));
		} else {
			DialogManager.showProgress(getActivity());
			String tip = mTxtDol.getText().toString().replace("$", "");

			int tipAmount = (int) (Float.parseFloat(tip) * 100);
			String url = AppConstants.PAYMENT_BASE_URL + "purchasedrink.php?"
					+ "NoOfDrinks=" + mCupCount + "&ShopID="
					+ shopDetails.getShopId() + "&SelectedDrinks="
					+ mChooseDrinks.replaceAll(" ", "%20")
					+ "&SelectedDrinksSize="
					+ mChooseDrinkSize.replaceAll(" ", "%20") + "&UserID="
					+ GlobalMethods.getUserID(getActivity()) + "&ShopCode="
					+ shopCode + "&TipAmount=" + tipAmount;
			webView.setWebViewClient(new MyWebViewClient());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.loadUrl(url);
			webView.requestFocus();
			webView.clearCache(true);
			
			System.out.println("Url:"+ url);
			android.webkit.CookieManager.getInstance().removeAllCookie();
		}

	}

}
