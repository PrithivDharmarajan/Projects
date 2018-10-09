package com.calix.calixgigaspireapp.ui.wifi;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.wifi.WiFiSpeedResAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.RouterMapEntity;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiSpeedResult extends BaseActivity {

    @BindView(R.id.wifi_speed_result_bg_lay)
    ViewGroup mWifiHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.wifi_speed_header_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.wifi_speed_result_recycler_view)
    RecyclerView mWifiSpeedResultRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_wifi_speed_result);

        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mWifiHeaderLay);

        setHeaderView();

        /*set Adapter*/
        setAdapter(AppConstants.SPEED_TEST_RESULT);

    }

    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.speed_result));
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(WifiSpeedResult.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(WifiSpeedResult.this), 0, 0);
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.dashboard_lay, R.id.refresh_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;

            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);
                break;

            case R.id.refresh_lay:
                nextScreen(InternetSpeedTest.class);
                break;
        }
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<RouterMapEntity> speedTestResultRes) {

        mWifiSpeedResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mWifiSpeedResultRecyclerView.setNestedScrollingEnabled(false);
        mWifiSpeedResultRecyclerView.setAdapter(new WiFiSpeedResAdapter(speedTestResultRes, this));
    }

    @Override
    public void onBackPressed() {
        previousScreen(InternetSpeedTest.class);
    }
}
