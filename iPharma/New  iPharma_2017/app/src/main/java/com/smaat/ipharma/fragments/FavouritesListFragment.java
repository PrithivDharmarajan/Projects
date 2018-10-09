package com.smaat.ipharma.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smaat.ipharma.R;
import com.smaat.ipharma.adapter.MapListAdapter;
import com.smaat.ipharma.apiinterface.APIRequestHandler;
import com.smaat.ipharma.entity.MapPropertyEntity;
import com.smaat.ipharma.entity.MapResponse;
import com.smaat.ipharma.entity.RateResponseEntity;
import com.smaat.ipharma.entity.WriteReviewEntity;
import com.smaat.ipharma.main.BaseFragment;
import com.smaat.ipharma.ui.HomeScreen;
import com.smaat.ipharma.utils.AppConstants;
import com.smaat.ipharma.utils.DialogManager;
import com.smaat.ipharma.utils.GPSTracker;
import com.smaat.ipharma.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 1/20/2017.
 */

public class FavouritesListFragment extends BaseFragment{

    String UserID = "";
    @BindView(R.id.favourite_list)
    ListView m_Favouritelist;
    GPSTracker gps_track;
    double latitude = 0.0;
    double longitude = 0.0;
    MapListAdapter adapter;

    @BindView(R.id.internetconnection_layout)
    RelativeLayout internetconnection_layout;


    @BindView(R.id.refresh_btn)
    ImageView refresh_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_favourite,
                container, false);
        ButterKnife.bind(this, rootview);
        setupUI(rootview);
        gps_track = new GPSTracker(getActivity());
        latitude = gps_track.getLatitude();
        longitude = gps_track.getLongitude();
        UserID = GlobalMethods.getUserID(getActivity());
        LoadFavouriteList();

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFavouriteList();
            }
        });
        return rootview;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void LoadFavouriteList() {

            APIRequestHandler.getInstance().GetFavList(UserID,String.valueOf(latitude),String.valueOf(longitude),internetconnection_layout,m_Favouritelist,
                    FavouritesListFragment.this);


    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeScreen) getActivity()).setToolbarTitle(getString(R.string.favourite),getString(R.string.home));

    }


    @Override
    public void onRequestSuccess(Object responseObj) {

        if(responseObj instanceof MapResponse)
        {

            m_Favouritelist.setVisibility(View.VISIBLE);
            if(internetconnection_layout.getVisibility()==View.VISIBLE)
            {
                internetconnection_layout.setVisibility(View.GONE);
            }

            MapResponse mMapResponse = (MapResponse) responseObj;
            if(mMapResponse.getResult().size()>0)
            {
                setListAdapter(mMapResponse.getResult());

            }else{
                //setListAdapter(mMapResponse.getResult());
                m_Favouritelist.setVisibility(View.GONE);
                showMsgPopup(getActivity(),getString(R.string.app_name),getString(R.string.no_fav_results));
            }


        }else if(responseObj instanceof WriteReviewEntity)
        {

            WriteReviewEntity mWriteReviewResponse = (WriteReviewEntity) responseObj;

            if (mWriteReviewResponse.getStatus().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                //callFavouriteService();
                adapter.removefavourites(AppConstants.CLICKED_POSITION);
                if(adapter.getFavsize()==0)
                {
                    showMsgPopup(getActivity(),
                            getString(R.string.app_name),
                            getString(R.string.no_fav_results));
                }
            } else {
                showMsgPopup(getActivity(),
                        getString(R.string.app_name), mWriteReviewResponse.getMsg());
            }
        }else if(responseObj instanceof RateResponseEntity)
        {
            RateResponseEntity rateresponse = (RateResponseEntity) responseObj;
            DialogManager.showMsgPopup(getActivity(),"",rateresponse.getMsg());
        }

    }
    private void setListAdapter(ArrayList<MapPropertyEntity> mPharmacyList) {
        AppConstants.IS_FAVOURITE = true;
        if (mPharmacyList != null) {
            adapter = new MapListAdapter(getActivity(),AppConstants.IS_FAVOURITE,
                    mPharmacyList,this);
            m_Favouritelist.setAdapter(adapter);


        }
    }
    public void showMsgPopup(final Context mContext, String title, String msg) {
        mDialog = DialogManager.getDialog(mContext, R.layout.popup_msg_layout);

        ImageView m_btnYes = (ImageView) mDialog.findViewById(R.id.yes_btn);
        ImageView m_btnNo = (ImageView) mDialog.findViewById(R.id.close_button);
        m_btnNo.setVisibility(View.GONE);
        TextView mTitte=(TextView)mDialog.findViewById(R.id.title_text);
        mTitte.setText(msg);

        m_btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                getActivity().onBackPressed();


            }
        });

        mDialog.show();
    }
}
