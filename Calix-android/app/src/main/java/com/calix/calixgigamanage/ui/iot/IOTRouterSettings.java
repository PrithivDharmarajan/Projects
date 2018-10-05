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
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;

/**
 * Created by prithiviraj on 11/22/2017.
 */

public class IOTRouterSettings extends BaseActivity {

    @BindView(R.id.iot_router_settings_parent_lay)
    ViewGroup mIOTRouterSettingsViewGroup;

    @BindView(R.id.iot_router_settings_header_bg_lay)
    RelativeLayout mIOTRouterSettingsHeaderBgLay;

    @BindView(R.id.iot_router_settings_header_msg_lay)
    RelativeLayout mIOTRouterSettingsHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.iot_device_name_txt)
    TextView mIOTDeviceNameTxt;


    @BindView(R.id.location_name_txt)
    TextView mLocationNameTxt;

    @BindView(R.id.device_ip_num_txt)
    TextView mDeviceIpNumTxt;

    @BindView(R.id.mac_num_txt)
    TextView mMacNumTxt;

    private WebSocket mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_iot_router_settings);
        initView();
    }


    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTRouterSettingsViewGroup);

        connectAlmondToRouter();
        setHeaderView();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTRouterSettingsHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTRouterSettingsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTRouterSettingsHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTRouterSettings.this)));
                    mIOTRouterSettingsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTRouterSettings.this), 0, 0);
                    mIOTRouterSettingsHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay, R.id.device_editor_img, R.id.location_edt_img, R.id.cont_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.device_editor_img:
                DialogManager.getInstance().showEdtDeviceNamePopup(this, mIOTDeviceNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtStr) {
                        mIOTDeviceNameTxt.setText(edtStr);
                        mHeaderTxt.setText(edtStr);
                        updateDeviceNameAndLoc();
                    }
                });
                break;
            case R.id.location_edt_img:
                DialogManager.getInstance().showEdtDeviceLocPopup(this, mLocationNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtStr) {
                        mLocationNameTxt.setText(edtStr);
                        updateDeviceNameAndLoc();

                    }
                });
                break;
            case R.id.cont_btn:
                nextScreen(IOTDeviceReadyToUse.class);
                break;
        }

    }

    private void updateDeviceNameAndLoc() {
        if (mWebSocket != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CommandType", "UpdateDeviceName");
                jsonObject.put("MobileInternalIndex", "898");
                jsonObject.put("ID", AppConstants.IOT_DEVICE_DETAILS.getSecurifiId());
                jsonObject.put("Name", mIOTDeviceNameTxt.getText().toString().trim());
                jsonObject.put("Location", mLocationNameTxt.getText().toString().trim());
                mWebSocket.send(String.valueOf(jsonObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            connectAlmondToRouter();
        }
    }

//    /*View onCheckedChanged*/
//    @OnCheckedChanged({R.id.custom_device_switch_compat})
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//            case R.id.custom_device_switch_compat:
//                mInvisibleView.setVisibility(isChecked ? View.GONE : View.VISIBLE);
//                break;
//        }
//    }

    private void setData() {

        IOTDeviceConfigResponse iotDeviceConfigRes = AppConstants.IOT_DEVICE_DETAILS;

        mIOTDeviceNameTxt.setText(iotDeviceConfigRes.getDeviceName());
        mHeaderTxt.setText(iotDeviceConfigRes.getDeviceName());
        mLocationNameTxt.setText(iotDeviceConfigRes.getLocationName());
        mDeviceIpNumTxt.setText("");
        mMacNumTxt.setText("");
    }

    @Override
    public void onSocketRequestOpen(WebSocket webSocket, Response response) {
        super.onSocketRequestOpen(webSocket, response);
        mWebSocket = webSocket;
    }

    @Override
    public void onSocketRequestMessage(String resObj) {
        super.onSocketRequestMessage(resObj);

        try {
            JSONObject json = new JSONObject(resObj);
            String commandTypeStr = json.getString("CommandType");
            sysOut("St CommandType" + commandTypeStr);
            if (commandTypeStr.equalsIgnoreCase("UpdateDeviceName")) {
//                nextScreen(IOTDeviceReadyToUse.class);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}