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
import com.calix.calixgigaspireapp.utils.InterfaceTwoEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IOTSensor extends BaseActivity {

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

    @BindView(R.id.pin_num_txt)
    TextView mSetPin;

    @BindView(R.id.door_status_txt)
    TextView mDoorStatusTxt;

    @BindView(R.id.history_txt)
    TextView mSwitchHistoryTxt;

    @BindView(R.id.iot_temp_recycler_view)
    RecyclerView mTempRecyclerView;

    @BindView(R.id.battery_status_lay)
    RelativeLayout mBatteryStatusLay;

    @BindView(R.id.water_sen_temp)
    RelativeLayout mWaterSenTemp;

    @BindView(R.id.on_off_status_card_view)
    CardView mSwitchOnOffCardView;

    @BindView(R.id.power_status)
    RelativeLayout mStatusWithLay;

    @BindView(R.id.common_rel_lay)
    LinearLayout mCommonRelativeLay;

    @BindView(R.id.pin_right_lay)
    RelativeLayout mSetPinLay;

    @BindView(R.id.energy_reader_recycler_view)
    RecyclerView mEnergyReaderRecyclerView;

    @BindView(R.id.clamp_1_lay)
    LinearLayout mEnergyReaderStatusLay;

    @BindView(R.id.garage_door_lay)
    LinearLayout mGarageDoorStatus;

    private boolean deviceStateBool = true;

    private String mDeviceNameStr = "";
    private String mPinStr = "";

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_sensor);
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
        setAdapter(mDeviceListResponse);
        setEnergyReaderAdapter(mDeviceListResponse);
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
                    mIOTCommonkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTSensor.this)));
                    mIOTCommonkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTSensor.this), 0, 0);
                }

            });
        }
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mTempRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTempRecyclerView.setNestedScrollingEnabled(false);
        mTempRecyclerView.setAdapter(new IOTTemperatureAdapter(deviceListResponse, this));
    }

    /*set energy adapter*/
    private void setEnergyReaderAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mEnergyReaderRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mEnergyReaderRecyclerView.setNestedScrollingEnabled(false);
        mEnergyReaderRecyclerView.setAdapter(new IOTTemperatureAdapter(deviceListResponse, this));
    }

    /*find the device Image*/
    private void deviceType(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 3:
                mIOTDeviceImg.setImageResource(R.drawable.door_close);
                mHeaderTxt.setText(getString(R.string.binary_sensor));
                mEditTxt.setText(getString(R.string.binary_sensor));
                break;
            case 10:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.standard_cie));
                mEditTxt.setText(getString(R.string.standard_cie));
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.standard_cie_history));
                mDoorStatusTxt.setVisibility(View.GONE);
                break;
            case 39:
                mIOTDeviceImg.setImageResource(R.drawable.door_open);
                mHeaderTxt.setText(getString(R.string.door_sensor));
                mEditTxt.setText(R.string.door_sensor);
                mDoorStatusTxt.setText(R.string.door_open);
                break;
            case 37:
                mIOTDeviceImg.setImageResource(R.drawable.flood);
                mHeaderTxt.setText(getString(R.string.flood_sensor));
                mEditTxt.setText(R.string.flood_sensor);
                mDoorStatusTxt.setText(R.string.flooded);
                break;
            case 44:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.unknown_onoff_module));
                mEditTxt.setText(R.string.unknown_onoff_module);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.unknown_onoff_module_history));
                break;
            case 53:
                mIOTDeviceImg.setImageResource(R.drawable.garage_door);
                mHeaderTxt.setText(getString(R.string.garage_door));
                mEditTxt.setText(R.string.garage_door);
                mDoorStatusTxt.setText(R.string.open);
                mGarageDoorStatus.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.garage_door_history));
                break;
            case 100:
                mIOTDeviceImg.setImageResource(R.drawable.flood);
                mHeaderTxt.setText(getString(R.string.moisture_sensor));
                mEditTxt.setText(R.string.moisture_sensor);
                mDoorStatusTxt.setText(R.string.flooded);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 11:
                mIOTDeviceImg.setImageResource(R.drawable.motion_sensor);
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.motion_sensor_name));
                mEditTxt.setText(R.string.motion_sensor_name);
                mDoorStatusTxt.setText(getString(R.string.motion_detected));
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mStatusWithLay.setVisibility(View.GONE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 13:
                mIOTDeviceImg.setImageResource(R.drawable.fire_sensor_green);
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.fire_sensor));
                mEditTxt.setText(R.string.fire_sensor);
                mDoorStatusTxt.setText(getString(R.string.fire_detected));
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.fire_sensor_history));
                break;
            case 14:
                mIOTDeviceImg.setImageResource(R.drawable.flood_red);
                mHeaderTxt.setText(getString(R.string.water_sensor));
                mEditTxt.setText(R.string.water_sensor);
                mDoorStatusTxt.setText(getString(R.string.flooded));
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mWaterSenTemp.setVisibility(View.GONE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 24:
                mIOTDeviceImg.setImageResource(R.drawable.occupancy_icon);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.occupancy_sensor));
                mEditTxt.setText(R.string.occupancy_sensor);
                mDoorStatusTxt.setText(R.string.detected);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 25:
                mIOTDeviceImg.setImageResource(R.drawable.brightness);
                mHeaderTxt.setText(getString(R.string.light_sensor));
                mEditTxt.setText(R.string.light_sensor);
                mDoorStatusTxt.setText("80 Lux");
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 27:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.temp_sensor));
                mEditTxt.setText(R.string.temp_sensor);
                mDoorStatusTxt.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 29:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.zigbee_temp_sesor));
                mEditTxt.setText(R.string.zigbee_temp_sesor);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.zigbee_temperature_history));
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 38:
                mIOTDeviceImg.setImageResource(R.drawable.vibration_on);
                mHeaderTxt.setText(getString(R.string.shock_sensor));
                mEditTxt.setText(R.string.shock_sensor);
                mDoorStatusTxt.setText(R.string.vibration_detected);
                mTempRecyclerView.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                break;

            case 41:
                mIOTDeviceImg.setImageResource(R.drawable.movement_icon);
                mHeaderTxt.setText(getString(R.string.movement_sensor));
                mEditTxt.setText(R.string.movement_sensor);
                mDoorStatusTxt.setText(R.string.motion_detected);
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                break;
            case 49:
                mIOTDeviceImg.setImageResource(R.drawable.movement_icon);
                mHeaderTxt.setText(getString(R.string.multi_sensor));
                mEditTxt.setText(R.string.multi_sensor);
                mDoorStatusTxt.setText(R.string.motion_detected);
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 5:
                mIOTDeviceImg.setImageResource(R.drawable.lock);
                mHeaderTxt.setText(getString(R.string.door_lock));
                mEditTxt.setText(getString(R.string.door_lock));
                mSwitchHistoryTxt.setText(getString(R.string.door_lock_history));
                mDoorStatusTxt.setText(getString(R.string.door_close));
                break;
            case 28:
                mIOTDeviceImg.setImageResource(R.drawable.unlock);
                mHeaderTxt.setText(getString(R.string.zigbee_door_lock));
                mDoorStatusTxt.setText(getString(R.string.door_open));
                mEditTxt.setText(R.string.zigbee_door_lock);
                mSetPinLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.zigbee_door_lock_history));
                break;
            case 36:
                mIOTDeviceImg.setImageResource(R.drawable.smoke_detector_icon);
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.smoke_detector));
                mEditTxt.setText(R.string.smoke_detected);
                mDoorStatusTxt.setText(R.string.smoke_detected);
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                mTempRecyclerView.setVisibility(View.GONE);
                mStatusWithLay.setVisibility(View.GONE);
                break;
            case 56:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.energy_reader));
                mEditTxt.setText(R.string.energy_reader);
                mEnergyReaderRecyclerView.setVisibility(View.VISIBLE);
                mEnergyReaderStatusLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.energy_reader_history));
                break;
            case 61:
                mIOTDeviceImg.setImageResource(R.drawable.securifi_icon);
                mHeaderTxt.setText(getString(R.string.securifi_button));
                mEditTxt.setText(R.string.securifi_button);
                mSwitchHistoryTxt.setText(getString(R.string.securifi_history));
                break;
            case 65:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.zb_temp_sensor));
                mEditTxt.setText(R.string.zb_temp_sensor);
                mSwitchHistoryTxt.setText(getString(R.string.zb_temp_sensor_history));
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                break;
            case 99:
                mIOTDeviceImg.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.peanut_plug));
                mEditTxt.setText(R.string.peanut_plug);
                mEnergyReaderRecyclerView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.peanut_plug_history));
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                break;
            case 101:
                mIOTDeviceImg.setImageResource(R.drawable.smoke_detected);
                mHeaderTxt.setText(getString(R.string.nest_protect));
                mEditTxt.setText(R.string.nest_protect);
                mEnergyReaderRecyclerView.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.nest_protect_history));
                mDoorStatusTxt.setText(getString(R.string.smoke_detected));
                break;
            case 71:
                mIOTDeviceImg.setImageResource(R.drawable.door_close);
                mHeaderTxt.setText(getString(R.string.zw_door_sensor));
                mEditTxt.setText(R.string.zw_door_sensor);
                mSwitchHistoryTxt.setText(getString(R.string.zw_door_sensor_history));
                mDoorStatusTxt.setText(getString(R.string.door_close));
                break;
            case 66:
                mIOTDeviceImg.setImageResource(R.drawable.flood);
                mHeaderTxt.setText(getString(R.string.zw_flood_sensor));
                mEditTxt.setText(R.string.zw_flood_sensor);
                mSwitchHistoryTxt.setText(getString(R.string.zw_flood_sensor_history));
                mDoorStatusTxt.setText(getString(R.string.flooded));
                break;
            case 67:
                mIOTDeviceImg.setImageResource(R.drawable.flood);
                mHeaderTxt.setText(getString(R.string.zw_moisture_sensor));
                mEditTxt.setText(R.string.zw_moisture_sensor);
                mSwitchHistoryTxt.setText(getString(R.string.zw_moisture_history));
                mDoorStatusTxt.setText(getString(R.string.flooded));
                mTempRecyclerView.setVisibility(View.VISIBLE);
                break;
            case 69:
                mIOTDeviceImg.setImageResource(R.drawable.smoke_detector_icon);
                mHeaderTxt.setText(getString(R.string.zw_smoke_detector));
                mEditTxt.setText(R.string.zw_smoke_detector);
                mSwitchHistoryTxt.setText(getString(R.string.zw_smoke_detector_history));
                mDoorStatusTxt.setText(getString(R.string.smoke_detected));
                mBatteryStatusLay.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.notifications_arrow_right_lay, R.id.arrow_right_lay, R.id.on_txt, R.id.pin_right_img})
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
                break;
            case R.id.pin_right_img:
                DialogManager.getInstance().showEdtIOTSetPinPopup(IOTSensor.this,
                        mSetPin.getText().toString().trim(), mPinStr, new InterfaceTwoEdtBtnCallback() {
                            @Override
                            public void onPositiveClick(String editFirstStr, String editSecondStr) {
                                mSetPin.setText(editSecondStr);
                            }

                            @Override
                            public void onNegativeClick() {

                            }
                        });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
