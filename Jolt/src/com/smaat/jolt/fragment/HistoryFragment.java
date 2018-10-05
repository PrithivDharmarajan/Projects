package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smaat.jolt.R;
import com.smaat.jolt.adapter.HistoryAdapter;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.HistoryDetailsEntity;
import com.smaat.jolt.model.MyHistoryResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class HistoryFragment extends BaseFragment implements OnClickListener {

	ArrayList<HistoryDetailsEntity> mHistory;
	private ListView mHistoryListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.history_fragment, container,
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
		
		//Api call
		APIRequestHandler.getInstance().getMyHistory(
				GlobalMethods.getUserID(getActivity()), this);
	}

	private void initComponents(View view) {
		mHistoryListView = (ListView) view.findViewById(R.id.history_listView);
	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof MyHistoryResponse) {

			MyHistoryResponse mResponse = (MyHistoryResponse) responseObj;
			if (mResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				mHistory = mResponse.getResult();
				HistoryAdapter adapter = new HistoryAdapter(getActivity(),
						mHistory);
				mHistoryListView.setAdapter(adapter);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		default:
			break;
		}

	}

	
}
