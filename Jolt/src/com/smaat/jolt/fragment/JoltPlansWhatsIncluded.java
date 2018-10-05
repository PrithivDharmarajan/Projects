package com.smaat.jolt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class JoltPlansWhatsIncluded extends BaseFragment implements
		OnClickListener {

	private TextView mGotItBtn,the_basics_txt, all_drinks_txt, the_basics_detail_txt,
			all_drinks_detail_txt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(
				R.layout.slide_jolt_plans_whats_included, container, false);
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
		mGotItBtn = (TextView) view.findViewById(R.id.got_it_txt);
		the_basics_txt = (TextView) view.findViewById(R.id.the_basics_txt);
		all_drinks_txt = (TextView) view.findViewById(R.id.all_drinks_txt);

		the_basics_detail_txt = (TextView) view
				.findViewById(R.id.the_basics_detail_txt);
		all_drinks_detail_txt = (TextView) view
				.findViewById(R.id.all_drinks_detail_txt);

		all_drinks_detail_txt
				.setText(JoltPlansFragment.allDrinksIncludesInfoDescription);
		the_basics_detail_txt
				.setText(JoltPlansFragment.classicPlansIncludesInfoDescription);

		

		setClickListener();
	}

	public void setClickListener() {
		mGotItBtn.setOnClickListener(this);
		the_basics_txt.setOnClickListener(this);
		all_drinks_txt.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.got_it_txt:
			HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
			((HomeScreen) getActivity()).updateDisplayFragment(
					new JoltPlansFragment(), false);

			break;

		case R.id.the_basics_txt:
			the_basics_txt.setBackgroundResource(R.drawable.classics_over);
			all_drinks_txt.setBackgroundResource(R.drawable.classics_normal);
			HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.JOLT_PLANS_INCLUDED_DISPLAY_VIEW,
					AppConstants.JOLT_PLANS_CLASSICS_VIEW);
			((HomeScreen) getActivity()).updateDisplayFragment(
					new JoltPlansFragment(), false);
			break;
		case R.id.all_drinks_txt:
			all_drinks_txt.setBackgroundResource(R.drawable.classics_over);
			the_basics_txt.setBackgroundResource(R.drawable.classics_normal);
			HomeScreen.mHeaderLeft.setTag(HomeScreen.MENU_SCREEN);
			GlobalMethods.storeValuetoPreference(getActivity(),
					GlobalMethods.STRING_PREFERENCE,
					AppConstants.JOLT_PLANS_INCLUDED_DISPLAY_VIEW,
					AppConstants.JOLT_PLANS_DRINKS_VIEW);
			((HomeScreen) getActivity()).updateDisplayFragment(
					new JoltPlansFragment(), false);
			break;
		}

	}

}
