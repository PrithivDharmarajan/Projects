package com.smaat.ipharma.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.OfferAdapter;
import com.smaat.ipharma.apiinterface.APICommonInterface;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.MapResponse;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OffersFragment extends BaseFragment implements
		DialogMangerCallback {
	private ListView favourite_list;

	private String Userid, mShopId, mLatitude, mLongitude, mSearchkey;
	private float mMapDistance;
	GPSTracker tracker;
	private AQuery aq1;
	private LinearLayout mOffers_list_item_view;
	private Bundle bundle;
	public static int mSelectedPos;
	private float mRate;
	ArrayList<MapPropertyEntity> orderList;
	private OfferAdapter adapter;



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootview = inflater.inflate(R.layout.fragment_favourite,
				container, false);
		return rootview;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ViewGroup mViewGroup = (ViewGroup) view
				.findViewById(R.id.parent_layout);
		setupUI(mViewGroup);
		hideSoftKeyboard(getActivity());
		Userid = GlobalMethods.getUserID(getActivity());
		tracker = new GPSTracker(getActivity());

		AppConstants.FRAG = AppConstants.MAP_SCREEN;
		HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
		HomeScreen.mHeaderRight
				.setBackgroundResource(R.drawable.maplist_normal);
		HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);
		orderList = new ArrayList<>();
		initComponents(view);
		callOffersService();// API Call
		// setAdapter();

		HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				HomeScreen.onBackMove(getActivity());
				AppConstants.offer_latitude = "";
				AppConstants.offer_longitude = "";
				AppConstants.offer_map_distance = 1;
				AppConstants.offer_search_text = "";
			}
		});
	}

	private void callOffersService() {

		DialogManager.showProgress(getActivity());
		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(
				AppConstants.Base_Url).build();
		APICommonInterface interfaces = restAdapter
				.create(APICommonInterface.class);
		if (AppConstants.offer_latitude.equalsIgnoreCase("")) {
			mLatitude = String.valueOf(tracker.getLatitude());
			mLongitude = String.valueOf(tracker.getLongitude());
		}
		mMapDistance = 10;
		mSearchkey = "";
		interfaces.getOffers(Userid, mLatitude, mLongitude, mSearchkey,
				mMapDistance, new Callback<MapResponse>() {

					public void failure(RetrofitError arg0) {

						DialogManager.hideProgress(getActivity());
						DialogManager.showCustomAlertDialog(getActivity(),
								OffersFragment.this,
								getString(R.string.no_network));
					}

					public void success(MapResponse response, Response obj) {

						DialogManager.hideProgress(getActivity());
						if (response.getStatus().equalsIgnoreCase(
								AppConstants.FAILURE_CODE)) {
							DialogManager.showCustomAlertDialog(getActivity(),
									OffersFragment.this, response.getMsg());
						} else {
							if (response.getResult() != null
									&& response.getResult().size() != 0) {
								orderList = response.getResult();
								adapter = new OfferAdapter(OffersFragment.this,R.layout.fragment_favourite,orderList);
								favourite_list.setAdapter(adapter);
							} else {
								DialogManager.showCustomAlertDialog(
										getActivity(), OffersFragment.this,
										getString(R.string.no_offers_found));
							}
						}

					}

				});
	}

	private void initComponents(View view) {
		favourite_list = (ListView) view.findViewById(R.id.favourite_list);
	}



	public void showRateDialog(final String mShopId) {

		mDialog = new Dialog(getActivity());
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mDialog.setContentView(R.layout.dialog_rate);

		mDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
		WindowManager.LayoutParams wmlp = mDialog.getWindow().getAttributes();
		wmlp.width = android.view.ViewGroup.LayoutParams.MATCH_PARENT;

		mDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
						| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		Button rate = (Button) mDialog.findViewById(R.id.rate_it);
		Button cancel = (Button) mDialog.findViewById(R.id.cancel);
		rate.setTypeface(HomeScreen.mHelveticaBold);
		cancel.setTypeface(HomeScreen.mHelveticaBold);
		TextView mRateText = (TextView) mDialog.findViewById(R.id.rate_text);
		final TextView mRating_avg_txt = (TextView) mDialog
				.findViewById(R.id.rating_avg_txt);
		mRating_avg_txt.setTypeface(HomeScreen.mHelveticaBold);
		mRateText.setTypeface(HomeScreen.mHelveticaNormal);
		final RatingBar mFav_ratingbar = (RatingBar) mDialog
				.findViewById(R.id.fav_ratingbar);
		final Button mRating_icons = (Button) mDialog
				.findViewById(R.id.rating_icons);

		mFav_ratingbar
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

					public void onRatingChanged(RatingBar ratingBar,
							float rating, boolean fromUser) {
						switch ((int) mFav_ratingbar.getRating()) {
						case 1:
							mRating_avg_txt.setText(getActivity().getString(
									R.string.poor));
							mRating_icons
									.setBackgroundResource(R.drawable.poor);
							break;
						case 2:
							mRating_avg_txt.setText(getActivity().getString(
									R.string.bad));
							mRating_icons.setBackgroundResource(R.drawable.bad);
							break;
						case 3:
							mRating_avg_txt.setText(getActivity().getString(
									R.string.average));
							mRating_icons
									.setBackgroundResource(R.drawable.average);
							break;
						case 4:
							mRating_avg_txt.setText(getActivity().getString(
									R.string.good));
							mRating_icons
									.setBackgroundResource(R.drawable.good);
							break;
						case 5:
							mRating_avg_txt.setText(getActivity().getString(
									R.string.excellent));
							mRating_icons
									.setBackgroundResource(R.drawable.excellent);
							break;
						}
					}
				});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mDialog.dismiss();
			}
		});
		rate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				float rating_value = mFav_ratingbar.getRating();

				mRate = rating_value;
				callPharmacyRatingService(rating_value,mShopId);
				mDialog.dismiss();
			}
		});

		mDialog.show();
	}

	private void callPharmacyRatingService(float rating_value, String mShopId) {
		String rating = String.valueOf(rating_value);
		APIRequestHandler.getInstance().shopRating(mShopId,
				GlobalMethods.getUserID(getActivity()), rating, this);
	}

	@Override
	public void onRequestSuccess(Object responseObj) {
		// TODO Auto-generated method stub
		super.onRequestSuccess(responseObj);
		if (responseObj instanceof RateResponseEntity) {
			RateResponseEntity mRatingResponse = (RateResponseEntity) responseObj;
			if (mRatingResponse.getStatus().equalsIgnoreCase(
					AppConstants.SUCCESS_CODE)
					|| mRatingResponse.getStatus().equalsIgnoreCase("success")) {
				DialogManager.showCustomAlertDialog(getActivity(),
						OffersFragment.this, getString(R.string.rating_added));
				orderList.get(mSelectedPos).setAvgRating(
						String.valueOf(mRatingResponse.getResult()));
				adapter.notifyDataSetChanged();
			} else {

				DialogManager.showCustomAlertDialog(getActivity(), this,
						mRatingResponse.getMsg());
			}
		}
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
