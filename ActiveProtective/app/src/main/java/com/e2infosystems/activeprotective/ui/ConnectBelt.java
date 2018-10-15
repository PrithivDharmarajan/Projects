package com.e2infosystems.activeprotective.ui;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.e2infosystems.activeprotective.R;
import com.e2infosystems.activeprotective.main.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ConnectBelt extends BaseActivity {

    @BindView(R.id.connect_belt_parent_lay)
    ViewGroup mConnectBeltViewGroup;

    @BindView(R.id.header_lay)
    RelativeLayout mHeaderLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_connect_belt);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mConnectBeltViewGroup);

        mHeaderTxt.setText(getString(R.string.connect_to_belt));

        setHeaderAdjustmentView();
    }

    /*Screen orientation changes*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setHeaderAdjustmentView();
    }

    /*Set header view*/
    private void setHeaderAdjustmentView() {
        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mHeaderLay.post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mHeaderLay.setPadding(0, getStatusBarHeight(ConnectBelt.this), 0, 0);
                        }
                    });
                }
            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_start_img_lay,R.id.cancel_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_start_img_lay:
            case R.id.cancel_btn:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}

