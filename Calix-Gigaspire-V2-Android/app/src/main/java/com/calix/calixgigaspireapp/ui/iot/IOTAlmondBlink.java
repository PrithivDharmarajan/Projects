package com.calix.calixgigaspireapp.ui.iot;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IOTAlmondBlink extends BaseActivity {

    @BindView(R.id.iot_home_sensor_bg_lay)
    RelativeLayout mIOTHomeLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.device_name_txt)
    TextView mEditTxt;

    @BindView(R.id.iot_home_bg_lay)
    RelativeLayout mIOTCommonkHeaderBgLay;

    @BindView(R.id.history_txt)
    TextView mSwitchHistoryTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_almond_blink);

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
        deviceType(AppConstants.TEMP_IOT_Details.getDeviceType());
    }

    private void setHeaderView() {
        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);


        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTCommonkHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mIOTCommonkHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTAlmondBlink.this)));
                    mIOTCommonkHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTAlmondBlink.this), 0, 0);
                }

            });
        }
    }

    /*find the device Image*/
    private void deviceType(int deviceTypeInt) {
        switch (deviceTypeInt) {
            case 64:
                mHeaderTxt.setText(getString(R.string.almond_blink));
                mEditTxt.setText(R.string.almond_blink);
                mSwitchHistoryTxt.setText(getString(R.string.almond_blink_history));
                break;
            default:
        }
    }
}
