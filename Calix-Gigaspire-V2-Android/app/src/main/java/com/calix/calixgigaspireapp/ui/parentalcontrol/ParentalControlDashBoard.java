package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.adapter.parentcontrol.PCDashBoardAdapter;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.output.model.DeviceEntity;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentalControlDashBoard extends BaseActivity {

    @BindView(R.id.parental_control_dashboard_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.pc_header_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.parent_control_recycler_view)
    RecyclerView mParentControlRecyclerView;

    @BindView(R.id.add_device_img)
    ImageView mAddDeviceImg;

    @BindView(R.id.pc_switch_compact)
    SwitchButton mSwitchCompact;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mRightSideLay;

    @BindView(R.id.play_pause_img)
    ImageView mPlayPauseImg;

    private boolean mIsChangePlayPauseBtn = false;
    private boolean mPauseAllBool = false;

    private ArrayList<DeviceEntity> mDeviceListResponse = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_dashboard);

        initView();
    }

    /*View initialization*/
    private void initView() {
        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlLay);

        setHeaderView();

        if (mSwitchCompact != null) {
            mSwitchCompact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
//                        mSwitchCompact.setBackColor(ContextCompat.getColorStateList(ParentalControlDashBoard.this,R.color.blue));
                        mSwitchCompact.setBackColor(ContextCompat.getColorStateList(ParentalControlDashBoard.this, R.color.blue));

                    } else {
//                        mSwitchCompact.setBackColor(ColorStateList.valueOf(Color.parseColor("#8a8a8a")));
                        mSwitchCompact.setBackColor(ContextCompat.getColorStateList(ParentalControlDashBoard.this, R.color.deep_gray));
                    }
                }
            });
        }

        /*Parent Control Api*/
        //parentControlAPI();
        setAdapter(mDeviceListResponse);
    }

    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.parent_control));
        mRightSideLay.setVisibility(View.INVISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlDashBoard.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlDashBoard.this), 0, 0);
                }

            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img_lay, R.id.dashboard_lay, R.id.play_pause_img, R.id.search_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.dashboard_lay:
                nextScreen(Dashboard.class);
                break;

            case R.id.play_pause_img:
                if (!mIsChangePlayPauseBtn) {
                    mPauseAllBool = true;
                    mPlayPauseImg.setImageResource(R.drawable.play_icon);
                    mIsChangePlayPauseBtn = true;
                } else {
                    mPauseAllBool = true;
                    mPlayPauseImg.setImageResource(R.drawable.pause_icon);
                    mIsChangePlayPauseBtn = false;
                }
                break;

            case R.id.search_lay:
                break;
        }
    }

    /*API call*/
    private void parentControlAPI() {
        //APIRequestHandler.getInstance().deviceListAPICall("", this);
    }

    /*Set adapter*/
    private void setAdapter(ArrayList<DeviceEntity> deviceListResponse) {
        mParentControlRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mParentControlRecyclerView.setNestedScrollingEnabled(false);
        mParentControlRecyclerView.setAdapter(new PCDashBoardAdapter(deviceListResponse, this));
    }

    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }

}
