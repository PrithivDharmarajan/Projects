package com.calix.calixgigamanage.ui.dashboard;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.DeviceListAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.CategoriesEntity;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.DeviceListResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.TextUtil;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DeviceDetailsList extends BaseActivity {

    @BindView(R.id.device_list_parent_lay)
    ViewGroup mDeviceListViewGroup;

    @BindView(R.id.device_list_header_bg_lay)
    RelativeLayout mDeviceListHeaderBgLay;

    @BindView(R.id.device_list_header_msg_lay)
    RelativeLayout mDeviceListHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.device_count_txt)
    TextView mDeviceListDeviceCountTxt;

    @BindView(R.id.device_img)
    ImageView mDeviceImg;

    @BindView(R.id.device_list_txt)
    TextView mDeviceListTxt;

    @BindView(R.id.device_list_recycler_view)
    RecyclerView mDeviceListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_device_details_list);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mDeviceListViewGroup);

        setHeaderView();

        CategoriesEntity deviceCategories = AppConstants.IOT_DEVICE_CATEGORIES;
        mDeviceImg.setImageResource(deviceListImg(deviceCategories.getType()));
        mHeaderTxt.setText(TextUtil.getInstance().capitalizeStr(deviceCategories.getName()));
        mDeviceListTxt.setText(TextUtil.getInstance().capitalizeStr(String.format(getString(R.string.iot_device_connected), deviceCategories.getName())));

        /*API call*/
        deviceListAPI();
    }

    private void setHeaderView() {

        /*set header changes*/
        mDeviceListHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDeviceListHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mDeviceListHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(DeviceDetailsList.this)));
                    mDeviceListHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(DeviceDetailsList.this), 0, 0);
                    mDeviceListHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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

    /*find the IOT device Image*/
    private int deviceListImg(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                return R.drawable.phone_white;
            case 2:
                return R.drawable.computer_white;
            case 3:
                return R.drawable.console_white;
            case 4:
                return R.drawable.storage_white;
            case 5:
                return R.drawable.printer_white;
            case 6:
                return R.drawable.television_white;
            case 7:
                return R.drawable.network_white;
            case 8:
                return R.drawable.camera_white;
            default:
                return R.mipmap.ic_launcher;
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



    /*API calls*/
    private void deviceListAPI() {
        APIRequestHandler.getInstance().deviceListAPICall(String.valueOf(AppConstants.IOT_DEVICE_CATEGORIES.getType()), this);
    }


    /*Populate the values*/
    private void setData(DeviceListResponse consoleDeviceListResponse) {
        mDeviceListDeviceCountTxt.setText(String.valueOf(consoleDeviceListResponse.getDevices().size()));
        setAdapter(consoleDeviceListResponse.getDevices());
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<DeviceEntity> consoleDeviceListRes) {
        mDeviceListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDeviceListRecyclerView.setNestedScrollingEnabled(false);
        mDeviceListRecyclerView.setAdapter(new DeviceListAdapter(consoleDeviceListRes, this));

    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse consoleListResponse = (DeviceListResponse) resObj;
            setData(consoleListResponse);
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
                        public void onPositiveClick() {deviceListAPI();
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}