package com.calix.calixgigaspireapp.ui.iot;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.calix.calixgigaspireapp.output.model.IOTRemoveDeviceEntity;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.DialogManager;
import com.calix.calixgigaspireapp.utils.InterfaceEdtBtnCallback;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.warkiz.widget.IndicatorSeekBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IOTSwitch extends BaseActivity {

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

    @BindView(R.id.iot_temp_recycler_view)
    RecyclerView mTempRecyclerView;

    @BindView(R.id.battery_status_lay)
    RelativeLayout mBatteryStatusLay;

    @BindView(R.id.on_off_status_card_view)
    CardView mSwitchOnOffCardView;

    @BindView(R.id.power_status)
    RelativeLayout mStatusWithLay;

    @BindView(R.id.brightness_seekbar_lay)
    RelativeLayout mBrightnessSeekBarLay;

    @BindView(R.id.switch_on_off_status)
    LinearLayout mSwitchOnOffStatus;

    @BindView(R.id.common_rel_lay)
    LinearLayout mCommonRelativeLay;

    @BindView(R.id.on_txt)
    TextView mOnTxt;

    @BindView(R.id.off_txt)
    TextView mOffTxt;

    @BindView(R.id.brightness_percentage)
    TextView mBrightnessTxt;

    private boolean deviceStateBool = true;

    @BindView(R.id.discrete_times)
    IndicatorSeekBar mSeekBarHour;

    private String mDeviceNameStr = "";

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_switch);
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
        brightnessSeekBar();
        setAdapter(mDeviceListResponse);
        deviceType(AppConstants.TEMP_IOT_Details.getDeviceType());
    }

    private void brightnessSeekBar() {
        mSeekBarHour.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
                mBrightnessTxt.setText(progress + " %");
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
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
                    mIOTCommonkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTSwitch.this)));
                    mIOTCommonkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTSwitch.this), 0, 0);
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

    /*find the device Image*/
    private void deviceType(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 1:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.binary_switch));
                mEditTxt.setText(R.string.binary_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.GONE);
                mStatusWithLay.setVisibility(View.GONE);
                mSwitchHistoryTxt.setText(getString(R.string.binary_switch_history));

                break;
            case 2:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.multi_level_switch));
                mEditTxt.setText(R.string.multi_level_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.GONE);
                mStatusWithLay.setVisibility(View.GONE);
                mBrightnessSeekBarLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.multi_level_switch_history));
                mOnTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.blue : R.color.grey));
                mOffTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.grey : R.color.gray));
                break;
            case 4:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.multi_level_switch));
                mEditTxt.setText(R.string.multi_level_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.GONE);
                mStatusWithLay.setVisibility(View.GONE);
                mBrightnessSeekBarLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.multi_level_switch_history));
                mOnTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.blue : R.color.grey));
                mOffTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.grey : R.color.gray));
                break;
            case 12:
                mIOTDeviceImg.setImageResource(R.drawable.door_open);
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.contact_switch));
                mEditTxt.setText(R.string.contact_switch);
                mDoorStatusTxt.setText(getString(R.string.open));
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mStatusWithLay.setVisibility(View.GONE);
                mSwitchHistoryTxt.setText(getString(R.string.contact_switch_history));
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;

            case 22:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.smart_ac_switch));
                mEditTxt.setText(R.string.smart_ac_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mStatusWithLay.setVisibility(View.GONE);
                mBrightnessSeekBarLay.setVisibility(View.GONE);
                mSwitchOnOffStatus.setVisibility(View.GONE);
                mSwitchHistoryTxt.setText(getString(R.string.smart_ac_switch_history));
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            case 43:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.GONE);
                mHeaderTxt.setText(getString(R.string.multi_switch));
                mEditTxt.setText(R.string.multi_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.GONE);
                mStatusWithLay.setVisibility(View.GONE);
                mBrightnessSeekBarLay.setVisibility(View.GONE);
                mSwitchOnOffStatus.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.multi_switch_history));
                break;
            case 45:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.binary_power_switch));
                mEditTxt.setText(R.string.binary_power_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mStatusWithLay.setVisibility(View.GONE);
                mSwitchHistoryTxt.setText(getString(R.string.binary_power_switch_history));
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;

            case 50:
                mIOTDeviceImg.setVisibility(View.GONE);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.securifi_smart_switch));
                mEditTxt.setText(R.string.securifi_smart_switch);
                mDoorStatusTxt.setVisibility(View.GONE);
                mBatteryStatusLay.setVisibility(View.GONE);
                mTempRecyclerView.setVisibility(View.VISIBLE);
                mStatusWithLay.setVisibility(View.GONE);
                mBrightnessSeekBarLay.setVisibility(View.GONE);
                mSwitchOnOffStatus.setVisibility(View.GONE);
                mSwitchHistoryTxt.setText(getString(R.string.securifi_smart_switch_history));
                mCommonRelativeLay.setVisibility(View.VISIBLE);
                break;
            default:
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.notifications_arrow_right_lay, R.id.arrow_right_lay, R.id.on_txt, R.id.off_txt})
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
                mOnTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.blue : R.color.grey));
                mOffTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.grey : R.color.gray));
                break;
            case R.id.off_txt:
                mOnTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.blue : R.color.grey));
                mOffTxt.setBackgroundColor(ContextCompat.getColor(this, deviceStateBool ? R.color.grey : R.color.gray));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
