package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.OtpEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

public class PaymentOptionsFragment extends BaseFragment implements
		DialogMangerCallback, OnClickListener {

	private Button mDebitcard, mCreditcard, mNetbanking, mIpharma_money_btn,
			mCashonDelivery;
	private TextView mPurchaseAmount;
	private String mTempCurrentMoney, mTempTotalMoney;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_payment_options,
				container, false);
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
		HomeScreen.mHeaderText.setTypeface(HomeScreen.mHelveticaBold);
		hideSoftKeyboard(getActivity());
		initComponents(view);
		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (AppConstants.FRAG
						.equalsIgnoreCase(AppConstants.ORDER_DETAILS)) {
					HomeScreen.onBackMove(getActivity());
				} else if (AppConstants.FRAG
						.equalsIgnoreCase(AppConstants.MONEY_SCREEN)) {
					AppConstants.from_my_order = "";
					HomeScreen.onBackMove(getActivity());

				}
				// AppConstants.from_my_order = "";
				// HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				// HomeScreen.mHeaderText.setText(R.string.ipharma_money);
				// HomeScreen.mBottombar.setVisibility(View.GONE);
				// HomeScreen.mHeaderLeft
				// .setBackgroundResource(R.drawable.back_butto);
				// ((HomeScreen) getActivity())
				// .replaceFragment(new IpharmaMoneyFragment());
			}
		});
	}

	private void initComponents(View view) {

		mDebitcard = (Button) view.findViewById(R.id.debit_card_money);
		mCreditcard = (Button) view.findViewById(R.id.credit_card_btn);
		mNetbanking = (Button) view.findViewById(R.id.net_banking_btn);
		mIpharma_money_btn = (Button) view.findViewById(R.id.ipharma_money_btn);
		mCashonDelivery = (Button) view.findViewById(R.id.cash_on_delivery_btn);
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

		mDebitcard.setTypeface(HomeScreen.mHelveticaBold);
		mCreditcard.setTypeface(HomeScreen.mHelveticaBold);
		mNetbanking.setTypeface(HomeScreen.mHelveticaBold);
		mIpharma_money_btn.setTypeface(HomeScreen.mHelveticaBold);
		mCashonDelivery.setTypeface(HomeScreen.mHelveticaBold);

		if (AppConstants.from_my_order
				.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			AppConstants.FRAG = AppConstants.ORDER_DETAILS;
			mIpharma_money_btn.setVisibility(View.VISIBLE);
			mCashonDelivery.setVisibility(View.VISIBLE);
		} else if (AppConstants.from_my_order
				.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
			AppConstants.FRAG = AppConstants.MONEY_SCREEN;
			mIpharma_money_btn.setVisibility(View.GONE);
			mCashonDelivery.setVisibility(View.GONE);
		}
		setClickListener();
		mTempTotalMoney = ((String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.IPHARMA_TOTAL_MONEY));
		if (mTempTotalMoney.equalsIgnoreCase(null)
				|| mTempTotalMoney.equalsIgnoreCase("")) {
			mTempTotalMoney = AppConstants.FAILURE_CODE;
		}
		mTempTotalMoney = mTempTotalMoney.replaceAll(
				getString(R.string.rupee_sym), "");
	}

	private void setClickListener() {
		// TODO Auto-generated method stub
		mDebitcard.setOnClickListener(this);
		mCreditcard.setOnClickListener(this);
		mNetbanking.setOnClickListener(this);
		mIpharma_money_btn.setOnClickListener(this);
		mCashonDelivery.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.debit_card_money:
			DialogManager.showToast(getActivity(), "Not Yet Integrate");

			break;
		case R.id.credit_card_btn:

			DialogManager.showToast(getActivity(), "Not Yet Integrate");
			// mTempCurrentMoney = mTempCurrentMoney.replaceAll(
			// getString(R.string.rupee_sym), "");
			// if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			// HomeScreen.mHeaderText.setText(R.string.credit_card);
			// HomeScreen.mHeaderLeft
			// .setBackgroundResource(R.drawable.back_butto);
			// ((HomeScreen) getActivity()).replaceFragment(
			// new PaymentOptionsTypeFragment(), true);
			// // mTempTotalMoney = String.valueOf(Integer
			// // .parseInt(mTempTotalMoney)
			// // - Integer.parseInt(mTempCurrentMoney));
			// } else if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
			// HomeScreen.mHeaderText.setText(R.string.credit_card);
			// HomeScreen.mHeaderLeft
			// .setBackgroundResource(R.drawable.back_butto);
			// ((HomeScreen) getActivity()).replaceFragment(
			// new PaymentOptionsTypeFragment(), true);
			// // mTempTotalMoney = String.valueOf(Integer
			// // .parseInt(mTempTotalMoney)
			// // + Integer.parseInt(mTempCurrentMoney));
			//
			// }
			// GlobalMethods.storeValuetoPreference(getActivity(),
			// GlobalMethods.STRING_PREFERENCE,
			// AppConstants.IPHARMA_TOTAL_MONEY, mTempTotalMoney);

			break;
		case R.id.net_banking_btn:

			DialogManager.showToast(getActivity(), "Not Yet Integrate");
			// mTempCurrentMoney = mTempCurrentMoney.replaceAll(
			// getString(R.string.rupee_sym), "");
			// if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			// mTempTotalMoney = String.valueOf(Integer
			// .parseInt(mTempTotalMoney)
			// - Integer.parseInt(mTempCurrentMoney));
			// } else if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
			//
			// mTempTotalMoney = String.valueOf(Integer
			// .parseInt(mTempTotalMoney)
			// + Integer.parseInt(mTempCurrentMoney));
			//
			// }
			// GlobalMethods.storeValuetoPreference(getActivity(),
			// GlobalMethods.STRING_PREFERENCE,
			// AppConstants.IPHARMA_TOTAL_MONEY, mTempTotalMoney);
			// backtoMoneyScreen();
			break;
		case R.id.ipharma_money_btn:

			DialogManager.showToast(getActivity(), "Not Yet Integrate");
			// mTempCurrentMoney = mTempCurrentMoney.replaceAll(
			// getString(R.string.rupee_sym), "");
			// if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
			// mTempTotalMoney = String.valueOf(Integer
			// .parseInt(mTempTotalMoney)
			// - Integer.parseInt(mTempCurrentMoney));
			// } else if (AppConstants.from_my_order
			// .equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
			//
			// mTempTotalMoney = String.valueOf(Integer
			// .parseInt(mTempTotalMoney)
			// + Integer.parseInt(mTempCurrentMoney));
			//
			// }
			// GlobalMethods.storeValuetoPreference(getActivity(),
			// GlobalMethods.STRING_PREFERENCE,
			// AppConstants.IPHARMA_TOTAL_MONEY, mTempTotalMoney);
			// backtoMoneyScreen();
			break;
		case R.id.cash_on_delivery_btn:
			APIRequestHandler.getInstance().getOrderPaymentUpdate(
					GlobalMethods.getUserID(getActivity()),
					AppConstants.history_mPrescriptionId,
					AppConstants.SUCCESS_CODE, "Cash On Delivery", this);

			break;

		default:
			break;
		}
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		super.onRequestSuccess(responseObj);
		if (responseObj instanceof OtpEntity) {

			OtpEntity response = (OtpEntity) responseObj;

			if (response.getStatus()
					.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
				((HomeScreen) getActivity()).replaceFragment(
						new MapScreenFragment(), false);
				AppConstants.FROMINTLOADMAP = AppConstants.FROMMAP;
				DialogManager.showToast(getActivity(), "Sucess");

			}
		}
	}

	void backtoMoneyScreen() {
		HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
		HomeScreen.mHeaderText.setText(R.string.ipharma_money);
		HomeScreen.mBottombar.setVisibility(View.GONE);
		HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
		((HomeScreen) getActivity()).replaceFragment(
				new IpharmaMoneyFragment(), false);
	}

	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// AppConstants.from_my_order = "";
	// getActivity().getSupportFragmentManager().beginTransaction()
	// .remove(this).commit();
	// }

	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
