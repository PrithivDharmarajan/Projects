package com.calix.calixgigaspireapp.ui.iot;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.iot.IOTTemperatureAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IOTSiren extends BaseActivity {

    @BindView(R.id.iot_home_sensor_bg_lay)
    RelativeLayout mIOTHomeLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.iot_home_bg_lay)
    RelativeLayout mIOTCommonkHeaderBgLay;

    @BindView(R.id.door_img)
    ImageView mIOTDeviceImg;

    @BindView(R.id.device_name_txt)
    TextView mEditTxt;

    @BindView(R.id.door_status_txt)
    TextView mDoorStatusTxt;

    @BindView(R.id.history_txt)
    TextView mSwitchHistoryTxt;

   /* @BindView(R.id.iot_temp_recycler_view)
    RecyclerView mTempRecyclerView;*/

    @BindView(R.id.battery_status_lay)
    RelativeLayout mBatteryStatusLay;

    @BindView(R.id.on_off_status_card_view)
    CardView mSwitchOnOffCardView;

    @BindView(R.id.siren_common_lay)
    LinearLayout mSirenCommonLay;

    @BindView(R.id.control_device_lay)
    LinearLayout mControlDeviceLay;

    @BindView(R.id.key_fob_lay)
    LinearLayout mPanicLay;

    @BindView(R.id.roller_shutter_lay)
    LinearLayout mRollerShutterLay;

    @BindView(R.id.sound_alarm_lay)
    RelativeLayout mSoundAlarmLay;

    private boolean deviceStateBool = true;

    private String mDeviceNameStr = "";

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_siren);
        initView();
    }

    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTHomeLay);

        setHeaderView();
        //setAdapter(mDeviceListResponse);
        deviceType(AppConstants.TEMP_IOT_Details.getDeviceType());
    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        /* mHeaderTxt.setText(getString(R.string.guest_network));*/

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTCommonkHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mIOTCommonkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTSiren.this)));
                    mIOTCommonkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTSiren.this), 0, 0);
                }

            });
        }
    }

    /*Set adapter*/
  /*  private void setAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mTempRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTempRecyclerView.setNestedScrollingEnabled(false);
        mTempRecyclerView.setAdapter(new IOTTemperatureAdapter(deviceListResponse, this));
    }*/

    /*find the device Image*/
    private void deviceType(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 6:
                mIOTDeviceImg.setImageResource(R.drawable.alarm);
                mDoorStatusTxt.setText(getString(R.string.alarm_is_ringing));
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.alarm));
                mEditTxt.setText(R.string.alarm);
                mSwitchHistoryTxt.setText(getString(R.string.alaram_history));
                break;
            case 63:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.almond_siren));
                mEditTxt.setText(R.string.almond_siren);
                mSwitchHistoryTxt.setText(getString(R.string.siren_history));
                mSirenCommonLay.setVisibility(View.VISIBLE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mDoorStatusTxt.setVisibility(View.GONE);
                break;
            case 42:
                mIOTDeviceImg.setImageResource(R.drawable.siren_icon);
                mHeaderTxt.setText(getString(R.string.siren));
                mEditTxt.setText(R.string.siren);
                mDoorStatusTxt.setText(R.string.siren_is_ringing);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.siren_history));
                break;

            case 52:
                mIOTDeviceImg.setImageResource(R.drawable.shutter_icon);
                mHeaderTxt.setText(getString(R.string.roller_shutter));
                mEditTxt.setText(getString(R.string.roller_shutter));
                mDoorStatusTxt.setText(getString(R.string.close));
                mRollerShutterLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.roller_shutter_history));
                mBatteryStatusLay.setVisibility(View.GONE);
                break;
            case 53:
                mIOTDeviceImg.setImageResource(R.drawable.door_open);
                mHeaderTxt.setText(getString(R.string.garage_door));
                mEditTxt.setText(getString(R.string.garage_door));
                mDoorStatusTxt.setText(getString(R.string.open));
                mRollerShutterLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.garage_door_history));
                mBatteryStatusLay.setVisibility(View.GONE);
                break;
            case 55:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.multi_sound_siren));
                mEditTxt.setText(R.string.multi_sound_siren);
                mControlDeviceLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.multi_sound_siren_history));
                break;
            case 19:
                mIOTDeviceImg.setImageResource(R.drawable.key);
                mDoorStatusTxt.setText(R.string.all_armed);
                mHeaderTxt.setText(getString(R.string.key_fob));
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mEditTxt.setText(R.string.key_fob);
                mPanicLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.key_fob_history));
                break;
            case 21:
                mIOTDeviceImg.setImageResource(R.drawable.siren);
                mDoorStatusTxt.setText(getString(R.string.sound_alarm));
                mHeaderTxt.setText(getString(R.string.standard_warning_device));
                mEditTxt.setText(R.string.standard_warning_device);
                mSoundAlarmLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.standard_warning_device_history));
                break;
            case 68:
                mIOTDeviceImg.setImageResource(R.drawable.siren_icon);
                mDoorStatusTxt.setText(getString(R.string.siren_is_ringing));
                mHeaderTxt.setText(getString(R.string.zw_siren));
                mEditTxt.setText(R.string.zw_siren);
                mSwitchHistoryTxt.setText(getString(R.string.zw_siren_history));
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                break;
                default:
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.notifications_arrow_right_lay, R.id.arrow_right_lay, R.id.on_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;

            case R.id.notifications_arrow_right_lay:
                DialogManager.getInstance().showEdtIOTNamePopup(this, deviceStateBool, deviceStateBool, new InterfaceEdtBtnCallback() {
                    @Override
                    public void onNegativeClick() {

                    }

                    @Override
                    public void onPositiveClick(String edtStr) {

                    }
                });
                break;
            case R.id.arrow_right_lay:
                DialogManager.getInstance().showEdtIOTDeviceNamePopup(this,
                        getString(R.string.device_edit_iot_header),
                        getString(R.string.device_edit_iot_sheader),
                        getString(R.string.device_edit_hint),
                        mEditTxt.getText().toString().trim(), new InterfaceEdtBtnCallback() {
                            @Override
                            public void onNegativeClick() {

                            }

                            @Override
                            public void onPositiveClick(String edtStr) {
                                mDeviceNameStr = edtStr;
                                mEditTxt.setText(edtStr);
                            }
                        });
                break;
            case R.id.on_txt:

        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
