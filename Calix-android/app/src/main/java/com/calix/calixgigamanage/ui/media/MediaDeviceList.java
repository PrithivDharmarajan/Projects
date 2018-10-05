package com.calix.calixgigamanage.ui.media;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.adapter.MyMediaDeviceListAdapter;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.NumberUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MediaDeviceList extends BaseActivity {

    @BindView(R.id.media_device_parent_lay)
    ViewGroup mMediaDeviceViewGroup;

    @BindView(R.id.media_device_header_bg_lay)
    RelativeLayout mMediaDeviceHeaderBgLay;

    @BindView(R.id.media_device_header_msg_lay)
    RelativeLayout mMediaDeviceHeaderMsgLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.media_device_recycler_view)
    RecyclerView mMediaDeviceRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_media_device_list);
        initView();
    }


    /*View initialization*/
    private void initView() {
        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mMediaDeviceViewGroup);

        setHeaderView();
        setAdapter();
    }

    private void setHeaderView() {

        /*set header changes*/
        mMediaDeviceHeaderMsgLay.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.GONE);
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText("Media Name");

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mMediaDeviceHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(IsScreenModePortrait() ? R.dimen.size190 : R.dimen.size45);
                    mMediaDeviceHeaderBgLay.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(MediaDeviceList.this)));
                    mMediaDeviceHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(MediaDeviceList.this), 0, 0);
                    mMediaDeviceHeaderBgLay.setBackground(IsScreenModePortrait() ? getResources().getDrawable(R.drawable.header_violet_bg) : getResources().getDrawable(R.color.violet));
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
    @OnClick({R.id.header_left_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;

        }
    }

    private void setAdapter() {
        MyMediaDeviceListAdapter mediaAdapter = new MyMediaDeviceListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMediaDeviceRecyclerView.setLayoutManager(linearLayoutManager);
        mMediaDeviceRecyclerView.setAdapter(mediaAdapter);
        mMediaDeviceRecyclerView.setNestedScrollingEnabled(false);

    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}