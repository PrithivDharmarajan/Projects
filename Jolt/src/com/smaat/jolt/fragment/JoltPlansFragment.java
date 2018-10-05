package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.JoltPlansListEntity;
import com.smaat.jolt.model.JoltPlansResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.CustomViewPager;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class JoltPlansFragment extends BaseFragment implements OnClickListener {

	private TextView mWhatIncludedTxt, mTopText, mChoose, drinksCount,
			planCost, costPerCup, expiration, taxIncluded;
	private LinearLayout mViewLayout, mOptionsLayo, mWatLay;
	private View mDrinks, mBasics;
	private CustomViewPager pager;
	boolean isBasicFirst = true;
	Animation animMove, animMove1, animmovelay;
	HorizontalScrollView mHSV;
	private Button btnInclude;
	private int deviceHeight = 0, density = 0, headerHeight = 0;
	private ArrayList<JoltPlansListEntity> classicPlansList,
			allDrinksPlansList = new ArrayList<JoltPlansListEntity>();
	public static String classicPlansIncludesInfoDescription = "",
			allDrinksIncludesInfoDescription = "", planType, device;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.slide_jolt_plans, container,
				false);
		setupUI(rootview);

		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.jolt_plans_lay);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());

		initComponents(view);

	}

	private void initComponents(View view) {
		mWhatIncludedTxt = (TextView) view.findViewById(R.id.what_included_txt);
		mViewLayout = (LinearLayout) view.findViewById(R.id.view_layout);
		mTopText = (TextView) view.findViewById(R.id.top_text);
		mBasics = (TextView) view.findViewById(R.id.basics);
		mDrinks = (TextView) view.findViewById(R.id.drinks);
		mWatLay = (LinearLayout) view.findViewById(R.id.what_lay);
		mOptionsLayo = (LinearLayout) view.findViewById(R.id.edit_lay);

		btnInclude = (Button) view.findViewById(R.id.btnInclude);

		mTopText.setTypeface(HomeScreen.kGBlankSpace);
		mWhatIncludedTxt.setTypeface(HomeScreen.kGBlankSpace);

		 ((TextView) mBasics).setTypeface(HomeScreen.helveticaNeueBold);
		 ((TextView) mDrinks).setTypeface(HomeScreen.helveticaNeueBold);

		pager = (CustomViewPager) view.findViewById(R.id.pager);

		getDeviceResolution();

		setClickListener();

		APIRequestHandler.getInstance().getJoltPlans(
				GlobalMethods.getUserID(getActivity()), this);

	}

	public void setClickListener() {
		mWhatIncludedTxt.setOnClickListener(this);
		mBasics.setOnClickListener(this);
		mDrinks.setOnClickListener(this);
		btnInclude.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnInclude:
			HomeScreen.mHeaderText.setText(R.string.whats_included_txt);
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);

			HomeScreen.moveBackFragment = new JoltPlansFragment();
			((HomeScreen) getActivity()).mFragment = new JoltPlansWhatsIncluded();
			((HomeScreen) getActivity()).replaceFragment(
					((HomeScreen) getActivity()).mFragment, true);

			break;

		case R.id.what_included_txt:
			HomeScreen.mHeaderRight.setVisibility(View.INVISIBLE);
			HomeScreen.mHeaderCup.setVisibility(View.GONE);
			HomeScreen.mHeaderText.setText(R.string.whats_included_txt);
			HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
			HomeScreen.moveBackFragment = new JoltPlansFragment();
			((HomeScreen) getActivity()).mFragment = new JoltPlansWhatsIncluded();
			((HomeScreen) getActivity()).replaceFragment(
					((HomeScreen) getActivity()).mFragment, true);

			break;

		case R.id.basics:
			basics();
			break;

		case R.id.drinks:
			drinks();
			break;
		}
	}

	public void basics() {
		mBasics.setBackgroundResource(R.drawable.classics_over);
		mDrinks.setBackgroundResource(R.drawable.classics_normal);
		mTopText.setVisibility(View.GONE);
		ShowListAdapter(classicPlansList);
		if (isBasicFirst) {
			SlideToAbove();
			isBasicFirst = false;
		} else {
			isBasicFirst = false;
		}

		planType = AppConstants.CLASSIC_DRINKS;
	}

	public void drinks() {
		mDrinks.setBackgroundResource(R.drawable.classics_over);
		mBasics.setBackgroundResource(R.drawable.classics_normal);
		mTopText.setVisibility(View.GONE);
		ShowListAdapter(allDrinksPlansList);
		if (isBasicFirst) {
			SlideToAbove();
			isBasicFirst = false;
		} else {

			isBasicFirst = false;
		}

		planType = AppConstants.ALL_DRINKS;
	}

	public void SlideToAbove() {
		TranslateAnimation mAnimation = null, mAnimation1 = null;

		mAnimation = new TranslateAnimation(0, 0, 0, -deviceHeight
				+ mWatLay.getHeight());
		mAnimation.setDuration(500);
		mAnimation.setFillAfter(true);
		mAnimation.setFillEnabled(true);

		mAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mWatLay.clearAnimation();
				RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(
						mWatLay.getWidth(), mWatLay.getHeight());

				RelativeLayout mContainer = (RelativeLayout) mWatLay
						.getParent();
				mContainer.setGravity(Gravity.TOP | Gravity.CENTER);
				mContainer.setPadding(0, 20, 0, 0);
				mParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				mWatLay.setLayoutParams(mParams);
				mViewLayout.setVisibility(View.VISIBLE);

			}

		});
		mWatLay.startAnimation(mAnimation);

		mAnimation1 = new TranslateAnimation(0, 0, 0, -deviceHeight
				+ mWatLay.getHeight() + mOptionsLayo.getHeight());
		mAnimation1.setDuration(500);
		mAnimation1.setFillAfter(true);
		mAnimation1.setFillEnabled(true);

		mAnimation1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {

				mOptionsLayo.clearAnimation();

				RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(
						mOptionsLayo.getWidth(), mOptionsLayo.getHeight());
				RelativeLayout mContainer = (RelativeLayout) mOptionsLayo
						.getParent();
				mContainer.setGravity(Gravity.TOP | Gravity.CENTER);
				mParams.setMargins(0, 20, 0, 0);
				mParams.addRule(RelativeLayout.BELOW, mWatLay.getId());
				mOptionsLayo.setLayoutParams(mParams);

				mViewLayout.setVisibility(View.VISIBLE);

			}

		});
		mOptionsLayo.startAnimation(mAnimation1);
	}

	public void ShowListAdapter(ArrayList<JoltPlansListEntity> plansList) {

		pager.setAdapter(new ImagePagerAdapter(plansList));
		DetailOnPageChangeListener listener = new DetailOnPageChangeListener();
		pager.setOnPageChangeListener(listener);
		if (plansList.size() > 0) {
			pager.setPagingEnabled(plansList.size() > 1);
		}

	}

	class DetailOnPageChangeListener extends
			ViewPager.SimpleOnPageChangeListener {
		private int currentPage;

		@Override
		public void onPageSelected(int position) {
			currentPage = position;

		}

		public int getCurrentPage() {
			return currentPage;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

			pager.getParent().requestDisallowInterceptTouchEvent(true);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	class ImagePagerAdapter extends PagerAdapter {

		private LayoutInflater inflater;
		ArrayList<JoltPlansListEntity> plansList;

		// private ArrayList<String> images;

		ImagePagerAdapter(ArrayList<JoltPlansListEntity> plansList) {

			inflater = getActivity().getLayoutInflater();

			this.plansList = plansList;
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return plansList.size();
		}

		@Override
		public float getPageWidth(int position) {
			return 0.8f;
		}

		@Override
		public Object instantiateItem(View view, final int position) {

			final View imageLayout = inflater.inflate(
					R.layout.item_choose_plan, null);
			ViewGroup group = (ViewGroup) imageLayout
					.findViewById(R.id.choose_lay);

			GlobalMethods.setFont(group, HomeScreen.helveticaNeueRegular);

			mChoose = (TextView) imageLayout.findViewById(R.id.choose);
			drinksCount = (TextView) imageLayout.findViewById(R.id.drinksCount);
			planCost = (TextView) imageLayout.findViewById(R.id.planCost);
			costPerCup = (TextView) imageLayout.findViewById(R.id.costPerCup);
			expiration = (TextView) imageLayout.findViewById(R.id.expiration);
			taxIncluded = (TextView) imageLayout.findViewById(R.id.taxIncluded);

			mChoose.setTypeface(HomeScreen.helveticaNeueBold);
			drinksCount.setTypeface(HomeScreen.helveticaNeueBold);
			planCost.setTypeface(HomeScreen.helveticaNeueBold);

			String tempCount = plansList.get(position).getDrinks();

			if (tempCount.equalsIgnoreCase(getString(R.string.unlimited))) {
				drinksCount.setText(getString(R.string.unlimited));
			} else {
				drinksCount.setText(tempCount + " "
						+ getString(R.string.drinks));
			}

			planCost.setText("$" + plansList.get(position).getCost());

			String tempCupCost = plansList.get(position).getPerCupCostInfo();

			if (tempCupCost.trim().isEmpty()) {
				costPerCup.setVisibility(View.GONE);
			} else {
				costPerCup.setText("$"
						+ plansList.get(position).getPerCupCostInfo() + " "
						+ getString(R.string.per_cup));
			}

			if (plansList.get(position).getIsIncludingTax()
					.equalsIgnoreCase("YES")) {
				taxIncluded.setVisibility(View.VISIBLE);
			} else {
				taxIncluded.setVisibility(View.GONE);
			}

			expiration.setText(plansList.get(position).getExpiration() + " "
					+ getString(R.string.expiration_txt));

			imageLayout.setTag(plansList.get(position));
			imageLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (!GlobalMethods.getUserID(getActivity())
							.equalsIgnoreCase(AppConstants.DEFAULT_USERID)) {
						Checkout.joltPlans = (JoltPlansListEntity) v.getTag();
						HomeScreen.mHeaderLeft
								.setTag(HomeScreen.SECONDARY_SCREEN);
						HomeScreen.moveBackFragment = new JoltPlansFragment();
						((HomeScreen) getActivity()).updateDisplayFragment(
								new Checkout(), true);
					} else {
						DialogManager.showCustomAlertSignInDialog(
								getActivity(),
								getString(R.string.sign_in_to_continue), null);
					}
				}
			});

			((ViewPager) view).addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		if (responseObj instanceof JoltPlansResponse) {

			JoltPlansResponse joltPlansResponse = (JoltPlansResponse) responseObj;

			if (joltPlansResponse != null
					&& joltPlansResponse.getError_code().equalsIgnoreCase(
							AppConstants.SUCCESS_CODE)) {
				classicPlansList = joltPlansResponse.getResult()
						.getClassicPlansIncludesInfo();
				allDrinksPlansList = joltPlansResponse.getResult()
						.getAllDrinksIncludesInfo();

				classicPlansIncludesInfoDescription = joltPlansResponse
						.getResult().getClassicdescription();
				allDrinksIncludesInfoDescription = joltPlansResponse
						.getResult().getAllDrinksdescription();
				if (((String) GlobalMethods.getValueFromPreference(
						getActivity(), GlobalMethods.STRING_PREFERENCE,
						AppConstants.JOLT_PLANS_INCLUDED_DISPLAY_VIEW))
						.equalsIgnoreCase(AppConstants.JOLT_PLANS_CLASSICS_VIEW)) {
					basics();
				} else if (((String) GlobalMethods.getValueFromPreference(
						getActivity(), GlobalMethods.STRING_PREFERENCE,
						AppConstants.JOLT_PLANS_INCLUDED_DISPLAY_VIEW))
						.equalsIgnoreCase(AppConstants.JOLT_PLANS_DRINKS_VIEW)) {
					drinks();
				}
			}

		}
	}

	private void getDeviceResolution() {
		density = getActivity().getResources().getDisplayMetrics().densityDpi;
		headerHeight = getResources().getDimensionPixelSize(R.dimen.margin50);
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);

		switch (density) {
		case DisplayMetrics.DENSITY_LOW:
			device = getString(R.string.LDPI);
			deviceHeight = metrics.heightPixels;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			device = getString(R.string.MDPI);
			deviceHeight = metrics.heightPixels;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			device = getString(R.string.HDPI);
			deviceHeight = metrics.heightPixels;
			break;
		case DisplayMetrics.DENSITY_TV:
			device = getString(R.string.TV);
			deviceHeight = metrics.heightPixels;
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			device = getString(R.string.XHDPI);
			deviceHeight = metrics.heightPixels;
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			device = getString(R.string.XXHDPI);
			deviceHeight = metrics.heightPixels;
			break;
		default:
			deviceHeight = metrics.heightPixels;
			break;

		}
		deviceHeight = deviceHeight - headerHeight;

	}
}
