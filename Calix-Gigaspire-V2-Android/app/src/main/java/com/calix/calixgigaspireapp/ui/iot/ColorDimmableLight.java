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

public class ColorDimmableLight extends BaseActivity {

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

    @BindView(R.id.history_txt)
    TextView mSwitchHistoryTxt;

 /*   @BindView(R.id.iot_temp_recycler_view)
    RecyclerView mTempRecyclerView;*/

    @BindView(R.id.on_off_status_card_view)
    CardView mSwitchOnOffCardView;

    @BindView(R.id.color_dimmable_common_lay)
    LinearLayout mColorDimmableLay;

    @BindView(R.id.hue_bulb_common_lay)
    LinearLayout mHueBulbCommonLay;

    private boolean deviceStateBool = true;

    private String mDeviceNameStr = "";

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_color_dimmable_light);
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
                    mIOTCommonkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ColorDimmableLight.this)));
                    mIOTCommonkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ColorDimmableLight.this), 0, 0);
                }

            });
        }
    }

    /*Set adapter*/
 /*   private void setAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mTempRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mTempRecyclerView.setNestedScrollingEnabled(false);
        mTempRecyclerView.setAdapter(new IOTTemperatureAdapter(deviceListResponse, this));
    }*/

    /*find the device Image*/
    private void deviceType(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 32:
                mIOTDeviceImg.setImageResource(R.drawable.unlock);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.color_dimmable_light));
                mEditTxt.setText(R.string.color_dimmable_light);
                mColorDimmableLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.color_dimmable_light_history));
                break;
            case 98:
                mIOTDeviceImg.setImageResource(R.drawable.unlock);
                mSwitchOnOffCardView.setVisibility(View.VISIBLE);
                mHeaderTxt.setText(getString(R.string.hue_bulb));
                mEditTxt.setText(R.string.hue_bulb);
                mHueBulbCommonLay.setVisibility(View.VISIBLE);
                mSwitchHistoryTxt.setText(getString(R.string.hue_bulb_history));
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
