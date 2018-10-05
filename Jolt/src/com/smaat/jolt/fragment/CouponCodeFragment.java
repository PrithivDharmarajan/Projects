package com.smaat.jolt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.smaat.jolt.R;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.model.GlobalResponse;
import com.smaat.jolt.model.PromoCouponResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.DialogManager;
import com.smaat.jolt.util.GlobalMethods;

public class CouponCodeFragment extends BaseFragment {

	Button send;
	EditText couponCode;
	String couponCodetext, mTempCheck;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.enter_coupon_fragment,
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
	}

	private void initComponents(View view) {
		mTempCheck = (((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.IS_VALIDATECOUPON_SCREEN)));

		send = (Button) getActivity().findViewById(R.id.btnSend);
		send.setTypeface(HomeScreen.helveticaNeueBold);
		couponCode = (EditText) getActivity().findViewById(R.id.edtCouponCode);
		couponCode.setTypeface(HomeScreen.helveticaNeueLight);
		if (mTempCheck.equalsIgnoreCase(AppConstants.IS_COUPONCODE_SCREEN)) {
			couponCode.setHint(getString(R.string.enter_coupon_code_txt));
		} else if (mTempCheck
				.equalsIgnoreCase(AppConstants.IS_PROMOCODE_SCREEN)) {
			couponCode.setHint(getString(R.string.enter_promo_code_txt));
		}
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				couponCodetext = couponCode.getText().toString().trim();
				hideSoftKeyboard(getActivity());
				if (couponCodetext.isEmpty()) {
					DialogManager.showToast(getActivity(),
							getString(R.string.enter_couponcode));
				} else {

					if (mTempCheck
							.equalsIgnoreCase(AppConstants.IS_COUPONCODE_SCREEN)) {
						APIRequestHandler.getInstance().validateCouponCode(
								GlobalMethods.getUserID(getActivity()),
								couponCodetext, CouponCodeFragment.this);
					} else if (mTempCheck
							.equalsIgnoreCase(AppConstants.IS_PROMOCODE_SCREEN)) {
						APIRequestHandler
								.getInstance()
								.validatePromoCouponCode(
										GlobalMethods.getUserID(getActivity()),
										couponCodetext, CouponCodeFragment.this);
					}

				}
			}
		});
	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof GlobalResponse) {

			GlobalResponse globalResponse = (GlobalResponse) responseObj;

			if (globalResponse != null
					&& globalResponse.getError_code().equalsIgnoreCase(
							AppConstants.SUCCESS_CODE)) {

				GlobalMethods.storeValuetoPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.AVAIL_DRINKS, globalResponse.getResult());
				DialogManager.showToast(getActivity(),
						getString(R.string.got_free_drink));

				if (HomeScreen.moveBackFragment instanceof Checkout) {
					HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
					((HomeScreen) getActivity()).updateDisplayFragment(
							new Checkout(), false);

				} else {
					((HomeScreen) getActivity()).showHomeScreenView();
				}

			} else {
				DialogManager.showToast(getActivity(), globalResponse.getMsg());
			}

		}
		if (responseObj instanceof PromoCouponResponse) {

			PromoCouponResponse promoCouponResponse = (PromoCouponResponse) responseObj;

			if (promoCouponResponse != null
					&& promoCouponResponse.getError_code().equalsIgnoreCase(
							AppConstants.SUCCESS_CODE)) {

				GlobalMethods.storeValuetoPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.AVAIL_DRINKS,
						promoCouponResponse.getResult());
				DialogManager.showToast(getActivity(),
						getString(R.string.got_free_drink));

				if (HomeScreen.moveBackFragment instanceof Checkout) {
					HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
					((HomeScreen) getActivity()).updateDisplayFragment(
							new Checkout(), false);

				} else {
					((HomeScreen) getActivity()).showHomeScreenView();
				}

			} else {
				DialogManager.showToast(getActivity(),
						promoCouponResponse.getMsg());
			}

		}

	}

}
