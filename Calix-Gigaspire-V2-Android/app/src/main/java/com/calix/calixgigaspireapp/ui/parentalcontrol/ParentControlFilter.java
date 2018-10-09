package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;
import com.warkiz.widget.IndicatorSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentControlFilter extends BaseActivity {

    @BindView(R.id.filter_header_bg_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.filter_status_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.filter_selection_txt)
    TextView mFilterTap;

    @BindView(R.id.selected_count_txt)
    TextView mFilterCount;

    @BindView(R.id.seekbar_filter)
    IndicatorSeekBar mFilterSeekBar;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_filter);

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
        /*seek bar*/
        indicatorSeekBarFilter();

        mNotificationLay.setVisibility(View.VISIBLE);

        /*Notification function*/
        if (AppConstants.ALERT_COUNT > 0) {
            mNotificationCountLay.setVisibility(View.VISIBLE);
            mNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mNotificationCountLay.setVisibility(View.GONE);
        }
        /*Filter List Api*/
        //filterListAPI();
    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.filter_for_john));
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentControlFilter.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentControlFilter.this), 0, 0);
                }

            });
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.allow_btn, R.id.decline_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;
            case R.id.allow_btn:
                break;
            case R.id.decline_btn:
                break;
        }
    }

    public void indicatorSeekBarFilter() {
        mFilterSeekBar.setOnSeekChangeListener(new IndicatorSeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(IndicatorSeekBar seekBar, int progress, float progressFloat, boolean fromUserTouch) {
            }

            @Override
            public void onSectionChanged(IndicatorSeekBar seekBar, int thumbPosOnTick, String textBelowTick, boolean fromUserTouch) {
                //only callback on discrete series SeekBar type.
                mFilterCount.setText("For john " + textBelowTick + " was selected");
            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar, int thumbPosOnTick) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        previousScreen(ParentControlInsights.class);
    }
}
