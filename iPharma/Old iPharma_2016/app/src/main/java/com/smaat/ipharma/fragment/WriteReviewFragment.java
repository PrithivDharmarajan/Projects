package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.smaat.ipharma.R;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import retrofit.RetrofitError;

public class WriteReviewFragment extends BaseFragment implements
		DialogMangerCallback {
	private EditText mREviewEdit;
	private Button mPost;
	private String UserID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_write_review,
				container, false);
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
		Typeface mTypeface = TypefaceSingleton.getInstance().getHelvetica(
				getActivity());
		setFont(mViewGroup, mTypeface);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());
		UserID = GlobalMethods.getUserID(getActivity());
		initComponents(view);

		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				HomeScreen.onBackMove(getActivity());
			}
		});

		AppConstants.FRAG = AppConstants.REVIEW;
	}

	private void initComponents(View view) {
		// mHeadingEdit = (EditText)
		// view.findViewById(R.id.review_heading_edit);
		mREviewEdit = (EditText) view.findViewById(R.id.review_desc_edit);
		mPost = (Button) view.findViewById(R.id.post);
		mPost.setTypeface(HomeScreen.mHelveticaBold);

		mPost.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (ValidRegister()) {
					callWritereviewService();
				}
			}
		});
	}

	private void callWritereviewService() {

		// DialogManager.showProgress(getActivity());
		// RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
		// AppConstants.Base_Url).build();
		// APICommonInterface interfaces = restAdapter
		// .create(APICommonInterface.class);

		String review_comment = mREviewEdit.getText().toString();
		String user_id = UserID;
		String shop_id = AppConstants.Pharmacy_id;

		APIRequestHandler.getInstance().writeReview(review_comment, user_id,
				shop_id, this);
		// interfaces.writeReview(review_comment, user_id, shop_id,
		// new Callback<WriteReviewEntity>() {
		//
		// public void failure(RetrofitError arg0) {
		//
		// DialogManager.hideProgress(getActivity());
		// DialogManager.showCustomAlertDialog(getActivity(),
		// WriteReviewFragment.this,
		// "Please check your Internet Connection.");
		// }
		//
		// public void success(WriteReviewEntity response, Response obj) {
		//
		// if (response.getStatus() != 0) {
		//
		// DialogManager.showCustomAlertDialog(getActivity(),
		// WriteReviewFragment.this,
		// "Review Submitted Successfully.");
		// } else {
		//
		// DialogManager.showCustomAlertDialog(getActivity(),
		// WriteReviewFragment.this, response.getMsg());
		// }
		//
		// DialogManager.hideProgress(getActivity());
		// }
		//
		// });
	}

	@Override
	public void onRequestSuccess(Object responseObj) {

		if (responseObj instanceof WriteReviewEntity) {

			DialogManager.hideProgress(getActivity());
			WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;

			if (mWriteReviewResponse.getStatus().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)) {

				((HomeScreen) getActivity()).replaceFragment(
						new ReviewsFragment(), false);
				// HomeScreen.mFooterText.setText(R.string.write_review);
			} else {

				DialogManager
						.showCustomAlertDialog(getActivity(),
								WriteReviewFragment.this,
								mWriteReviewResponse.getMsg());
			}
		}

		super.onRequestSuccess(responseObj);
	}

	@Override
	public void onRequestFailure(RetrofitError errorCode) {

		super.onRequestFailure(errorCode);
	}

	private boolean ValidRegister() {
		if (mREviewEdit.getText().toString().trim().isEmpty()
				&& mREviewEdit.getText().toString().trim().length() < 1
				&& mREviewEdit.getText().toString().trim().equalsIgnoreCase("")) {
			DialogManager.showCustomAlertDialog(getActivity(), this,
					getString(R.string.enter_reviews));
			return false;
		}

		return true;
	}

	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// getActivity().getSupportFragmentManager().beginTransaction()
	// .remove(this).commit();
	// }

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	public void onItemclick(String SelctedItem, int pos) {
		// TODO Auto-generated method stub

	}

	public void onOkclick() {
		// TODO Auto-generated method stub

	}
}
