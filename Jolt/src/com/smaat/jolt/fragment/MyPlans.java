package com.smaat.jolt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.adapter.MyPlansAdapter;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;

public class MyPlans extends BaseFragment {

	private ListView mPlansLay;
	private TextView free_drink, free_drink_txt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.my_plans_fragment, container,
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

		initComponents(view);

	}

	private void initComponents(View view) {
		free_drink_txt = (TextView) view.findViewById(R.id.free_drink_txt);
		free_drink = (TextView) view.findViewById(R.id.free_drink);
		free_drink.setText(ProfileFragment.mNoOfDrinks);
		mPlansLay = (ListView) view.findViewById(R.id.my_plans_lay);
		if (ProfileFragment.mUserPlanList != null
				&& ProfileFragment.mUserPlanList.size() > 0) {
			MyPlansAdapter adapter = new MyPlansAdapter(getActivity(),
					ProfileFragment.mUserPlanList);
			mPlansLay.setAdapter(adapter);

		}
		
		free_drink_txt.setTypeface(HomeScreen.helveticaNeueBold);
		free_drink.setTypeface(HomeScreen.helveticaNeueMedium);
	}

}
