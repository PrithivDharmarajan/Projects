package com.smaat.ipharma.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.OneTouchAdapterSelector;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.model.MapResponse;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.util.AppConstants;
import com.smaat.ipharma.util.DialogManager;
import com.smaat.ipharma.util.DialogMangerCallback;
import com.smaat.ipharma.util.GPSTracker;
import com.smaat.ipharma.util.GlobalMethods;
import com.smaat.ipharma.util.TypefaceSingleton;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class OneTouchListSelectFragment extends BaseFragment implements
        DialogMangerCallback {
    private ListView favourite_list;

    private GPSTracker tracker;

    public static String mUserID;
    public static int mSelectedPos, isFavorite;
    ArrayList<MapPropertyEntity> orderList;
    private OneTouchAdapterSelector adapter;
    private float mRate;
    public static MapPropertyEntity mMapDetailEntity;
    private float map_distance = (float) 10.0;


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

        getPharmacy(); // API Call
        initComponents(view);
        HomeScreen.mHeaderLeftLay.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {

                AppConstants.FRAG = AppConstants.ONE_TOUCH_ORDER_SELECT;
                HomeScreen.onBackMove(getActivity());
            }
        });
    }

    private void getPharmacy() {

        String mUserID = GlobalMethods.getUserID(getActivity());
        tracker = new GPSTracker(getActivity());
        String mLat = "";
        String mLongit = "";
        if (tracker != null) {
            mLat = tracker.getLatitude() + "";
            mLongit = tracker.getLongitude() + "";
        }

        APIRequestHandler.getInstance().getSearchPharmacies(mUserID, mLat,
                mLongit, "", map_distance,
                this);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {

        if (responseObj instanceof WriteReviewEntity) {

            WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;

            if (mWriteReviewResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {


                if(isFavorite == 1){
                    DialogManager.showCustomAlertDialog(getActivity(),
                            OneTouchListSelectFragment.this, "Selected Pharmacy added to your One Touch Order Pharmacy List, Please go back to place your order.");

                } else {
                    DialogManager.showCustomAlertDialog(getActivity(),
                            OneTouchListSelectFragment.this, "Selected Pharmacy removed from your One Touch Order Pharmacy List, Please go back to place your order.");

                }
                orderList.get(mSelectedPos).setIsFav(isFavorite+"");


                adapter.notifyDataSetChanged();
            } else {
                DialogManager.showCustomAlertDialog(getActivity(),
                        OneTouchListSelectFragment.this, mWriteReviewResponse.getMsg());
            }

        }

        if (responseObj instanceof MapResponse) {

            MapResponse mFavouriteShopsResponse = (MapResponse) responseObj;

            if (mFavouriteShopsResponse.getStatus().equalsIgnoreCase(
                    AppConstants.SUCCESS_CODE)) {

                if (mFavouriteShopsResponse.getResult() != null
                        && mFavouriteShopsResponse.getResult().size() != 0) {
                    orderList = mFavouriteShopsResponse.getResult();
                    adapter = new OneTouchAdapterSelector(OneTouchListSelectFragment.this, R.layout.fragment_favourite, orderList);
                    favourite_list.setAdapter(adapter);


                } else {
                    favourite_list.removeAllViews();
                    DialogManager.showCustomAlertDialog(getActivity(),
                            OneTouchListSelectFragment.this,
                            getString(R.string.no_favourite_shop));
                }
            } else {
                DialogManager
                        .showCustomAlertDialog(getActivity(),
                                OneTouchListSelectFragment.this,
                                mFavouriteShopsResponse.getMsg());
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


    public void callFavouriteservice(String isCheck,String mShopId) {
        String UserID = GlobalMethods.getUserID(getActivity());

        APIRequestHandler.getInstance().addFavourite(mShopId, UserID, isCheck,
                this);
    }



    public void onItemclick(String SelctedItem, int pos) {
        // TODO Auto-generated method stub

    }

    public void onOkclick() {
        // TODO Auto-generated method stub

    }


}
