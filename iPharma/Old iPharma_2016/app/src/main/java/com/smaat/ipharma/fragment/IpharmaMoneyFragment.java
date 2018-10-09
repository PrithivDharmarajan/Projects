package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

public class IpharmaMoneyFragment extends BaseFragment implements
		DialogMangerCallback, OnClickListener {

	private Button mAdd_ipharma_money, ipharma_money_1, ipharma_money_2,
			ipharma_money_3;
	private EditText enter_amount;
	private String mTempMoney, mTempTotalMoney;
	private TextView mCurrentBalance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_ipharma_money,
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

		AppConstants.FRAG = AppConstants.MAP_SCREEN;
		hideSoftKeyboard(getActivity());
		initComponents(view);
		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				HomeScreen.onBackMove(getActivity());
			}
		});
	}

	private void initComponents(View view) {
		mCurrentBalance = (TextView) view.findViewById(R.id.current_balance);
		mTempTotalMoney = (String) GlobalMethods.getValueFromPreference(
				getActivity(), GlobalMethods.STRING_PREFERENCE,
				AppConstants.IPHARMA_TOTAL_MONEY);
		mTempTotalMoney = mTempTotalMoney.replaceAll(
				getString(R.string.rupee_sym), "");
		if (mTempTotalMoney.equalsIgnoreCase(null)
				|| mTempTotalMoney.equalsIgnoreCase("")
				|| mTempTotalMoney.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
			mCurrentBalance.setText(getString(R.string.rupee_sym)
					+ AppConstants.FAILURE_CODE);
		} else {
			mCurrentBalance.setText(getString(R.string.rupee_sym)
					+ mTempTotalMoney);
		}
		mAdd_ipharma_money = (Button) view.findViewById(R.id.add_ipharma_money);
		ipharma_money_1 = (Button) view.findViewById(R.id.ipharma_money_1);
		ipharma_money_1.setText(getString(R.string.rupee_sym)
				+ getString(R.string.money1));
		ipharma_money_2 = (Button) view.findViewById(R.id.ipharma_money_2);
		ipharma_money_2.setText(getString(R.string.rupee_sym)
				+ getString(R.string.money2));
		ipharma_money_3 = (Button) view.findViewById(R.id.ipharma_money_3);
		ipharma_money_3.setText(getString(R.string.rupee_sym)
				+ getString(R.string.money3));
		enter_amount = (EditText) view.findViewById(R.id.enter_amount);
		enter_amount.setText("");
		// listeners
		mAdd_ipharma_money.setOnClickListener(this);
		ipharma_money_1.setOnClickListener(this);
		ipharma_money_2.setOnClickListener(this);
		ipharma_money_3.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_ipharma_money:
			mTempMoney = enter_amount.getText().toString().trim();
			if (mTempMoney.equalsIgnoreCase(null)
					|| mTempMoney.equalsIgnoreCase("")) {
				DialogManager.showCustomAlertDialog(getActivity(), this,
						getString(R.string.add_money_title),
						getString(R.string.enter_valid_money));
			} else {
				GlobalMethods.storeValuetoPreference(getActivity(),
						GlobalMethods.STRING_PREFERENCE,
						AppConstants.IPHARMA_MONEY, mTempMoney);
				AppConstants.from_my_order = AppConstants.FAILURE_CODE;
				HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
				HomeScreen.mHeaderLeft
						.setBackgroundResource(R.drawable.back_butto);
				((HomeScreen) getActivity())
						.replaceFragment(new PaymentOptionsFragment(),true);
			}
			break;
		case R.id.ipharma_money_1:
			enter_amount.setText(ipharma_money_1.getText().toString().trim());
			break;
		case R.id.ipharma_money_2:
			enter_amount.setText(ipharma_money_2.getText().toString().trim());
			break;
		case R.id.ipharma_money_3:
			enter_amount.setText(ipharma_money_3.getText().toString().trim());
			break;
		default:
			break;
		}
	}

//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.remove(this).commit();
//	}

	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
