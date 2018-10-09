package com.calix.calixgigaspireapp.ui.iot;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.iot.IOTAddDeviceAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.AddIOTDeviceEntity;
import com.calix.calixgigaspireapp.output.model.AddIOTDeviceResponse;
import com.calix.calixgigaspireapp.output.model.IOTDeviceConfigResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;


public class IOTAddDevice extends BaseActivity {

    @BindView(R.id.add_iot_device_parent_lay)
    ViewGroup mAddIOTDeviceViewGroup;

    @BindView(R.id.add_iot_device_header_bg_lay)
    RelativeLayout mAddIOTDeviceHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.add_iot_device_recycler_view)
    RecyclerView mAddIOTDeviceRecyclerView;

    private Dialog mAddIOTAlertDialog;
    private TextView mAlertDeviceStatusHeaderTxt, mConfigStatusMsgTxt;
    private AVLoadingIndicatorView mConfigIndicatorView;
    private ImageView mConfigSuccessImg;
    private Button mAlertPositiveBtn, mAlertNegativeBtn;
    private WebSocket mWebSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_add_iot_device);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mAddIOTDeviceViewGroup);

        addIOTDeviceAPICall();

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.add_iot_device));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAddIOTDeviceHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mAddIOTDeviceHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTAddDevice.this)));
                    mAddIOTDeviceHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTAddDevice.this), 0, 0);
                    mAddIOTDeviceHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
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
    @OnClick({R.id.header_left_img_lay, R.id.dashboard_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);

        }
    }

    /*Add IOT API calls*/
    private void addIOTDeviceAPICall() {
        APIRequestHandler.getInstance().addIOTDeviceAPICall(this);
    }

    /*IOT Device Config API calls*/
    private void iotDeviceConfigAPICall() {
        APIRequestHandler.getInstance().iotDeviceConfigAPICall(String.valueOf(AppConstants.ADD_IOT_DEVICE_DETAILS.getId()), this);
    }


    /*set Adapter*/
    private void setAdapter(ArrayList<AddIOTDeviceEntity> deviceTypes) {
        AddIOTDeviceEntity addIOTDeviceEntity = new AddIOTDeviceEntity();
        addIOTDeviceEntity.setId(100);
        addIOTDeviceEntity.setName("Moisture Sensor");
        deviceTypes.add(addIOTDeviceEntity);

        AddIOTDeviceEntity nestIOTDeviceEntity = new AddIOTDeviceEntity();
        nestIOTDeviceEntity.setId(101);
        nestIOTDeviceEntity.setName("Nest Protect");
        deviceTypes.add(nestIOTDeviceEntity);

        AddIOTDeviceEntity peanutIOTDeviceEntity = new AddIOTDeviceEntity();
        peanutIOTDeviceEntity.setId(99);
        peanutIOTDeviceEntity.setName("Peanut Plug");
        deviceTypes.add(peanutIOTDeviceEntity);

        AddIOTDeviceEntity hueBulbIOTDeviceEntity = new AddIOTDeviceEntity();
        hueBulbIOTDeviceEntity.setId(98);
        hueBulbIOTDeviceEntity.setName("Hue Bulb");
        deviceTypes.add(hueBulbIOTDeviceEntity);

        AddIOTDeviceEntity zwDoorSensorIOTDeviceEntity = new AddIOTDeviceEntity();
        zwDoorSensorIOTDeviceEntity.setId(71);
        zwDoorSensorIOTDeviceEntity.setName("ZW Door Sensor");
        deviceTypes.add(zwDoorSensorIOTDeviceEntity);

        AddIOTDeviceEntity zwFloodSensorIOTDeviceEntity = new AddIOTDeviceEntity();
        zwFloodSensorIOTDeviceEntity.setId(66);
        zwFloodSensorIOTDeviceEntity.setName("ZW Flood Sensor");
        deviceTypes.add(zwFloodSensorIOTDeviceEntity);

        AddIOTDeviceEntity zwMoistureSensorIOTDeviceEntity = new AddIOTDeviceEntity();
        zwMoistureSensorIOTDeviceEntity.setId(67);
        zwMoistureSensorIOTDeviceEntity.setName("ZW Moisture Sensor");
        deviceTypes.add(zwMoistureSensorIOTDeviceEntity);

        AddIOTDeviceEntity zwSirenIOTDeviceEntity = new AddIOTDeviceEntity();
        zwSirenIOTDeviceEntity.setId(68);
        zwSirenIOTDeviceEntity.setName("ZW Siren");
        deviceTypes.add(zwSirenIOTDeviceEntity);

        AddIOTDeviceEntity zwSmokeDetectorIOTDeviceEntity = new AddIOTDeviceEntity();
        zwSmokeDetectorIOTDeviceEntity.setId(69);
        zwSmokeDetectorIOTDeviceEntity.setName("ZW Smoke Detector");
        deviceTypes.add(zwSmokeDetectorIOTDeviceEntity);

        mAddIOTDeviceRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAddIOTDeviceRecyclerView.setAdapter(new IOTAddDeviceAdapter(deviceTypes, this));
        mAddIOTDeviceRecyclerView.setNestedScrollingEnabled(false);

    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof AddIOTDeviceResponse) {
            AddIOTDeviceResponse addIOTResponse = (AddIOTDeviceResponse) resObj;
            setAdapter(addIOTResponse.getDeviceTypes());
        } else if (resObj instanceof IOTDeviceConfigResponse) {
            AppConstants.IOT_DEVICE_DETAILS = (IOTDeviceConfigResponse) resObj;
            showIOTDeviceStatusAlertPopup(this, this);
        }
    }


    @Override
    public void onRequestFailure(final Object inputModelObj, final Throwable t) {
        super.onRequestFailure(inputModelObj, t);
        if (t instanceof IOException) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    DialogManager.getInstance().showNetworkErrorPopup(IOTAddDevice.this,
                            (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                                    .connect_time_out)), new InterfaceBtnCallback() {
                                @Override
                                public void onPositiveClick() {
                                    if (inputModelObj instanceof AddIOTDeviceResponse)
                                        addIOTDeviceAPICall();
                                    else if (inputModelObj instanceof IOTDeviceConfigResponse)
                                        iotDeviceConfigAPICall();

                                }
                            });
                }
            });

        }
    }


    /*Show Add IOT Alert popup*/
    public void showIOTDeviceStatusAlertPopup(Context context, final InterfaceTwoBtnCallback dialogAlertInterface) {
        alertDismiss(mAddIOTAlertDialog);
        mAddIOTAlertDialog = new Dialog(context);
        mAddIOTAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (mAddIOTAlertDialog.getWindow() != null) {
            mAddIOTAlertDialog.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            mAddIOTAlertDialog.setContentView(R.layout.popup_iot_status_alert);
            mAddIOTAlertDialog.getWindow().setGravity(Gravity.CENTER);
            mAddIOTAlertDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        mAddIOTAlertDialog.setCancelable(false);
        mAddIOTAlertDialog.setCanceledOnTouchOutside(false);

        WindowManager.LayoutParams LayoutParams = new WindowManager.LayoutParams();
        Window window = mAddIOTAlertDialog.getWindow();

        if (window != null) {
            LayoutParams.copyFrom(window.getAttributes());
            LayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            LayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(LayoutParams);
            window.setGravity(Gravity.CENTER);
        }


        /*Init view*/
        mAlertDeviceStatusHeaderTxt = mAddIOTAlertDialog.findViewById(R.id.alert_device_status_header_txt);
        mConfigStatusMsgTxt = mAddIOTAlertDialog.findViewById(R.id.alert_device_status_txt);
        mConfigIndicatorView = mAddIOTAlertDialog.findViewById(R.id.config_indicator_view);
        mConfigSuccessImg = mAddIOTAlertDialog.findViewById(R.id.config_success_img);
        mAlertPositiveBtn = mAddIOTAlertDialog.findViewById(R.id.alert_positive_btn);
        mAlertNegativeBtn = mAddIOTAlertDialog.findViewById(R.id.alert_negative_btn);

        /*Set data*/
        headerTxt(context.getString(R.string.searching));

        mAlertPositiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddIOTAlertDialog.dismiss();
                nextScreen(IOTRouterSettings.class);
            }
        });

        mAlertNegativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebSocket != null) {
                    mWebSocket.cancel();
                }
                mAddIOTAlertDialog.dismiss();
                onBackPressed();
            }
        });

        /*init Router Config*/
        connectAlmondToRouter();
        alertShowing(mAddIOTAlertDialog);

    }

    private void headerTxt(final String deviceStatusTxt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAlertDeviceStatusHeaderTxt != null)
                    mAlertDeviceStatusHeaderTxt.setText(deviceStatusTxt);
            }
        });
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

                        final String deviceNameStr = json.has("Name") ? json.getString("Name") : "";
                        final String locationStr = json.has("Location") ? json.getString("Location") : "";

                        headerTxt(getString(R.string.adding_device));
                        DialogManager.getInstance().showEdtIOTDeviceNameLocPopup(IOTAddDevice.this, deviceNameStr, locationStr, new InterfaceTwoEdtBtnCallback() {
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
                    if (mAlertDeviceStatusHeaderTxt.getText().toString().equalsIgnoreCase(getString(R.string.device_added)) && commandTypeStr.equalsIgnoreCase("DynamicDeviceAdded")) {
                        try {
                            getConnectedDeviceID(json.getJSONObject("Devices"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mConfigIndicatorView.hide();
                        mConfigIndicatorView.setVisibility(View.GONE);
                        mConfigSuccessImg.setVisibility(View.VISIBLE);
                        mConfigStatusMsgTxt.setText(getString(R.string.device_finalize_setup));
                        mAlertNegativeBtn.setVisibility(View.INVISIBLE);
                        mAlertPositiveBtn.setVisibility(View.VISIBLE);

                    }

                    if (commandTypeStr.equalsIgnoreCase("Invalid Command")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogManager.getInstance().showToast(IOTAddDevice.this, "Please, Try Again");
                                if (mWebSocket != null) {
                                    mWebSocket.cancel();
                                }
                                alertDismiss(mAddIOTAlertDialog);

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


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

    @Override
    public void onSocketRequestClosing(String resObj) {
        super.onSocketRequestClosing(resObj);
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }
}