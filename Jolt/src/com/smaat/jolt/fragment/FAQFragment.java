package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.smaat.jolt.R;
import com.smaat.jolt.adapter.FAQAdapter;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.FAQEntity;
import com.smaat.jolt.model.FAQResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class FAQFragment extends BaseFragment {

	private ListView FaqList;
	private TextView mFaqTxt;
	public ArrayList<FAQEntity> faq;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.faq_fragment, container,
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
		// Api call
		APIRequestHandler.getInstance().getFAQMessage(
				GlobalMethods.getUserID(getActivity()), this);

	}

	private void initComponents(View view) {
		
		FaqList = (ListView) view.findViewById(R.id.faq_list_view);
		mFaqTxt = (TextView) view.findViewById(R.id.faq_txt);
		mFaqTxt.setTypeface(HomeScreen.helveticaNeueBold);
	}

	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof FAQResponse) {

			FAQResponse mResponse = (FAQResponse) responseObj;
			if (mResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				faq = mResponse.getResult();
				FAQAdapter mAdapter = new FAQAdapter(getActivity(), faq);
				FaqList.setAdapter(mAdapter);
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {

		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
