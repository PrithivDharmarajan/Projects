package com.calix.calixgigamanage.ui.iot;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by prithiviraj on 11/22/2017.
 */

public class IOTDeviceReadyToUse extends BaseActivity {

    @BindView(R.id.iot_device_ready_parent_lay)
    ViewGroup mIOTDeviceReadyViewGroup;

    @BindView(R.id.iot_device_ready_header_bg_lay)
    RelativeLayout mIOTDeviceReadyHeaderBgLay;

    @BindView(R.id.iot_device_ready_header_msg_lay)
    RelativeLayout mIOTDeviceReadyHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ui_iot_device_ready_use);
        initView();
    }


    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mIOTDeviceReadyViewGroup);

        setHeaderView();
    }

    private void setHeaderView() {

        /*set header changes*/
        mIOTDeviceReadyHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.INVISIBLE : View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.device_ready));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mIOTDeviceReadyHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mIOTDeviceReadyHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(IOTDeviceReadyToUse.this)));
                    mIOTDeviceReadyHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(IOTDeviceReadyToUse.this), 0, 0);
                    mIOTDeviceReadyHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay, R.id.done_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
            case R.id.done_btn:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        previousScreen(IOTDeviceList.class);
    }
}