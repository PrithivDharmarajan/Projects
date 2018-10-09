package com.calix.calixgigaspireapp.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

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

    @BindView(R.id.iot_settings_parent_lay)
    ViewGroup mIOTSettingsViewGroup;

    @BindView(R.id.iot_settings_header_bg_lay)
    RelativeLayout mIOTSettingsHeaderBgLay;

    @BindView(R.id.iot_settings_header_img)
    ImageView mIOTSettingsHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.iot_settings_device_name_edt)
    EditText mIOTDeviceNameEdt;

    @BindView(R.id.iot_settings_device_loc_edt)
    EditText mIOTDeviceLocNameEdt;

    @BindView(R.id.device_ip_num_txt)
    TextView mDeviceIpNumTxt;

    @BindView(R.id.mac_num_txt)
    TextView mMacNumTxt;

    private WebSocket mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_iot_settings);
        initView();
    }


    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTSettingsViewGroup);

        connectAlmondToRouter();
        setHeaderView();

        /*Keypad button action*/
        mIOTDeviceLocNameEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 100 || actionId == EditorInfo.IME_ACTION_DONE) {
                    updateDeviceNameAndLoc();
                }
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTSettingsHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTSettingsHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTSettingsHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTRouterSettings.this)));
                    mIOTSettingsHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTRouterSettings.this), 0, 0);
                    mIOTSettingsHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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
    @OnClick({R.id.header_left_img_lay, R.id.cont_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.cont_btn:
                updateDeviceNameAndLoc();
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
                jsonObject.put("Name", mIOTDeviceNameEdt.getText().toString().trim());
                jsonObject.put("Location", mIOTDeviceLocNameEdt.getText().toString().trim());
                mWebSocket.send(String.valueOf(jsonObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            connectAlmondToRouter();
        }
    }


    private void setData() {

        IOTDeviceConfigResponse iotDeviceConfigRes = AppConstants.IOT_DEVICE_DETAILS;

        mIOTDeviceNameEdt.setText(iotDeviceConfigRes.getDeviceName());
        mHeaderTxt.setText(iotDeviceConfigRes.getDeviceName());
        mIOTDeviceLocNameEdt.setText(iotDeviceConfigRes.getLocationName());
        mDeviceIpNumTxt.setText("");
        mMacNumTxt.setText("");
    }

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
    @Override
    public void onSocketRequestMessage(String resObj) {
        super.onSocketRequestMessage(resObj);

        try {
            JSONObject json = new JSONObject(resObj);
            String commandTypeStr = json.getString("CommandType");
            sysOut("St CommandType" + commandTypeStr);
            if (commandTypeStr.equalsIgnoreCase("UpdateDeviceName")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DialogManager.getInstance().showIOTReadyToUseAlertPopup(IOTRouterSettings.this, new InterfaceBtnCallback() {
                            @Override
                            public void onPositiveClick() {
                                onBackPressed();
                            }
                        });
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        previousScreen(IOTDeviceList.class);
    }
}