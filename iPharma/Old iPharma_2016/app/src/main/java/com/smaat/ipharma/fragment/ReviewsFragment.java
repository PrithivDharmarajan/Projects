package com.smaat.ipharma.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.ReviewsAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.Reviews;
import com.smaat.ipharma.entity.ShowReviewMessageEntity;
import com.smaat.ipharma.entity.ShowReviewResponse;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class ReviewsFragment extends BaseFragment implements
		DialogMangerCallback {
	private ReviewsAdapter mReviewAdapter;
	private ListView mReviewsList;
	ArrayList<Reviews> mReviews;
	private LinearLayout mBottomView;
	private TextView mFooterText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_reviews, container,
				false);
		setupUI(rootview);
		Window window = getActivity().getWindow();
		window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());
		HomeScreen.mBottombar.setVisibility(View.GONE);
		initComponents(view);
		getReviewsService();// API Call
		AppConstants.FRAG = AppConstants.ORDERNOW_SCREEN;
		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (AppConstants.from_map_list_review
						.equalsIgnoreCase(AppConstants.FAILURE_CODE)) {
					HomeScreen.onBackMove(getActivity());
				} else {
					HomeScreen.mHeaderRightLay.setVisibility(View.VISIBLE);
					HomeScreen.mHeaderRight
							.setBackgroundResource(R.drawable.map_view_normal);
					HomeScreen.mHeaderLeft
							.setBackgroundResource(R.drawable.slide);
					((HomeScreen) getActivity()).replaceFragment(
							new MapScreenFragment(), true);
					AppConstants.from_map_list_review = AppConstants.FAILURE_CODE;
				}
			}
		});

	}

	private void setAdapter(ArrayList<ShowReviewMessageEntity> review_list) {
		if (review_list != null) {
			mReviewAdapter = new ReviewsAdapter(getActivity(),
					R.layout.reviews_item, review_list);
			mReviewsList.setAdapter(mReviewAdapter);
		}
	}

	private void initComponents(View view) {
		mReviewsList = (ListView) view.findViewById(R.id.reviews_list);
		mBottomView = (LinearLayout) view.findViewById(R.id.bottom_bar);
		mFooterText = (TextView) view.findViewById(R.id.footer_text);
		mFooterText.setText(getString(R.string.write_review));
		mFooterText.setTypeface(HomeScreen.mHelveticaBold);

		mBottomView.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				((HomeScreen) getActivity()).replaceFragment(
						new WriteReviewFragment(), true);
			}
		});

	}

	private void getReviewsService() {

		String shop_id = AppConstants.Pharmacy_id;

		APIRequestHandler.getInstance().showReview(shop_id, this);
	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof ShowReviewResponse) {

			DialogManager.hideProgress(getActivity());
			ShowReviewResponse mShowReviewResponse = (ShowReviewResponse) responseObj;

			if (mShowReviewResponse.getStatus().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {
				DialogManager.hideProgress(getActivity());
				setAdapter(mShowReviewResponse.getMsg());

			}

		}
		super.onRequestSuccess(responseObj);
	}

	@Override
	public void onRequestFailure(RetrofitError errorCode) {

		super.onRequestFailure(errorCode);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
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
