package com.calix.calixgigamanage.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IOTAddZigBeeDevice extends BaseActivity {

    @BindView(R.id.iot_add_zigbee_parent_lay)
    ViewGroup mIOTAddZigBeeViewGroup;

    @BindView(R.id.iot_add_zigbee_header_bg_lay)
    RelativeLayout mIOTAddZigBeeHeaderBgLay;

    @BindView(R.id.iot_add_zigbee_header_msg_lay)
    RelativeLayout mIOTAddZigBeeHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_iot_add_zigbee);
        initView();
    }


    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTAddZigBeeViewGroup);
        setHeaderView();

    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTAddZigBeeHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_zig_bee));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTAddZigBeeHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTAddZigBeeHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTAddZigBeeDevice.this)));
                    mIOTAddZigBeeHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTAddZigBeeDevice.this), 0, 0);
                    mIOTAddZigBeeHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay, R.id.cancel_btn, R.id.cont_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
            case R.id.cancel_btn:
                onBackPressed();
                break;
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("CommandType", "RemoveAllDevices");
//                    jsonObject.put("MobileInternalIndex", "898");
////            mSocket.emit("Request", jsonObject);
////            mSocket.send(jsonObject);
//                    mWebSocket.send(String.valueOf(jsonObject));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            case R.id.cont_btn:

                iotDeviceConfigAPI();
                break;

        }

    }

    /*API call*/
    private void iotDeviceConfigAPI() {
        APIRequestHandler.getInstance().iotDeviceConfigAPICall(String.valueOf(AppConstants.ADD_IOT_DEVICE_DETAILS.getId()), this);
    }



    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof IOTDeviceConfigResponse) {
            AppConstants.IOT_DEVICE_DETAILS = (IOTDeviceConfigResponse) resObj;
            nextScreen(IOTPairZigBeeDevice.class);
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
                    iotDeviceConfigAPI();
                }
            });
        }
    }



    @Override
    public void onBackPressed() {
        backScreen();
    }
}