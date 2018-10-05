package com.calix.calixgigamanage.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.IOTRemoveDeviceListAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.IOTRemoveDeviceEntity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Response;
import okhttp3.WebSocket;


public class IOTRemoveDeviceList extends BaseActivity {

    @BindView(R.id.iot_device_list_parent_lay)
    ViewGroup mIOTDeviceListViewGroup;

    @BindView(R.id.iot_device_list_header_bg_lay)
    RelativeLayout mIOTDeviceListHeaderBgLay;

    @BindView(R.id.iot_device_list_header_msg_lay)
    RelativeLayout mIOTDeviceListHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.iot_device_count_txt)
    TextView mIOTDeviceCountTxt;

    @BindView(R.id.iot_device_list_recycler_view)
    RecyclerView mIOTDeviceListRecyclerView;

    private WebSocket mWebSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_remove_iot_device_list);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTDeviceListViewGroup);


        setHeaderView();

        connectAlmondToRouter();
    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTDeviceListHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.iot));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTDeviceListHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTDeviceListHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTRemoveDeviceList.this)));
                    mIOTDeviceListHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTRemoveDeviceList.this), 0, 0);
                    mIOTDeviceListHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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


    private void setIOTDeviceListAdapter(ArrayList<IOTRemoveDeviceEntity> iotDeviceListResponse) {
        mIOTDeviceCountTxt.setText(String.valueOf(iotDeviceListResponse.size()));
        mIOTDeviceListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mIOTDeviceListRecyclerView.setAdapter(new IOTRemoveDeviceListAdapter(iotDeviceListResponse, this, mWebSocket));
        mIOTDeviceListRecyclerView.setNestedScrollingEnabled(false);

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
                            setIOTDeviceListAdapter(getConnectedDeviceDetails(json.getJSONObject("Devices")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (commandTypeStr.equalsIgnoreCase("DynamicDeviceUpdated")  ||commandTypeStr.equalsIgnoreCase("DynamicDeviceRemoved") || commandTypeStr.equalsIgnoreCase("DynamicDeviceAdded")) {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return iotDeviceListResponse;
    }


    @Override
    public void onBackPressed() {
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
        backScreen();
    }
}