package com.calix.calixgigamanage.ui.device;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.output.model.ChartDetailsEntity;
import com.calix.calixgigamanage.output.model.ChartDetailsResponse;
import com.calix.calixgigamanage.output.model.ChartFilterEntity;
import com.calix.calixgigamanage.output.model.ChartFilterResponse;
import com.calix.calixgigamanage.output.model.CommonResponse;
import com.calix.calixgigamanage.output.model.DeviceEntity;
import com.calix.calixgigamanage.output.model.DeviceRenameResponse;
import com.calix.calixgigamanage.services.APIRequestHandler;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.DateUtil;
import com.calix.calixgigamanage.utils.DialogManager;
import com.calix.calixgigamanage.utils.ImageUtil;
import com.calix.calixgigamanage.utils.InterfaceBtnCallback;
import com.calix.calixgigamanage.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigamanage.utils.NumberUtil;
import com.calix.calixgigamanage.utils.TextUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DeviceDetails extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.graph_par_lay)
    RelativeLayout mGraphLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.graph_header_bg_lay)
    RelativeLayout mGraphHeaderBgLay;

    @BindView(R.id.line_chart)
    LineChart mLineChart;

    @BindView(R.id.filter_spinner)
    Spinner mFilterSpinner;

    @BindView(R.id.device_img)
    ImageView mDeviceImg;

    @BindView(R.id.device_name_txt)
    TextView mDeviceNameTxt;

    @BindView(R.id.device_sub_type_txt)
    TextView mDeviceSubTypeTxt;

    @BindView(R.id.connection_status_img)
    ImageView mConnectionStatusImg;

    @BindView(R.id.connection_status_txt)
    TextView mConnectionStatusTxt;

    @BindView(R.id.connected_device_name_txt)
    TextView mConnectedDeviceNameTxt;

    @BindView(R.id.connected_device_img)
    ImageView mConnectedDeviceImg;

    @BindView(R.id.download_speed_txt)
    TextView mDownloadSpeedTxt;

    @BindView(R.id.download_scale_txt)
    TextView mDownloadScaleTxt;

    @BindView(R.id.upload_speed_txt)
    TextView mUploadSpeedTxt;

    @BindView(R.id.upload_scale_txt)
    TextView mUploadScaleTxt;

    @BindView(R.id.signal_strength_txt)
    TextView mSignalStrengthTxt;

    @BindView(R.id.connect_disconnect_img)
    ImageView mConnectDisconnectImg;

    @BindView(R.id.connect_disconnect_txt)
    TextView mConnectDisconnectTxt;

    @BindView(R.id.connect_disconnect_switch_compat)
    SwitchCompat mConnectDisconnectSwitchCompat;

    @BindView(R.id.ip_address_txt)
    TextView mIpAddressTxt;

    @BindView(R.id.band_txt)
    TextView mBandTxt;

    @BindView(R.id.channel_txt)
    TextView mChannelTxt;

    @BindView(R.id.byte_conversion_scale_txt)
    TextView mByteConversionTxt;

    @BindView(R.id.time_conversion_scale_txt)
    TextView mTimeConversionTxt;

    @BindView(R.id.wifi_detail_lay)
    LinearLayout mWifiDetailLay;

    @BindView(R.id.signal_strength_view)
    View mSignalStrengthView;

    @BindView(R.id.signal_strength_lay)
    RelativeLayout mSignalStrengthLay;


    private ArrayList<String> mFilterTypStrArrList = new ArrayList<>();
    private float mGraphYAxisMaxFloat = 0;
    private String mFilterNameStr = "", mDeviceNameStr = "";
    private boolean mIsDeviceConnectBool = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_device_details);
        initView();
    }


    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mGraphLay);

        setHeaderView();
        setData();

    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mGraphHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size190);
                    mGraphHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(DeviceDetails.this)));
                    mGraphHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(DeviceDetails.this), 0, 0);
                    mGraphHeaderBgLay.setBackground(getResources().getDrawable(R.drawable.header_violet_bg));
                }

            });
        }
    }


    @OnClick({R.id.header_left_img_lay, R.id.arrow_img, R.id.device_edit_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.arrow_img:
                mFilterSpinner.performClick();
                break;
            case R.id.device_edit_img:

                DialogManager.getInstance().showEdtDeviceNamePopup(this, mDeviceNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtStr) {
                        mDeviceNameStr = edtStr;
                        edtDeviceNameAPI(mDeviceNameStr);
                    }
                });
                break;
        }
    }

    private void setData() {
        DeviceEntity deviceDetailsRes = AppConstants.DEVICE_DETAILS_ENTITY;
        boolean isDeviceConnectedBool = deviceDetailsRes.isConnected2network();

        mHeaderTxt.setText(deviceDetailsRes.getName());

        mDeviceImg.setImageResource(ImageUtil.getInstance().deviceImg(deviceDetailsRes.getType()));
        mDeviceNameTxt.setText(deviceDetailsRes.getName());

        String deviceTypeStr = TextUtil.getInstance().deviceSubTypeNameStr(this, deviceDetailsRes.getType(), deviceDetailsRes.getSubType());
        mDeviceSubTypeTxt.setText(deviceTypeStr);
        mDeviceSubTypeTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceDetailsRes.getIfType()));
        mConnectionStatusTxt.setText(getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from));
        mConnectedDeviceNameTxt.setText(deviceDetailsRes.getRouter().getName());

        mSignalStrengthTxt.setText(deviceDetailsRes.getSignalStrength() + " " + getString(R.string.dbm));

        mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(!isDeviceConnectedBool, deviceDetailsRes.getIfType()));
        mConnectDisconnectTxt.setText(String.format(getString(isDeviceConnectedBool ? R.string.disconnect_device : R.string.connect_device), deviceDetailsRes.getRouter().getName()));
        mConnectDisconnectSwitchCompat.setChecked(isDeviceConnectedBool);

        mUploadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(this, deviceDetailsRes.getNetworkUsage().getUpload()));
        mDownloadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(this, deviceDetailsRes.getNetworkUsage().getDownload()));
        mUploadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceDetailsRes.getNetworkUsage().getUpload()));
        mDownloadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceDetailsRes.getNetworkUsage().getDownload()));


        mIpAddressTxt.setText(deviceDetailsRes.getIpAddress());
        mBandTxt.setText(deviceDetailsRes.getBand());
        mChannelTxt.setText(deviceDetailsRes.getChannel());
        mWifiDetailLay.setVisibility(deviceDetailsRes.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);
        mSignalStrengthView.setVisibility(deviceDetailsRes.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);
        mSignalStrengthLay.setVisibility(deviceDetailsRes.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);

        graphFilterAPI();
    }

    /*View onCheckedChanged*/
    @OnCheckedChanged({R.id.connect_disconnect_switch_compat})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.connect_disconnect_switch_compat:
                mIsDeviceConnectBool = isChecked;

                connectAndDisConnectDeviceAPI(mIsDeviceConnectBool);

                mConnectionStatusImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(mIsDeviceConnectBool, AppConstants.DEVICE_DETAILS_ENTITY.getIfType()));
                mConnectionStatusTxt.setText(getString(isChecked ? R.string.connected_to : R.string.disconnected_from));

                mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(!isChecked, AppConstants.DEVICE_DETAILS_ENTITY.getIfType()));
                mConnectDisconnectTxt.setText(String.format(getString(isChecked ? R.string.disconnect_device : R.string.connect_device), AppConstants.DEVICE_DETAILS_ENTITY.getRouter().getName()));

                break;
        }
    }


    private void graphAPI(String filterNameStr) {
        mFilterNameStr = filterNameStr;

//        APIRequestHandler.getInstance().deviceChartDetailsAPICall("bfaa3128-f7dc-4fa7-8fc6-8f6256741312", filterNameStr, this);

        APIRequestHandler.getInstance().deviceChartDetailsAPICall(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), filterNameStr, this);
    }

    private void graphFilterAPI() {
        APIRequestHandler.getInstance().deviceChartFilterAPICall(this);
    }

    private void edtDeviceNameAPI(String deviceNameStr) {
        APIRequestHandler.getInstance().deviceRenameAPICalll(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), deviceNameStr, "1234", DeviceDetails.this);
        mDeviceNameTxt.setText(deviceNameStr);
        mHeaderTxt.setText(deviceNameStr);
    }

    private void connectAndDisConnectDeviceAPI(boolean isChecked) {
        if (isChecked)
            APIRequestHandler.getInstance().deviceConnectAPICall(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), this);
        else
            APIRequestHandler.getInstance().deviceDisconnectAPICall(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), this);

    }

    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof ChartFilterResponse) {
            ChartFilterResponse chartFilterResponse = (ChartFilterResponse) resObj;
            if (chartFilterResponse.getFilters().size() > 0) {
                ArrayList<ChartFilterEntity> encryptionTypeEntity = chartFilterResponse.getFilters();
                mFilterTypStrArrList = new ArrayList<>();

                int spinnerSelectedTypeInt = 0;
                for (int i = 0; i < encryptionTypeEntity.size(); i++) {
                    mFilterTypStrArrList.add(encryptionTypeEntity.get(i).getType());
//                    if (encryptionTypeEntity.get(i).getType().equalsIgnoreCase(getString(R.string.day))) {
//                        spinnerSelectedTypeInt = i;
//                    }

                }
                setSpinnerData(spinnerSelectedTypeInt);

            }
        } else if (resObj instanceof ChartDetailsResponse) {
            ChartDetailsResponse chartDetailsResponse = (ChartDetailsResponse) resObj;
            GraphSection(chartDetailsResponse.getType(), chartDetailsResponse.getData());
        }
    }

    @Override
    public void onRequestFailure(final Object inputModelObj, Throwable t) {
        super.onRequestFailure(inputModelObj, t);
        if (t instanceof IOException) {
            DialogManager.getInstance().showNetworkErrorPopup(this,
                    (t instanceof java.net.ConnectException ? getString(R.string.no_internet) : getString(R.string
                            .connect_time_out)), new InterfaceBtnCallback() {
                        @Override
                        public void onPositiveClick() {
                            if (inputModelObj instanceof ChartFilterResponse) {
                                graphFilterAPI();
                            } else if (inputModelObj instanceof ChartDetailsResponse) {
                                graphAPI(mFilterNameStr);
                            } else if (inputModelObj instanceof DeviceRenameResponse) {
                                edtDeviceNameAPI(mDeviceNameStr);
                            } else if (inputModelObj instanceof CommonResponse) {
                                connectAndDisConnectDeviceAPI(mIsDeviceConnectBool);
                            }
                        }
                    });
        }
    }


    private void setSpinnerData(int spinnerPosInt) {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(DeviceDetails.this, R.layout.adap_spinner_equ_selected, mFilterTypStrArrList);
        adapter.setDropDownViewResource(R.layout.adap_spinner_equ_list);
        mFilterSpinner.setAdapter(adapter);


        //Model Spinner item click
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                graphAPI(mFilterTypStrArrList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mFilterTypStrArrList.size() > 0) {
            mFilterSpinner.setSelection(spinnerPosInt, true);
        }


    }

    private void GraphSection(String filterTypeStr, ArrayList<ChartDetailsEntity> chartDetailsRes) {

        mLineChart.setDrawGridBackground(false);

        setGraphDetails(filterTypeStr, chartDetailsRes);

        // get the legend (only possible after setting data)
        Legend l = mLineChart.getLegend();

        mLineChart.getLegend().setEnabled(false);

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

        // no description text
        mLineChart.setDescription("");
        mLineChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mLineChart.setTouchEnabled(true);

        // enable scaling and dragging
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setScaleXEnabled(true);
        mLineChart.setScaleYEnabled(true);
        mLineChart.setExtraLeftOffset(10f);

        XAxis bottomAxis = mLineChart.getXAxis();
        bottomAxis.setTextColor(Color.WHITE);
        bottomAxis.setAxisMinValue(0);
        bottomAxis.setLabelsToSkip(0);
       

        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setDrawZeroLine(false);
//        leftAxis.setAxisMinValue(0);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawLabels(chartDetailsRes.size() > 0);

//        if (chartDetailsRes.size() <= 0) {
//            mLineChart.getAxisLeft().setDrawLabels(false);
//        } else {
//            mLineChart.getAxisLeft().setDrawLabels(true);
//        }

        boolean isTabletBool = (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//
//        sysOut("isTabletBool---"+isTabletBool);

        bottomAxis.setTextSize(getResources().getDimensionPixelSize(isTabletBool ? R.dimen.size5 : R.dimen.size2));
        leftAxis.setTextSize(getResources().getDimensionPixelSize(isTabletBool ? R.dimen.size5 : R.dimen.size2));
//        bottomAxis.setTextSize(isTabletBool ? 5 : 2);
//        leftAxis.setTextSize(isTabletBool ? 5 : 2);
        mLineChart.setExtraRightOffset(isTabletBool ? 35f : 25f);
        mLineChart.animateX(700, Easing.EasingOption.EaseInOutQuart);



        //  dont forget to refresh the drawing
        mLineChart.invalidate();

    }


    private void setGraphDetails(String filterTypeStr, ArrayList<ChartDetailsEntity> chartDetailsRes) {

        ArrayList<String> xAxisDateArrList = new ArrayList<>();
        ArrayList<Entry> downloadEntryArrList = new ArrayList<>();
        ArrayList<Entry> uploadEntryArrList = new ArrayList<>();
        LineDataSet downloadLineDataSet, uploadLineDataSet;
        int totalSizeCalInt = chartDetailsRes.size() / 2;
        int chatDefaultByteCalInt = 0;
        boolean isMByteBool = false;


        for (int chatCalIntPos = 0; chatCalIntPos < chartDetailsRes.size(); chatCalIntPos++) {
            if (chartDetailsRes.get(chatCalIntPos).getDownload() >= 1024) {
                chatDefaultByteCalInt++;
            }
            if (chartDetailsRes.get(chatCalIntPos).getUpload() >= 1024) {
                chatDefaultByteCalInt++;
            }
            if (totalSizeCalInt <= chatDefaultByteCalInt) {
                isMByteBool = true;
                break;
            }
        }

        for (int chatDataIntPos = 0; chatDataIntPos < chartDetailsRes.size(); chatDataIntPos++) {
            float downloadFloat = (float) (isMByteBool ? chartDetailsRes.get(chatDataIntPos).getDownload() / 1024 : chartDetailsRes.get(chatDataIntPos).getDownload());
            float uploadFloat = (float)  (isMByteBool ? chartDetailsRes.get(chatDataIntPos).getUpload() / 1024 : chartDetailsRes.get(chatDataIntPos).getUpload());

            if (mGraphYAxisMaxFloat == 0) {
                mGraphYAxisMaxFloat = downloadFloat;
            } else if (downloadFloat > mGraphYAxisMaxFloat) {
                mGraphYAxisMaxFloat = downloadFloat;
            }
            if (uploadFloat > mGraphYAxisMaxFloat) {
                mGraphYAxisMaxFloat = uploadFloat;
            }

            SimpleDateFormat targetDateFormat = AppConstants.GRAPH_HOUR_DATE_FORMAT;

//            if(filterTypeStr.equalsIgnoreCase(getString(R.string.hour))||filterTypeStr.equalsIgnoreCase(getString(R.string.min))){
//                targetDateFormat=AppConstants.GRAPH_HOUR_DATE_FORMAT;
//            }
            if (filterTypeStr.equalsIgnoreCase(getString(R.string.day)) || filterTypeStr.equalsIgnoreCase(getString(R.string.week))) {
                targetDateFormat = AppConstants.GRAPH_DAY_DATE_FORMAT;
            } else if (filterTypeStr.equalsIgnoreCase(getString(R.string.month))) {
                targetDateFormat = AppConstants.GRAPH_MONTH_DATE_FORMAT;
            } else if (filterTypeStr.equalsIgnoreCase(getString(R.string.year))) {
                targetDateFormat = AppConstants.GRAPH_YEAR_DATE_FORMAT;
            }



            xAxisDateArrList.add(DateUtil.getCustomDateAndTimeFormat(chartDetailsRes.get(chatDataIntPos).getTime(),targetDateFormat));


            downloadEntryArrList.add(new Entry(downloadFloat, chatDataIntPos));
            uploadEntryArrList.add(new Entry(uploadFloat, chatDataIntPos));
        }

        mByteConversionTxt.setText(getString(isMByteBool?R.string.mbytes:R.string.kbytes));
        mTimeConversionTxt.setText(TextUtil.getInstance().capitalizeStr(filterTypeStr));

        downloadLineDataSet = new LineDataSet(downloadEntryArrList, getString(R.string.download));

        downloadLineDataSet.setFillAlpha(110);
        downloadLineDataSet.setColor(getResources().getColor(R.color.graph_downloaded_color));
        downloadLineDataSet.setCircleColor(Color.WHITE);
//        downloadLineDataSet.setValueTextColor(Color.WHITE);
        downloadLineDataSet.setLineWidth(1f);
        downloadLineDataSet.setCircleRadius(2f);
        downloadLineDataSet.setDrawCircleHole(false);
        downloadLineDataSet.setValueTextSize(0f);
        downloadLineDataSet.setDrawFilled(false);
//        downloadLineDataSet.setDrawCubic(true);


        uploadLineDataSet = new LineDataSet(uploadEntryArrList, getString(R.string.upload));
        uploadLineDataSet.setColor(getResources().getColor(R.color.graph_uploaded_color));
        uploadLineDataSet.setCircleColor(Color.WHITE);
        uploadLineDataSet.setValueTextColor(Color.WHITE);
        uploadLineDataSet.setLineWidth(1f);
        uploadLineDataSet.setCircleRadius(3f);
        uploadLineDataSet.setDrawCircleHole(false);
        uploadLineDataSet.setValueTextSize(0f);
        uploadLineDataSet.setDrawFilled(false);
        //uploadLineDataSet.setDrawCubic(true);


        ArrayList<ILineDataSet> LineDataSet = new ArrayList<>();
        LineDataSet.add(downloadLineDataSet);
        LineDataSet.add(uploadLineDataSet);


        // create a data object with the datasets
        LineData data = new LineData(xAxisDateArrList, LineDataSet);
        // set data
        mLineChart.setData(data);
    }


    @Override
    public void onBackPressed() {
        backScreen();
    }




}




