package com.calix.calixgigamanage.ui.iot;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.LocationAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.LocationEntry;
import com.calix.calixgigamanage.output.model.LocationResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.PreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IOTDeviceLocationList extends BaseActivity {

    @BindView(R.id.loc_parent_lay)
    ViewGroup mLocationViewGroup;

    @BindView(R.id.loc_header_bg_lay)
    RelativeLayout mLocationHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.loc_RecyclerView)
    RecyclerView mLocationRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_location);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mLocationViewGroup);


        setHeaderView();
        locationAPICall();

    }

    /*Populate the location values*/
    private void setData(LocationResponse locationResponse) {

        setAdapter(locationResponse.getLocations());

    }

    /*set Adapter*/
    private void setAdapter(ArrayList<LocationEntry> locations) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mLocationRecyclerView.setLayoutManager(linearLayoutManager);
        mLocationRecyclerView.setAdapter(new LocationAdapter(locations,this));

    }

    /*Location API calls*/
    private void locationAPICall() {
        sysOut(PreferenceUtil.getAuthorization(this));
        APIRequestHandler.getInstance().locationAPICall(this);
    }

    /*Location API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof LocationResponse) {
            LocationResponse locationResponse = (LocationResponse) resObj;
            setData(locationResponse);
        }
    }

    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj,t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                    locationAPICall();
                }
            });
        }
    }


    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.location));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mLocationHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mLocationHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTDeviceLocationList.this)));
                    mLocationHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTDeviceLocationList.this), 0, 0);
                }

            });
        }

    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}