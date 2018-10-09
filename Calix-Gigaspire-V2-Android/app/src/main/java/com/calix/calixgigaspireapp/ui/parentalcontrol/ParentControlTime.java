package com.calix.calixgigaspireapp.ui.parentalcontrol;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.fragment.BedTime;
import com.calix.calixgigaspireapp.fragment.TimeLimit;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentControlTime extends BaseActivity {

    @BindView(R.id.user_time_minutes_par_lay)
    ViewGroup mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.hours_time_usage_header_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_lay)
    TabLayout mTabLayout;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_pc_time);

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

        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        setHeaderView();

        mNotificationLay.setVisibility(View.VISIBLE);

        /*Notification function*/
        if (AppConstants.ALERT_COUNT > 0) {
            mNotificationCountLay.setVisibility(View.VISIBLE);
            mNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mNotificationCountLay.setVisibility(View.GONE);
        }
    }

    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.set_time));
        mHeaderTxt.setVisibility(IsScreenModePortrait() ? View.VISIBLE : View.VISIBLE);

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentControlTime.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentControlTime.this), 0, 0);
                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new TimeLimit());
        adapter.addFragment(new BedTime());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(adapter.getCount());
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.time_limitt) : getString(R.string.bed_time);
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.header_right_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;
            case R.id.header_right_img_lay:
                nextScreen(Alert.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(ParentControlInsights.class);
    }
}
