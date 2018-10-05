package com.smaat.jolt.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.smaat.jolt.R;
import com.smaat.jolt.adapter.MessageAdapter;
import com.smaat.jolt.apiinterface.APIRequestHandler;
import com.smaat.jolt.entity.JoltMessageEntity;
import com.smaat.jolt.model.MessageResponse;
import com.smaat.jolt.ui.BaseFragment;
import com.smaat.jolt.ui.HomeScreen;
import com.smaat.jolt.util.AppConstants;
import com.smaat.jolt.util.GlobalMethods;

public class MessageUsFragment extends BaseFragment {

	ListView mMessageList;
	public static RelativeLayout mHeaderLeft, mHeaderRight;
	public static Button mHeaderRightBtn;
	private ArrayList<JoltMessageEntity> messages;
	private String mTemp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootview = inflater.inflate(R.layout.message_us, container, false);
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
		HomeScreen.mHeaderText.setText(R.string.conversations_txt);
		// Api call
		APIRequestHandler.getInstance().getConversationList(
				GlobalMethods.getUserID(getActivity()), this);

	}

	private void initComponents(View view) {

		mMessageList = (ListView) getActivity().findViewById(R.id.message_list);
		mTemp = getString(R.string.mRightImgTemp);

		if (mTemp.equalsIgnoreCase(getString(R.string.mRightImgTemp))) {
			mHeaderRight = (RelativeLayout) getActivity().findViewById(
					R.id.header_right);
			mHeaderRightBtn = (Button) getActivity().findViewById(
					R.id.header_right_btn);
			mHeaderRightBtn.setBackgroundResource(R.drawable.conversation_edit);
			mHeaderRight.setVisibility(View.VISIBLE);
			mHeaderRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					HomeScreen.mHeaderLeft.setTag(HomeScreen.SECONDARY_SCREEN);
					HomeScreen.moveBackFragment = new MessageUsFragment();
					((HomeScreen) getActivity()).updateDisplayFragment(
							new NewConversationFragment(), true);
					mHeaderRight.setVisibility(View.INVISIBLE);

				}
			});
		}

	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof MessageResponse) {

			MessageResponse mResponse = (MessageResponse) responseObj;
			if (mResponse.getError_code().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				messages = mResponse.getResult();
				MessageAdapter adapter = new MessageAdapter(getActivity(),
						messages);
				mMessageList.setAdapter(adapter);
				// adapter.notifyDataSetChanged();
				// mMessageList.invalidate();
				// ((BaseAdapter) mMessageList.getAdapter())
				// .notifyDataSetChanged();
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {

		// case R.id.header_right:
		// Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
		}
	}

}
