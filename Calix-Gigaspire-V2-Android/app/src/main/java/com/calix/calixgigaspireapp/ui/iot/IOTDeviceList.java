package com.calix.calixgigaspireapp.ui.iot;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.iot.IOTDeviceListAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.DeviceListResponse;
import com.calix.calixgigaspireapp.output.model.IOTRemoveDeviceEntity;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceTwoBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.google.gson.Gson;

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


public class IOTDeviceList extends BaseActivity {

    @BindView(R.id.iot_device_list_parent_lay)
    ViewGroup mIOTDeviceListViewGroup;

    @BindView(R.id.iot_device_list_header_bg_lay)
    RelativeLayout mIOTDeviceListHeaderBgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mHeaderRightImgLay;

    @BindView(R.id.header_right_img)
    ImageView mHeaderRightImg;

    @BindView(R.id.iot_device_list_recycler_view)
    RecyclerView mIOTDeviceListRecyclerView;

    private WebSocket mWebSocket;
    private boolean mIsRouterConnected = false, mIsAllDeviceBool = true;
    private ArrayList<IOTRemoveDeviceEntity> mIOTDeviceArrListRes = new ArrayList<>();
    private boolean mIsIOTIPBool = false, mIsRESTFullAPICallBool = false, mIsRouterConnectedBool = false;
    private NsdManager mNsdManager;
    private int mIOTIPConnectInt = 0;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_iot_device_list);
        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIOTIPConnectInt = 0;
        try {
            if (mNsdManager != null) {
                mNsdManager.stopServiceDiscovery(mDiscoveryListener);
            }
        } catch (Exception e) {
            Log.e(AppConstants.TAG, e.getLocalizedMessage());
        }
        localIOTHubConnectProcess();

    }

    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTDeviceListViewGroup);


        setHeaderView();

        /*Connect To Almond*/
        DialogManager.getInstance().showProgress(this);
        connectAlmondToRouter();
    }

    private void localIOTHubConnectProcess() {


        if (!mIsIOTIPBool && !mIsRESTFullAPICallBool) {
            if (mIOTIPConnectInt == 3) {
                callRESTFullAPI();
            } else {
                if (mNsdManager != null) {
                    mNsdManager.discoverServices(
                            AppConstants.WEB_SOCKET_SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
                }
                mIOTIPConnectInt += 1;
            }
        }
    }

    NsdManager.DiscoveryListener mDiscoveryListener = new NsdManager.DiscoveryListener() {

        // Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    removeHandler();
                    if (!mIsIOTIPBool) {
                        try {
                            mNsdManager.stopServiceDiscovery(mDiscoveryListener);
                            localIOTHubConnectProcess();
                        } catch (Exception e) {
                            if (!mIsRESTFullAPICallBool)
                                callRESTFullAPI();
                        }
                    }
                }
            };

            mHandler = new Handler();
            mHandler.postDelayed(mRunnable, 10000);
            sysOut("Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found! Do something with it.
            // connect to the service and obtain serviceInfo
            mIsIOTIPBool = true;
            mNsdManager.resolveService(service, new NsdManager.ResolveListener() {
                @Override
                public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                    callRESTFullAPI();
                    sysOut("Resolve failed " + errorCode);
                }

                @Override
                public void onServiceResolved(NsdServiceInfo serviceInfo) {
                    sysOut("serviceInfo---" + serviceInfo.toString());
                    AppConstants.WEB_SOCKET_CALIX_IP = serviceInfo.getHost().getHostName();
                    AppConstants.WEB_SOCKET_CALIX_PORT = String.valueOf(serviceInfo.getPort());
                    mIsIOTIPBool = true;
                    connectAlmondToRouter();
                }
            });

        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            callRESTFullAPI();
            sysOut("service lost" + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            sysOut("Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            callRESTFullAPI();
            sysOut("Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            sysOut("Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }
    };

    private void callRESTFullAPI() {
        DialogManager.getInstance().showToast(this, getString(R.string.login_failed));
        iotDeviceListAPI();
    }

    private void setHeaderView() {

        /*set header changes*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.iot_devices));
        mHeaderRightImgLay.setVisibility(View.INVISIBLE);
        mHeaderRightImg.setImageResource(R.drawable.filter);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTDeviceListHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mIOTDeviceListHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTDeviceList.this)));
                    mIOTDeviceListHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTDeviceList.this), 0, 0);
                    mIOTDeviceListHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_bg) : getResources().getDrawable(R.color.blue));
                }

            });
        }

    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.header_right_img_lay, R.id.add_iot_device_lay, R.id.remove_iot_device_lay, R.id.remove_all_iot_device_lay, R.id.dashboard_lay, R.id.settings_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.header_right_img_lay:

                break;
            case R.id.add_iot_device_lay:

//                nextScreen(IOTAddDevice.class);
                if (mIsRouterConnected)
                    nextScreen(IOTAddDevice.class);
                else
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.failed_to_connect_router), this);
			    break;

            case R.id.remove_iot_device_lay:
                if (mIsRouterConnected)
                    setIOTDeviceListAdapter(mIOTDeviceArrListRes, !mIsAllDeviceBool);
                else
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.failed_to_connect_router), this);
                break;

            case R.id.remove_all_iot_device_lay:
                if (mIsRouterConnected) {
                    DialogManager.getInstance().showOptionPopup(this, getString(R.string.remove_all_iot_msg), getString(R.string.yes), getString(R.string.no), new InterfaceTwoBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (mWebSocket != null) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("CommandType", "RemoveAllDevices");
                                    jsonObject.put("MobileInternalIndex", "898");
                                    mIsAllDeviceBool = true;
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

                } else
                    DialogManager.getInstance().showAlertPopup(this, getString(R.string.failed_to_connect_router), this);

                break;
            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);
                break;
            case R.id.settings_lay:

                break;

        }
    }


    /*API calls*/
    private void iotDeviceListAPI() {
        APIRequestHandler.getInstance().iotDeviceListAPICall(AppConstants.ALEXA_ROUTER_ID,this);
    }

    /*From Almond Router - WebSocket*/
    private void setIOTDeviceListAdapter(ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse, boolean isAllDeviceBool) {
        mIsRouterConnected = true;
        mIsAllDeviceBool = isAllDeviceBool;
        mIOTDeviceListRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mIOTDeviceListRecyclerView.setAdapter(new IOTDeviceListAdapter(iotDeviceListResponse, isAllDeviceBool, mWebSocket, this));
        mIOTDeviceListRecyclerView.setNestedScrollingEnabled(false);
    }

    /*From Calix API - REST Full API*/
    private void setDeviceListAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mIsRouterConnected = false;
        mIOTDeviceListRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mIOTDeviceListRecyclerView.setAdapter(new IOTDeviceListAdapter(deviceListResponse, this));
        mIOTDeviceListRecyclerView.setNestedScrollingEnabled(false);
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse dashboardResponse = (DeviceListResponse) resObj;
            setDeviceListAdapter(dashboardResponse.getDevices());
        }
    }

    @Override
    public void onRequestFailure(Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException || t instanceof java.net.UnknownHostException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            iotDeviceListAPI();
                        }
                    });
        }
    }

    @Override
    public void onSocketRequestOpen(WebSocket webSocket, Response response) {
        super.onSocketRequestOpen(webSocket, response);
        mWebSocket = webSocket;
        DialogManager.getInstance().hideProgress();
//        getAllIOTDevice();
        logInIOTRouter();
    }

    @Override
    public void onSocketRequestFailure(Throwable throwable) {


        DialogManager.getInstance().hideProgress();
//        if (throwable instanceof java.io.IOException || throwable instanceof java.net.SocketTimeoutException || throwable instanceof java.net.ConnectException ||throwable instanceof java.net.NoRouteToHostException) {
        if (throwable instanceof IOException) {
            iotDeviceListAPI();
        }
    }

    private void getAllIOTDevice() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CommandType", "DeviceList");
            jsonObject.put("MobileInternalIndex", "898");
            mWebSocket.send(String.valueOf(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        getAllIOTDevice();
                    }

                    if (commandTypeStr.equalsIgnoreCase("DeviceList")) {
                        try {
                            mIOTDeviceArrListRes = getConnectedDeviceDetails(json.getJSONObject("Devices"));
                            setIOTDeviceListAdapter(mIOTDeviceArrListRes, mIsAllDeviceBool);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (commandTypeStr.equalsIgnoreCase("DynamicAllDevicesRemoved")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                DialogManager.getInstance().showToast(IOTDeviceList.this, getString(R.string.device_removed_success));
                            }
                        });
                    }
                    if (commandTypeStr.equalsIgnoreCase("DynamicDeviceUpdated") || commandTypeStr.equalsIgnoreCase("DynamicDeviceAdded") ||
                            commandTypeStr.equalsIgnoreCase("DynamicDeviceRemoved") || commandTypeStr.equalsIgnoreCase("DynamicIndexUpdated") ||
                            commandTypeStr.equalsIgnoreCase("DynamicAllDevicesRemoved")) {
                        getAllIOTDevice();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private ArrayList<IOTRemoveDeviceEntity> getConnectedDeviceDetails(JSONObject jObject) {
        ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse = new ArrayList<>();
        Iterator<String> keys = jObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                iotDeviceListResponse.add(new Gson().fromJson(jObject.getJSONObject(key).getJSONObject("Data").toString(), IOTRemoveDeviceEntity.class));
                iotDeviceListResponse.get(iotDeviceListResponse.size() - 1).setDeviceONOFFState(jObject.getJSONObject(key).getJSONObject("DeviceValues").getJSONObject(iotDeviceListResponse.get(iotDeviceListResponse.size() - 1).getFriendlyDeviceType().equals("AlmondSiren") ? "2" : "1").getString("Value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return iotDeviceListResponse;
    }

    private void removeHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }
}