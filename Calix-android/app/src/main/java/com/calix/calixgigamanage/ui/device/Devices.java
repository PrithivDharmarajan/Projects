package com.calix.calixgigamanage.ui.device;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.DeviceAdapter;
import com.calix.calixgigamanage.adapter.DeviceSpinnerAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CategoriesEntity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.DeviceFilterEntity;
import com.calix.calixgigamanage.output.model.DeviceFilterListResponse;
import com.calix.calixgigamanage.output.model.DeviceListResponse;
import com.calix.calixgigamanage.output.model.FilterDeviceListResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Devices extends BaseActivity {

    @BindView(R.id.pc_device_par_lay)
    RelativeLayout mParentControlDeviceLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.device_header_bg_lay)
    RelativeLayout mDeviceHeaderBgLay;

    @BindView(R.id.device_header_msg_lay)
    RelativeLayout mDeviceHeaderMsgLay;

    @BindView(R.id.pc_devices_recycler_lay)
    RecyclerView mControlDeviceRecyclerView;

    @BindView(R.id.device_spinner_card_view)
    CardView mDeviceSpinnerCardView;

    @BindView(R.id.arrow_img)
    ImageView mArrowImg;

    @BindView(R.id.device_spinner_recycler_view)
    RecyclerView mDeviceSpinnerRecyclerView;

    @BindView(R.id.device_count_txt)
    TextView mDeviceCountTxt;

    @BindView(R.id.spinner_filter_name_txt)
    TextView mSpinnerFilterNameTxt;

    private boolean isDeviceListAPIBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_devices);
        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlDeviceLay);

        setHeaderView();

        deviceListAPI();
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.device_spinner_lay, R.id.device_lay, R.id.device_arrow_up_lay, R.id.arrow_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.device_lay:
                mDeviceSpinnerCardView.setVisibility(View.GONE);
                mArrowImg.setRotation(0);
                break;
            case R.id.device_spinner_lay:
            case R.id.device_arrow_up_lay:
            case R.id.arrow_img:
                mDeviceSpinnerCardView.setVisibility(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mArrowImg.setRotation(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? 180 : 0);
                break;

        }
    }

    private void setHeaderView() {

        /*set header changes*/
        mDeviceHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.device));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDeviceHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    int dropDownHeightInt = heightInt + getResources().getDimensionPixelSize(R.dimen.size35) + NumberUtil.getInstance().getStatusBarHeight(Devices.this);

                    mDeviceHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(Devices.this)));
                    mDeviceHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(Devices.this), 0, 0);
                    mDeviceHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));


                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.size145), RelativeLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0, dropDownHeightInt, 0, 0);
                    layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    mDeviceSpinnerCardView.setLayoutParams(layoutParams);
                }

            });
        }

    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();

    }


    /*API calls*/
    private void deviceListAPI() {
        isDeviceListAPIBool = true;
        AppConstants.IOT_DEVICE_CATEGORIES = new CategoriesEntity();
        APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    private void deviceFilterListAPI() {
        isDeviceListAPIBool = false;
        APIRequestHandler.getInstance().deviceFilterListAPICall(this);

    }


    /*Populate the values*/
    private void setDeviceListData(DeviceListResponse deviceListResponse) {
        mDeviceCountTxt.setText(String.valueOf(deviceListResponse.getDevices().size()));
        setDeviceListAdapter(deviceListResponse.getDevices());
    }

    private void setDeviceFilterListData(FilterDeviceListResponse deviceListResponse) {
        mDeviceCountTxt.setText(String.valueOf(deviceListResponse.getDevices().size()));
        setDeviceListAdapter(deviceListResponse.getDevices());
    }

    /*Set device list adapter*/
    private void setDeviceListAdapter(ArrayList<DeviceEntity> deviceListResponse) {

        mControlDeviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mControlDeviceRecyclerView.setNestedScrollingEnabled(false);
        mControlDeviceRecyclerView.setAdapter(new DeviceAdapter(deviceListResponse, this));

    }

    /*Set filter adapter*/
    private void setDeviceFilterListAdapter(ArrayList<DeviceFilterEntity> deviceFilterListResponse) {
        InterfaceEdtBtnCallback interfaceEdtBtnCallback;
        interfaceEdtBtnCallback = new InterfaceEdtBtnCallback() {
            @Override
            public void onPositiveClick(String editStr) {
                isDeviceListAPIBool = true;
                mSpinnerFilterNameTxt.setText(editStr);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        ArrayList<DeviceFilterEntity> deviceFilterRes = new ArrayList<>();
        /*Manually we add All value to adapter first pos*/
        if (deviceFilterListResponse.size() > 1) {
            DeviceFilterEntity deviceFilterEntity = new DeviceFilterEntity();
            deviceFilterEntity.setName(getString(R.string.all));
            deviceFilterRes.add(deviceFilterEntity);
        } else if (deviceFilterListResponse.size() > 0) {
            mSpinnerFilterNameTxt.setText(deviceFilterListResponse.get(0).getName());
        }
        deviceFilterRes.addAll(deviceFilterListResponse);

        mDeviceSpinnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDeviceSpinnerRecyclerView.setAdapter(new DeviceSpinnerAdapter(deviceFilterRes, interfaceEdtBtnCallback, mDeviceSpinnerCardView, mArrowImg, this));


    }


    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse deviceListResponse = (DeviceListResponse) resObj;
            deviceFilterListAPI();
            setDeviceListData(deviceListResponse);
        } else if (resObj instanceof FilterDeviceListResponse) {
            FilterDeviceListResponse deviceFilterListRes = (FilterDeviceListResponse) resObj;
            setDeviceFilterListData(deviceFilterListRes);
        } else if (resObj instanceof DeviceFilterListResponse) {
            DeviceFilterListResponse deviceFilterListResponse = (DeviceFilterListResponse) resObj;
            setDeviceFilterListAdapter(deviceFilterListResponse.getFilters());
        }
    }


    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (isDeviceListAPIBool) deviceListAPI();
                            else deviceFilterListAPI();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
