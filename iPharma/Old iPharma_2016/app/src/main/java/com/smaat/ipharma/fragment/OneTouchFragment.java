package com.smaat.ipharma.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.OneTouchAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.FavouriteCommonResponse;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.ProfileImageSelectionUtil;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class OneTouchFragment extends BaseFragment implements
        DialogMangerCallback {
    private ListView favourite_list;

    private GPSTracker tracker;

    public static String mUserID;
    public static int mSelectedPos;
    ArrayList<MapPropertyEntity> orderList;
    private OneTouchAdapter adapter;
    private float mRate;
    public static MapPropertyEntity mMapDetailEntity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_onetouch,
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
        orderList = new ArrayList<>();

        callFavouriteService(); // API Call
        initComponents(view);

        AppConstants.FRAG = AppConstants.MAP_SCREEN;
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                HomeScreen.onBackMove(getActivity());
            }
        });

        HomeScreen.mHeaderRightLay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSelectList();
            }
        });
    }


    private void moveToSelectList(){
        AppConstants.from_map_list = AppConstants.FALSE;
        HomeScreen.mFragment = new OneTouchListSelectFragment();
        HomeScreen.mHeaderRightLay.setVisibility(View.INVISIBLE);

        HomeScreen.mHeaderLeft.setBackgroundResource(R.drawable.back_butto);
        ((HomeScreen)getActivity()).replaceFragment(HomeScreen.mFragment, true);
    }
    private void callFavouriteService() {

        String mUserID = GlobalMethods.getUserID(getActivity());
        tracker = new GPSTracker(getActivity());
        String mLat = "";
        String mLongit = "";
        if (tracker != null) {
            mLat = tracker.getLatitude() + "";
            mLongit = tracker.getLongitude() + "";
        }
        APIRequestHandler.getInstance().getFavouriteShops(mUserID, mLat,
                mLongit, this);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof WriteReviewEntity) {

            WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;

            if (mWriteReviewResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {
                callFavouriteService();
            } else {
                DialogManager.showCustomAlertDialog(getActivity(),
                        OneTouchFragment.this, mWriteReviewResponse.getMsg());
            }

        }

        if (responseObj instanceof FavouriteCommonResponse) {

            FavouriteCommonResponse mFavouriteShopsResponse = (FavouriteCommonResponse) responseObj;

            if (mFavouriteShopsResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {

                if (mFavouriteShopsResponse.getResult() != null
                        && mFavouriteShopsResponse.getResult().size() != 0) {
                    orderList = mFavouriteShopsResponse.getResult();
                    adapter = new OneTouchAdapter(OneTouchFragment.this, R.layout.fragment_favourite, orderList);
                    favourite_list.setAdapter(adapter);
                } else {
                    moveToSelectList();
                }
            } else {
                DialogManager
                        .showCustomAlertDialog(getActivity(),
                                OneTouchFragment.this,
                                mFavouriteShopsResponse.getMsg());
            }

        }
        if (responseObj instanceof RateResponseEntity) {
            RateResponseEntity mRatingResponse = (RateResponseEntity) responseObj;
            if (mRatingResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)
                    || mRatingResponse.getStatus().equalsIgnoreCase("success")) {
                DialogManager.showCustomAlertDialog(getActivity(),
                        OneTouchFragment.this, getString(R.string.rating_added));
                orderList.get(mSelectedPos).setAvgRating(
                        String.valueOf(mRatingResponse.getResult()));


                adapter.notifyDataSetChanged();


            } else {

                DialogManager.showCustomAlertDialog(getActivity(), this,
                        mRatingResponse.getMsg());
            }
        }
        super.onRequestSuccess(responseObj);
    }

    @Override
    public void onRequestFailure(RetrofitError errorCode) {

        super.onRequestFailure(errorCode);
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
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;

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
                callPharmacyRatingService(rating_value, mShopId);
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

    public void callUnFavouriteservice(String mShopId) {
        String UserID = GlobalMethods.getUserID(getActivity());
        APIRequestHandler.getInstance()
                .addFavourite(mShopId, UserID, "2", this);

    }


    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }

    public void selectPrescription(MapPropertyEntity pharmacy) {

        AppConstants.FRAG = AppConstants.ONE_TOUCH_ORDER;
        HomeScreen.mBackMove = this;
        mMapDetailEntity = pharmacy;
        ProfileImageSelectionUtil.showscanPopUp(getActivity());
    }
}
