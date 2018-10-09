package com.calix.calixgigaspireapp.ui.devices;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.devices.DeviceDetailsSpinnerAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.ChartDetailsEntity;
import com.calix.calixgigaspireapp.output.model.ChartDetailsResponse;
import com.calix.calixgigaspireapp.output.model.ChartFilterEntity;
import com.calix.calixgigaspireapp.output.model.ChartFilterResponse;
import com.calix.calixgigaspireapp.output.model.CommonResponse;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.output.model.DeviceRenameResponse;
import com.calix.calixgigaspireapp.services.APIRequestHandler;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DateUtil;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.ImageUtil;
import com.calix.calixgigaspireapp.utils.InterfaceBtnCallback;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.calix.calixgigaspireapp.utils.TextUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

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

    @BindView(R.id.device_img)
    ImageView mDeviceImg;

    @BindView(R.id.device_name_txt)
    TextView mDeviceNameTxt;

    @BindView(R.id.device_name_sub_txt)
    TextView mDeviceNameSubTxt;

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

    @BindView(R.id.connect_disconnect_switch_btn)
    ToggleButton mToggleWifi;

    @BindView(R.id.ip_address_txt)
    TextView mIpAddressTxt;

    @BindView(R.id.band_txt)
    TextView mBandTxt;

    @BindView(R.id.channel_txt)
    TextView mChannelTxt;

    /* Footer Variables */

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_one_txt)
    TextView mFooterOneTxt;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_second_txt)
    TextView mFooterSecondTxt;

    @BindView(R.id.byte_conversion_scale_txt)
    TextView mByteConversionTxt;

    @BindView(R.id.time_conversion_scale_txt)
    TextView mTimeConversionTxt;

    @BindView(R.id.footer_notification_count_lay)
    RelativeLayout mFooterNotificationCountLay;

    @BindView(R.id.footer_notification_count_temp_txt)
    TextView mFooterNotificationCountTempTxt;

    @BindView(R.id.footer_notification_count_txt)
    TextView mFooterNotificationCountTxt;

    @BindView(R.id.footer_third_img)
    ImageView mFooterThirdImg;

    @BindView(R.id.footer_third_txt)
    TextView mFooterThirdTxt;

    @BindView(R.id.transparent_img)
    ImageView mTransaprentView;

    @BindView(R.id.device_spinner_card_view)
    CardView mDeviceSpinnerCardView;

    @BindView(R.id.device_spinner_recycler_view)
    RecyclerView mDeviceSpinnerRecyclerView;

    @BindView(R.id.wifi_detail_lay)
    LinearLayout mWifiDetailLay;

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
        setFooterVIew();

        setData();
    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mGraphHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size170);
                    mGraphHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(DeviceDetails.this)));
                    mGraphHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(DeviceDetails.this), 0, 0);
                    mGraphHeaderBgLay.setBackground(getResources().getDrawable(R.drawable.header_bg));
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


        mFooterThirdImg.setImageResource(R.drawable.ic_filter);
        mFooterThirdTxt.setText(getString(R.string.footer_filter));
    }


    @OnClick({R.id.header_left_img_lay, R.id.device_edit_img, R.id.footer_first_lay, R.id.footer_second_lay, R.id.footer_third_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;

            case R.id.device_edit_img:

                DialogManager.getInstance().showEdtDeviceNamePopup(this,
                        getString(R.string.device_edit_pheader),
                        getString(R.string.device_edit_sheader),
                        getString(R.string.device_edit_hint),
                        mDeviceNameTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
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

            case R.id.footer_first_lay:
                previousScreen(Dashboard.class);
                break;
            case R.id.footer_second_lay:
                nextScreen(Alert.class);
                break;
            case R.id.footer_third_lay:
                mTransaprentView.setVisibility(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                if (mDeviceSpinnerCardView.getVisibility() == View.GONE) {
                    setDeviceFilterListAdapter();
                }
                mDeviceSpinnerCardView.setVisibility(mDeviceSpinnerCardView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

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
        mDeviceNameSubTxt.setText(deviceTypeStr);
        mDeviceNameSubTxt.setVisibility(deviceTypeStr.isEmpty() ? View.GONE : View.VISIBLE);

        mSignalStrengthTxt.setText(deviceDetailsRes.getSignalStrength() + " " + getString(R.string.dbm));

        mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isDeviceConnectedBool, deviceDetailsRes.getIfType()));
        mConnectDisconnectTxt.setText(String.format(getString(isDeviceConnectedBool ? R.string.connected_to : R.string.disconnected_from), AppConstants.DEVICE_DETAILS_ENTITY.getRouter().getName()));
        mToggleWifi.setChecked(isDeviceConnectedBool);
        setToggleView(isDeviceConnectedBool);

        mUploadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(this, deviceDetailsRes.getNetworkUsage().getUpload()));
        mDownloadScaleTxt.setText(TextUtil.getInstance().deviceScaleNameStr(this, deviceDetailsRes.getNetworkUsage().getDownload()));
        mUploadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceDetailsRes.getNetworkUsage().getUpload()));
        mDownloadSpeedTxt.setText(NumberUtil.getInstance().usageScaleValStr(deviceDetailsRes.getNetworkUsage().getDownload()));


        mIpAddressTxt.setText(deviceDetailsRes.getIpAddress());
        mBandTxt.setText(String.format(getString(R.string.band_unit), deviceDetailsRes.getBand()));
        mChannelTxt.setText(String.valueOf(deviceDetailsRes.getChannel()));


        mWifiDetailLay.setVisibility(deviceDetailsRes.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);
        mSignalStrengthLay.setVisibility(deviceDetailsRes.getIfType().contains(AppConstants.ETHER_NET) ? View.GONE : View.VISIBLE);

        graphFilterAPI();
    }

    /*View onCheckedChanged*/
    @OnCheckedChanged({R.id.connect_disconnect_switch_btn})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.connect_disconnect_switch_btn:
                mIsDeviceConnectBool = isChecked;
                setToggleView(mIsDeviceConnectBool);

                connectAndDisConnectDeviceAPI(mIsDeviceConnectBool);

                mConnectDisconnectImg.setImageResource(ImageUtil.getInstance().connectedStatusViaRouterImg(isChecked, AppConstants.DEVICE_DETAILS_ENTITY.getIfType()));
                mConnectDisconnectTxt.setText(String.format(getString(isChecked ? R.string.connected_to : R.string.disconnected_from), AppConstants.DEVICE_DETAILS_ENTITY.getRouter().getName()));

                break;
        }
    }

    private void setToggleView(boolean isChecked) {
        if (isChecked) {
            // The toggle is enabled
            mToggleWifi.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);// Set left padding
            int padding = getResources().getDimensionPixelSize(R.dimen.size2);
            int paddingBottom = getResources().getDimensionPixelSize(R.dimen.size3);
            int paddingLeft = getResources().getDimensionPixelSize(R.dimen.size5);
            mToggleWifi.setPadding(paddingLeft, padding, padding, paddingBottom);
        } else {
            // The toggle is disabled
            mToggleWifi.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);// Set left padding
            int padding = getResources().getDimensionPixelSize(R.dimen.size2);
            int paddingTop = getResources().getDimensionPixelSize(R.dimen.size3);
            int paddingRight = getResources().getDimensionPixelSize(R.dimen.size3);
            mToggleWifi.setPadding(padding, paddingTop, paddingRight, padding);
        }
    }

    private void connectAndDisConnectDeviceAPI(boolean isChecked) {
        if (isChecked)
            APIRequestHandler.getInstance().deviceConnectAPICall(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), this);
        else
            APIRequestHandler.getInstance().deviceDisconnectAPICall(AppConstants.DEVICE_DETAILS_ENTITY.getDeviceId(), this);

    }


    private void graphAPI(String filterNameStr) {
        mFilterNameStr = filterNameStr;
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


    @Override
    public void onRequestSuccess(Object resObj) {
        super.onRequestSuccess(resObj);
        if (resObj instanceof ChartFilterResponse) {
            ChartFilterResponse chartFilterResponse = (ChartFilterResponse) resObj;
            if (chartFilterResponse.getFilters().size() > 0) {
                ArrayList<ChartFilterEntity> encryptionTypeEntity = chartFilterResponse.getFilters();
                mFilterTypStrArrList = new ArrayList<>();

                for (int i = 0; i < encryptionTypeEntity.size(); i++) {
                    mFilterTypStrArrList.add(encryptionTypeEntity.get(i).getType());

                }
                graphAPI("min");

                setDeviceFilterListAdapter();

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

    /*Set filter adapter*/
    private void setDeviceFilterListAdapter() {
        InterfaceEdtBtnCallback interfaceEdtBtnCallback;
        interfaceEdtBtnCallback = new InterfaceEdtBtnCallback() {
            @Override
            public void onPositiveClick(String editStr) {
                graphAPI(editStr);
                mTransaprentView.setVisibility(View.GONE);
            }

            @Override
            public void onNegativeClick() {

            }
        };

        mDeviceSpinnerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDeviceSpinnerRecyclerView.setAdapter(new DeviceDetailsSpinnerAdapter(mFilterNameStr, mFilterTypStrArrList, interfaceEdtBtnCallback, mDeviceSpinnerCardView, this));
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
//        bottomAxis.setTextSize(getResources().getDimensionPixelSize(R.dimen.size2));
        bottomAxis.setAxisMinValue(0);
        bottomAxis.setLabelsToSkip(0);


        YAxis leftAxis = mLineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.setTextColor(Color.WHITE);
// leftAxis.setTextSize(getResources().getDimensionPixelSize(R.dimen.size2));
//        leftAxis.setLabelCount(chartDetailsRes.size(),true);
        leftAxis.setDrawZeroLine(false);
        //  leftAxis.setAxisMinValue(0);


        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mLineChart.getAxisRight().setEnabled(false);

        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        mLineChart.getAxisLeft().setDrawGridLines(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisLeft().setDrawLabels(chartDetailsRes.size() > 0);


//        TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
//
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

        // do something


//        mLineChart.getAxisLeft().setValueFormatter(new YAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, YAxis yAxis) {
//                return NumberUtil.getInstance().speedTwoDigitsValStr(String.valueOf(value));
//            }
//        });

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
            float uploadFloat = (float) (isMByteBool ? chartDetailsRes.get(chatDataIntPos).getUpload() / 1024 : chartDetailsRes.get(chatDataIntPos).getUpload());

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

            xAxisDateArrList.add(DateUtil.getCustomDateAndTimeFormat(chartDetailsRes.get(chatDataIntPos).getTime(), targetDateFormat));
            downloadEntryArrList.add(new Entry(downloadFloat, chatDataIntPos));
            uploadEntryArrList.add(new Entry(uploadFloat, chatDataIntPos));

        }
        mByteConversionTxt.setText(getString(isMByteBool ? R.string.mbytes : R.string.kbytes));
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
        uploadLineDataSet.setCircleRadius(2f);
        uploadLineDataSet.setDrawCircleHole(false);
        uploadLineDataSet.setValueTextSize(0f);
        uploadLineDataSet.setDrawFilled(false);
//        uploadLineDataSet.setDrawCubic(true);


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





