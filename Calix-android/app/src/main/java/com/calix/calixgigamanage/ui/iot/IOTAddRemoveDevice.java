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
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;


public class IOTAddRemoveDevice extends BaseActivity {

    @BindView(R.id.add_remove_iot_parent_lay)
    ViewGroup mAddRemoveIOTViewGroup;

    @BindView(R.id.add_remove_iot_header_bg_lay)
    RelativeLayout mAddRemoveIOTHeaderBgLay;

    @BindView(R.id.add_remove_iot_header_msg_lay)
    RelativeLayout mAddRemoveIOTHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    private WebSocket mWebSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_remove_iot);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddRemoveIOTViewGroup);

        setHeaderView();

        connectAlmondToRouter();
    }

    private void setHeaderView() {

        /*set header changes*/
        mAddRemoveIOTHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_iot_device));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAddRemoveIOTHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mAddRemoveIOTHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTAddRemoveDevice.this)));
                    mAddRemoveIOTHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTAddRemoveDevice.this), 0, 0);
                    mAddRemoveIOTHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay, R.id.iot_add_lay, R.id.remove_device_lay, R.id.remove_all_device_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.iot_add_lay:
                nextScreen(IOTAddDevice.class);
                break;
            case R.id.remove_device_lay:
                nextScreen(IOTRemoveDeviceList.class);
                break;
            case R.id.remove_all_device_lay:
                DialogManager.getInstance().showOptionPopup(this, getString(R.string.remove_all_iot_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                    @Override
                    public void onPositiveClick() {
                        if (mWebSocket != null) {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("CommandType", "RemoveAllDevices");
                                jsonObject.put("MobileInternalIndex", "898");
                                mWebSocket.send(String.valueOf(jsonObject));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            connectAlmondToRouter();
                        }
                    }

                    @Override
                    public void onNegativeClick() {

                    }
                });


                break;
        }
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
            String st = json.getString("CommandType");
            if (mWebSocket != null && st.equalsIgnoreCase("DynamicAllDevicesRemoved")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogManager.getInstance().showToast(IOTAddRemoveDevice.this, "Device Removed Successfully...");
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
        backScreen();
    }
}