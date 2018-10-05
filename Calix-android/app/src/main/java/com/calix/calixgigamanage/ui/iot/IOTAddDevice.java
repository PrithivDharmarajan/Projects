package com.calix.calixgigamanage.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.AddDeviceIOTAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.AddIOTDeviceEntity;
import com.calix.calixgigamanage.output.model.AddIOTDeviceResponse;
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


public class IOTAddDevice extends BaseActivity {

    @BindView(R.id.add_iot_device_parent_lay)
    ViewGroup mAddIOTDeviceViewGroup;

    @BindView(R.id.add_iot_device_header_bg_lay)
    RelativeLayout mAddIOTDeviceHeaderBgLay;

    @BindView(R.id.add_iot_device_header_msg_lay)
    RelativeLayout mAddIOTDeviceHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.add_iot_device_recycler_view)
    RecyclerView mAddIOTDeviceRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_device_iot);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddIOTDeviceViewGroup);

        addIOIDeviceAPICall();

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        mAddIOTDeviceHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_iot_device));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAddIOTDeviceHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mAddIOTDeviceHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTAddDevice.this)));
                    mAddIOTDeviceHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTAddDevice.this), 0, 0);
                    mAddIOTDeviceHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    private void addIOIDeviceAPICall() {
        sysOut(PreferenceUtil.getAuthorization(this));
        APIRequestHandler.getInstance().addIOTDeviceAPICall(this);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof AddIOTDeviceResponse) {
            AddIOTDeviceResponse addIOTResponse = (AddIOTDeviceResponse) resObj;
            setAdapter(addIOTResponse.getDeviceTypes());
        }
    }

    private void setAdapter(ArrayList<AddIOTDeviceEntity> deviceTypes) {
        mAddIOTDeviceRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAddIOTDeviceRecyclerView.setAdapter(new AddDeviceIOTAdapter(deviceTypes, this));
        mAddIOTDeviceRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onRequestFailure(Object inputModelObj, Throwable t) {
        super.onRequestFailure(inputModelObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                    addIOIDeviceAPICall();
                }
            });
        }
    }



    @Override
    public void onBackPressed() {
        backScreen();
    }
}