package com.calix.calixgigaspireapp.ui.devices;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.devices.DeviceSpinnerAdapter;
import com.calix.calixgigaspireapp.adapter.devices.DeviceTypeAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.DeviceFilterEntity;
import com.calix.calixgigaspireapp.output.model.DeviceFilterListResponse;
import com.calix.calixgigaspireapp.output.model.DeviceListResponse;
import com.calix.calixgigaspireapp.output.model.FilterDeviceListResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DevicesList extends BaseActivity {

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.devices_header_bg_lay)
    RelativeLayout mDevicesHeaderBgLay;

    @BindView(R.id.devices_list_recycler_view)
    RecyclerView mDevicesListRecyclerView;

    @BindView(R.id.header_right_img)
    ImageView mHeaderRightImg;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mHeaderRightImgLay;

    @BindView(R.id.device_spinner_card_view)
    CardView mDeviceSpinnerCardView;

    @BindView(R.id.device_spinner_recycler_view)
    RecyclerView mDeviceSpinnerRecyclerView;


    /* Footer Variables */

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_one_txt)
    TextView mFooterOneTxt;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_second_txt)
    TextView mFooterSecondTxt;

    @BindView(R.id.footer_notification_count_lay)
    RelativeLayout mFooterNotificationCountLay;

    @BindView(R.id.footer_notification_count_temp_txt)
    TextView mFooterNotificationCountTempTxt;

    @BindView(R.id.footer_notification_count_txt)
    TextView mFooterNotificationCountTxt;

    @BindView(R.id.footer_third_lay)
    LinearLayout mFooterThirdLay;

    @BindView(R.id.footer_third_view)
    View mFooterThirdView;


    @BindView(R.id.device_lay)
    RelativeLayout mDeviceLayout;

    @BindView(R.id.transparent_img)
    ImageView mTransparentImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_devices_list);
        initView();

    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        setHeaderView();
        setFooterVIew();

        deviceListAPICall();

    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.devices));
        mHeaderRightImgLay.setVisibility(View.VISIBLE);
        mHeaderRightImg.setImageResource(R.drawable.ic_filter);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDevicesHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);

                    mDevicesHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(DevicesList.this)));
                    mDevicesHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(DevicesList.this), 0, 0);

                }

            });
        }
    }

    /*Set Footer View */
    private void setFooterVIew() {
        if (AppConstants.ALERT_COUNT > 0) {
            mFooterNotificationCountLay.setVisibility(View.VISIBLE);
            mFooterNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mFooterNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mFooterNotificationCountLay.setVisibility(View.GONE);
        }

        mFooterFirstImg.setImageResource(R.drawable.ic_dashboard);
        mFooterOneTxt.setText(getString(R.string.dashboard));

        mFooterSecondImg.setImageResource(R.drawable.ic_notification);
        mFooterSecondTxt.setText(getString(R.string.alert));
        mFooterThirdLay.setVisibility(View.GONE);
        mFooterThirdView.setVisibility(View.GONE);
    }


    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.device_lay, R.id.header_right_img_lay,
            R.id.footer_first_lay, R.id.footer_second_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.device_lay:
                mDeviceSpinnerCardView.setVisibility(View.GONE);
                mTransparentImg.setVisibility(View.GONE);
                break;
            case R.id.header_right_img_lay:
                mTransparentImg.setVisibility(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mDeviceSpinnerCardView.setVisibility(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.footer_first_lay:
                previousScreen(Dashboard.class);
                break;
            case R.id.footer_second_lay:
                nextScreen(Alert.class);
                break;

        }
    }

    /*Device List API calls*/
    private void deviceListAPICall() {
        APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    /*Set device list adapter*/
    private void setDeviceListAdapter(ArrayList<DeviceListResponse> deviceListRes) {
        mDevicesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDevicesListRecyclerView.setNestedScrollingEnabled(false);
        mDevicesListRecyclerView.setAdapter(new DeviceTypeAdapter(deviceListRes, this));
    }

    /*API request success and failure*/
    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof DeviceListResponse) {
            DeviceListResponse deviceListResponse = (DeviceListResponse) resObj;
            arrangeAdapterList(deviceListResponse.getDevices());
            deviceFilterListAPI();
        } else if (resObj instanceof FilterDeviceListResponse) {
            FilterDeviceListResponse deviceFilterListRes = (FilterDeviceListResponse) resObj;
            arrangeAdapterList(deviceFilterListRes.getDevices());
        } else if (resObj instanceof DeviceFilterListResponse) {
            DeviceFilterListResponse deviceFilterListResponse = (DeviceFilterListResponse) resObj;
            setDeviceFilterListAdapter(deviceFilterListResponse.getFilters());
        }
    }

    private void deviceFilterListAPI() {
        APIRequestHandler.getInstance().deviceFilterListAPICall(this);
    }


    /*Set filter adapter*/
    private void setDeviceFilterListAdapter(ArrayList<DeviceFilterEntity> deviceFilterListResponse) {
        InterfaceEdtBtnCallback interfaceEdtBtnCallback;
        interfaceEdtBtnCallback = new InterfaceEdtBtnCallback() {
            @Override
            public void onPositiveClick(String editStr) {
                mTransparentImg.setVisibility(View.GONE);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        /*Manually we add All value to adapter first pos*/
        ArrayList<DeviceFilterEntity> deviceFilterRes = new ArrayList<>();
        if (deviceFilterListResponse.size() > 1) {
            DeviceFilterEntity deviceFilterEntity = new DeviceFilterEntity();
            deviceFilterEntity.setName(getString(R.string.all));
            deviceFilterRes.add(deviceFilterEntity);
        }

        deviceFilterRes.addAll(deviceFilterListResponse);

        mDeviceSpinnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDeviceSpinnerRecyclerView.setAdapter(new DeviceSpinnerAdapter(deviceFilterRes, interfaceEdtBtnCallback, mDeviceSpinnerCardView, this));

    }
//    /*Arrange adapter based on the device list */
//    private void arrangeAdapterList(ArrayList<DeviceEntity> deviceListRes) {
//
//
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> arrangeDevicesArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> phoneListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> computerListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> consoleListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> storageListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> printerListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> televisionListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> iotListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> cameraListArrLisRes = new LinkedHashMap<>();
//        LinkedHashMap<Integer, ArrayList<DeviceEntity>> unknownListArrLisRes = new LinkedHashMap<>();
//
//
//        for (int posInt = 0; posInt < deviceListRes.size(); posInt++) {
//            DeviceEntity deviceRes = deviceListRes.get(posInt);
//
//            /*find the IOT device List*/
//
//            switch (deviceRes.getType()) {
//                case 1:
//                    ArrayList<DeviceEntity> phoneLisRes = phoneListArrLisRes.containsKey(deviceRes.getType()) ? phoneListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    phoneLisRes.add(deviceRes);
//                    phoneListArrLisRes.put(1, phoneLisRes);
//                    break;
//                case 2:
//                    ArrayList<DeviceEntity> computerListRes = computerListArrLisRes.containsKey(deviceRes.getType()) ? computerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    computerListRes.add(deviceRes);
//                    computerListArrLisRes.put(2, computerListRes);
//                    break;
//                case 3:
//                    ArrayList<DeviceEntity> consoleListRes = consoleListArrLisRes.containsKey(deviceRes.getType()) ? consoleListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    consoleListRes.add(deviceRes);
//                    consoleListArrLisRes.put(3, consoleListRes);
//                    break;
//                case 4:
//                    ArrayList<DeviceEntity> storageListRes = storageListArrLisRes.containsKey(deviceRes.getType()) ? storageListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    storageListRes.add(deviceRes);
//                    storageListArrLisRes.put(4, storageListRes);
//                    break;
//                case 5:
//                    ArrayList<DeviceEntity> printerListRes =printerListArrLisRes.containsKey(deviceRes.getType()) ? printerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    printerListRes.add(deviceRes);
//                    printerListArrLisRes.put(5, printerListRes);
//                    break;
//                case 6:
//                    ArrayList<DeviceEntity> televisionListRes =televisionListArrLisRes.containsKey(deviceRes.getType()) ? televisionListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    televisionListRes.add(deviceRes);
//                    televisionListArrLisRes.put(6, televisionListRes);
//                    break;
//                case 7:
//                    ArrayList<DeviceEntity> iotListRes= iotListArrLisRes.containsKey(deviceRes.getType()) ? iotListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    iotListRes.add(deviceRes);
//                    iotListArrLisRes.put(7, iotListRes);
//                    break;
//                case 8:
//                    ArrayList<DeviceEntity> cameraListRes= cameraListArrLisRes.containsKey(deviceRes.getType()) ? cameraListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    cameraListRes.add(deviceRes);
//                    cameraListArrLisRes.put(8, cameraListRes);
//                    break;
//                default:
//                    ArrayList<DeviceEntity> unknownListRes= unknownListArrLisRes.containsKey(deviceRes.getType()) ? unknownListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
//                    unknownListRes.add(deviceRes);
//                    unknownListArrLisRes.put(0, unknownListRes);
//                    break;
//
//            }
//
//
//
//            arrangeDevicesArrLisRes.putAll(unknownListArrLisRes);
//            if(phoneListArrLisRes.get(1)!=null)
//                arrangeDevicesArrLisRes.put(1, phoneListArrLisRes.get(1));
//
//            if(computerListArrLisRes.get(2)!=null)
//                arrangeDevicesArrLisRes.put(2, computerListArrLisRes.get(2));
//
//            if(consoleListArrLisRes.get(3)!=null)
//                arrangeDevicesArrLisRes.put(3, consoleListArrLisRes.get(3));
//
//            if(storageListArrLisRes.get(4)!=null)
//                arrangeDevicesArrLisRes.put(4, storageListArrLisRes.get(4));
//
//            if(storageListArrLisRes.get(5)!=null)
//                arrangeDevicesArrLisRes.put(5, storageListArrLisRes.get(5));
//
//            if(storageListArrLisRes.get(6)!=null)
//                arrangeDevicesArrLisRes.put(6, storageListArrLisRes.get(6));
//
//            if(iotListArrLisRes.get(7)!=null)
//                arrangeDevicesArrLisRes.put(7, iotListArrLisRes.get(7));
//
//            if(cameraListArrLisRes.get(8)!=null)
//                arrangeDevicesArrLisRes.put(8, cameraListArrLisRes.get(8));
//
//            if(unknownListArrLisRes.get(0)!=null)
//                arrangeDevicesArrLisRes.put(0, unknownListArrLisRes.get(0));
//        }

    /*Set Adapter*/
//        setDeviceListAdapter(arrangeDevicesArrLisRes);
//    }


    /*Arrange adapter based on the device list */
    private void arrangeAdapterList(ArrayList<DeviceEntity> deviceListRes) {


        ArrayList<DeviceListResponse> arrangeDevicesArrLisRes = new ArrayList<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> phoneListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> computerListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> consoleListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> storageListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> printerListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> televisionListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> iotListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> cameraListArrLisRes = new LinkedHashMap<>();
        LinkedHashMap<Integer, ArrayList<DeviceEntity>> unknownListArrLisRes = new LinkedHashMap<>();


        for (int posInt = 0; posInt < deviceListRes.size(); posInt++) {
            DeviceEntity deviceRes = deviceListRes.get(posInt);

            /*find the IOT device List*/

            switch (deviceRes.getType()) {
                case 1:
                    ArrayList<DeviceEntity> phoneLisRes = phoneListArrLisRes.containsKey(deviceRes.getType()) ? phoneListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    phoneLisRes.add(deviceRes);
                    phoneListArrLisRes.put(1, phoneLisRes);
                    break;
                case 2:
                    ArrayList<DeviceEntity> computerListRes = computerListArrLisRes.containsKey(deviceRes.getType()) ? computerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    computerListRes.add(deviceRes);
                    computerListArrLisRes.put(2, computerListRes);
                    break;
                case 3:
                    ArrayList<DeviceEntity> consoleListRes = consoleListArrLisRes.containsKey(deviceRes.getType()) ? consoleListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    consoleListRes.add(deviceRes);
                    consoleListArrLisRes.put(3, consoleListRes);
                    break;
                case 4:
                    ArrayList<DeviceEntity> storageListRes = storageListArrLisRes.containsKey(deviceRes.getType()) ? storageListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    storageListRes.add(deviceRes);
                    storageListArrLisRes.put(4, storageListRes);
                    break;
                case 5:
                    ArrayList<DeviceEntity> printerListRes = printerListArrLisRes.containsKey(deviceRes.getType()) ? printerListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    printerListRes.add(deviceRes);
                    printerListArrLisRes.put(5, printerListRes);
                    break;
                case 6:
                    ArrayList<DeviceEntity> televisionListRes = televisionListArrLisRes.containsKey(deviceRes.getType()) ? televisionListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    televisionListRes.add(deviceRes);
                    televisionListArrLisRes.put(6, televisionListRes);
                    break;
                case 7:
                    ArrayList<DeviceEntity> iotListRes = iotListArrLisRes.containsKey(deviceRes.getType()) ? iotListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    iotListRes.add(deviceRes);
                    iotListArrLisRes.put(7, iotListRes);
                    break;
                case 8:
                    ArrayList<DeviceEntity> cameraListRes = cameraListArrLisRes.containsKey(deviceRes.getType()) ? cameraListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    cameraListRes.add(deviceRes);
                    cameraListArrLisRes.put(8, cameraListRes);
                    break;
                default:
                    ArrayList<DeviceEntity> unknownListRes = unknownListArrLisRes.containsKey(deviceRes.getType()) ? unknownListArrLisRes.get(deviceRes.getType()) : new ArrayList<DeviceEntity>();
                    unknownListRes.add(deviceRes);
                    unknownListArrLisRes.put(0, unknownListRes);
                    break;

            }


        }

        if (phoneListArrLisRes.get(1) != null) {
            DeviceListResponse phoneArrList = new DeviceListResponse();
            phoneArrList.setType(1);
            phoneArrList.setDevices(phoneListArrLisRes.get(1));
            arrangeDevicesArrLisRes.add(phoneArrList);
        }
        if (computerListArrLisRes.get(2) != null) {
            DeviceListResponse computerArrList = new DeviceListResponse();
            computerArrList.setType(2);
            computerArrList.setDevices(computerListArrLisRes.get(2));
            arrangeDevicesArrLisRes.add(computerArrList);
        }
        if (consoleListArrLisRes.get(3) != null) {
            DeviceListResponse consoleArrList = new DeviceListResponse();
            consoleArrList.setType(3);
            consoleArrList.setDevices(consoleListArrLisRes.get(3));
            arrangeDevicesArrLisRes.add(consoleArrList);
        }
        if (storageListArrLisRes.get(4) != null) {
            DeviceListResponse storageArrList = new DeviceListResponse();
            storageArrList.setType(4);
            storageArrList.setDevices(storageListArrLisRes.get(4));
            arrangeDevicesArrLisRes.add(storageArrList);
        }
        if (printerListArrLisRes.get(5) != null) {
            DeviceListResponse printerArrList = new DeviceListResponse();
            printerArrList.setType(5);
            printerArrList.setDevices(printerListArrLisRes.get(5));
            arrangeDevicesArrLisRes.add(printerArrList);
        }
        if (televisionListArrLisRes.get(6) != null) {
            DeviceListResponse televisionArrList = new DeviceListResponse();
            televisionArrList.setType(6);
            televisionArrList.setDevices(televisionListArrLisRes.get(6));
            arrangeDevicesArrLisRes.add(televisionArrList);
        }
        if (iotListArrLisRes.get(7) != null) {
            DeviceListResponse iotArrList = new DeviceListResponse();
            iotArrList.setType(7);
            iotArrList.setDevices(iotListArrLisRes.get(7));
            arrangeDevicesArrLisRes.add(iotArrList);
        }
        if (cameraListArrLisRes.get(8) != null) {
            DeviceListResponse cameraArrList = new DeviceListResponse();
            cameraArrList.setType(8);
            cameraArrList.setDevices(cameraListArrLisRes.get(8));
            arrangeDevicesArrLisRes.add(cameraArrList);
        }
        if (unknownListArrLisRes.get(0) != null) {
            DeviceListResponse unknownArrList = new DeviceListResponse();
            unknownArrList.setType(0);
            unknownArrList.setDevices(unknownListArrLisRes.get(0));
            arrangeDevicesArrLisRes.add(unknownArrList);
        }
        /*Set Adapter*/
        setDeviceListAdapter(arrangeDevicesArrLisRes);
    }

    @Override
    public void onRequestFailure(final Object resObj, Throwable t) {
        super.onRequestFailure(resObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (resObj instanceof DeviceListResponse)
                                deviceListAPICall();
                            else deviceFilterListAPI();
                        }
                    });
        }
    }


    /*Default back button action*/
    @Override
    public void onBackPressed() {
        Log.d("backscreen", "- pressed");
        backScreen();
    }
}
