package com.calix.calixgigamanage.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.InterfaceTwoEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;


public class IOTPairZigBeeDevice extends BaseActivity {

    @BindView(R.id.iot_pair_zigbee_parent_lay)
    ViewGroup mIOTPairZigBeeViewGroup;

    @BindView(R.id.iot_pair_zigbee_header_bg_lay)
    RelativeLayout mIOTPairZigBeeHeaderBgLay;

    @BindView(R.id.iot_pair_zigbee_header_msg_lay)
    RelativeLayout mIOTPairZigBeeHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.searching_txt)
    TextView mSearchingTxt;

    @BindView(R.id.config_indicator_view)
    AVLoadingIndicatorView mConfigIndicatorView;

    @BindView(R.id.config_success_img)
    ImageView mConfigSuccessImg;

    @BindView(R.id.config_status_msg_txt)
    TextView mConfigStatusMsgTxt;

    @BindView(R.id.cancel_cont_btn)
    Button mCancelContBtn;

    private WebSocket mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_iot_pair_zigbee);
        initView();
    }


    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTPairZigBeeViewGroup);

        setHeaderView();

        headerTxt(getString(R.string.searching));

        connectAlmondToRouter();

    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTPairZigBeeHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTPairZigBeeHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTPairZigBeeHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTPairZigBeeDevice.this)));
                    mIOTPairZigBeeHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTPairZigBeeDevice.this), 0, 0);
                    mIOTPairZigBeeHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
                }

            });
        }

    }

    private void headerTxt(final String headerTxtStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchingTxt.setText(headerTxtStr);
                mHeaderTxt.setText(headerTxtStr);
            }
        });
    }

    /*Screen orientation Changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderView();
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.cancel_cont_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.cancel_cont_btn:
                if (mConfigSuccessImg.getVisibility() == View.GONE) {
                    if (mWebSocket != null) {
                        mWebSocket.cancel();
                    }
                    onBackPressed();
                } else
                    nextScreen(IOTRouterSettings.class);
                break;
        }
    }


    /*API request success and failure*/

    @Override
    public void onSocketRequestOpen(WebSocket webSocket, Response response) {
        super.onSocketRequestOpen(webSocket, response);
        mWebSocket = webSocket;
        logInIOTRouter();
    }

    private void logInIOTRouter() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CommandType", "Login");
            jsonObject.put("MobileInternalIndex", "898");
            jsonObject.put("LongSecret", AppConstants.WEB_SOCKET_CALIX_TOKEN);
            mWebSocket.send(String.valueOf(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addDevice(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CommandType", "AddDevice");
            jsonObject.put("MobileInternalIndex", "898");
            mWebSocket.send(String.valueOf(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSocketRequestMessage(final String resObj) {
        super.onSocketRequestMessage(resObj);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject json = new JSONObject(resObj);
                    String commandTypeStr = json.getString("CommandType");
                    sysOut("St CommandType" + commandTypeStr);

                    if (commandTypeStr.equalsIgnoreCase("Login")) {
                        addDevice();
                    }
                    if (commandTypeStr.equalsIgnoreCase("GettingInfo")) {
                        headerTxt(getString(R.string.getting_device_info));
                    }
                    if (mWebSocket != null && commandTypeStr.equalsIgnoreCase("AddedDeviceDefaultName")) {

                        final String deviceNameStr = json.get("Name") != null ? json.getString("Name") : "";
                        final String locationStr = json.get("Location") != null ? json.getString("Location") : "";

                        headerTxt(getString(R.string.adding_device));
                        DialogManager.getInstance().showEdtIOTDeviceNameLocPopup(IOTPairZigBeeDevice.this, deviceNameStr, locationStr, new InterfaceTwoEdtBtnCallback() {
                            @Override
                            public void onPositiveClick(String deviceNameStr, String locStr) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    AppConstants.IOT_DEVICE_DETAILS.setLocationName(locStr);
                                    AppConstants.IOT_DEVICE_DETAILS.setDeviceName(deviceNameStr);

                                    jsonObject.put("CommandType", "SetName");
                                    jsonObject.put("MobileInternalIndex", "898");
                                    jsonObject.put("Name", deviceNameStr);
                                    jsonObject.put("Location", locStr);
                                    mWebSocket.send(String.valueOf(jsonObject));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onNegativeClick() {

                            }
                        });

                    }
                    if (commandTypeStr.equalsIgnoreCase("SetName")) {

                        headerTxt(getString(R.string.device_added));

                    }
                    if (mHeaderTxt.getText().toString().equalsIgnoreCase(getString(R.string.device_added)) && commandTypeStr.equalsIgnoreCase("DynamicDeviceAdded")) {
                        try {
                            getConnectedDeviceID(json.getJSONObject("Devices"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mConfigIndicatorView.hide();
                        mConfigIndicatorView.setVisibility(View.GONE);
                        mConfigSuccessImg.setVisibility(View.VISIBLE);
                        mConfigStatusMsgTxt.setText(getString(R.string.device_finalize_setup));
                        mCancelContBtn.setVisibility(View.VISIBLE);
                        mCancelContBtn.setText(getString(R.string.cont));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onSocketRequestClosing(String resObj) {
        super.onSocketRequestClosing(resObj);
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }

    private void getConnectedDeviceID(JSONObject jObject) {
        Iterator<String> keys = jObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                JSONObject innerJObject = jObject.getJSONObject(key).getJSONObject("Data");
                if (innerJObject.getString("Name").equalsIgnoreCase(AppConstants.IOT_DEVICE_DETAILS.getDeviceName())) {
                    AppConstants.IOT_DEVICE_DETAILS.setSecurifiId(innerJObject.getString("ID"));
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


}