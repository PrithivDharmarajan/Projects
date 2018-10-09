package com.calix.calixgigaspireapp.ui.network;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigaspireapp.R;
import com.calix.calixgigaspireapp.fragment.DownloadFragment;
import com.calix.calixgigaspireapp.fragment.UploadFragment;
import com.calix.calixgigaspireapp.main.BaseActivity;
import com.calix.calixgigaspireapp.ui.dashboard.Alert;
import com.calix.calixgigaspireapp.ui.dashboard.Dashboard;
import com.calix.calixgigaspireapp.ui.parentalcontrol.ParentalControlDashBoard;
import com.calix.calixgigaspireapp.utils.AppConstants;
import com.calix.calixgigaspireapp.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NetworkUsage extends BaseActivity {

    @BindView(R.id.network_usage_par_lay)
    RelativeLayout mParentControlLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.network_usage_header_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_lay)
    TabLayout mTabLayout;

    @BindView(R.id.header_right_img_lay)
    RelativeLayout mNotificationLay;

    @BindView(R.id.notification_count_lay)
    RelativeLayout mNotificationCountLay;

    @BindView(R.id.notification_count_txt)
    TextView mNotificationCountTxt;

    @BindView(R.id.notification_count_temp_txt)
    TextView mNotificationCountTempTxt;

    /* Footer Variables */

    @BindView(R.id.footer_first_img)
    ImageView mFooterFirstImg;

    @BindView(R.id.footer_one_txt)
    TextView mFooterOneTxt;

    @BindView(R.id.footer_second_img)
    ImageView mFooterSecondImg;

    @BindView(R.id.footer_second_txt)
    TextView mFooterSecondTxt;

    @BindView(R.id.footer_notification_count_lay)
    RelativeLayout mFooterNotificationCountLay;

    @BindView(R.id.footer_notification_count_temp_txt)
    TextView mFooterNotificationCountTempTxt;

    @BindView(R.id.footer_notification_count_txt)
    TextView mFooterNotificationCountTxt;

    @BindView(R.id.footer_third_lay)
    LinearLayout mFooterThirdLay;

    @BindView(R.id.footer_third_view)
    View mFooterThirdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_network_usage);
        initView();
    }

    private void initView() {

        /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

        /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mParentControlLay);

        /*Header view*/
        setHeaderView();

        /*Footer view*/
        setFooterVIew();

        /*Tab view pager*/
        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

        /*Header Alert notification*/
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
        mHeaderTxt.setText(getString(R.string.network_usage));

        /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(NetworkUsage.this)));
                    mPCHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(NetworkUsage.this), 0, 0);
                }

            });
        }
    }

    /*Set Footer View */
    private void setFooterVIew() {
        if (AppConstants.ALERT_COUNT > 0) {
            mFooterNotificationCountLay.setVisibility(View.VISIBLE);
            mFooterNotificationCountTempTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
            mFooterNotificationCountTxt.setText(String.valueOf(AppConstants.ALERT_COUNT));
        } else {
            mFooterNotificationCountLay.setVisibility(View.GONE);
        }

        mFooterFirstImg.setImageResource(R.drawable.ic_dashboard);
        mFooterOneTxt.setText(getString(R.string.dashboard));

        mFooterSecondImg.setImageResource(R.drawable.ic_notification);
        mFooterSecondTxt.setText(getString(R.string.alert));
        mFooterThirdLay.setVisibility(View.GONE);
        mFooterThirdView.setVisibility(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new DownloadFragment());
        adapter.addFragment(new UploadFragment());

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
            return position == 0 ? getString(R.string.download) : getString(R.string.upload);
        }

        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }

    /*View onClick*/
    @OnClick({R.id.header_left_img, R.id.footer_first_lay, R.id.footer_second_lay, R.id.header_right_img_lay})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img:
                onBackPressed();
                break;
            case R.id.footer_first_lay:
                previousScreen(Dashboard.class);
                break;
            case R.id.footer_second_lay:
                nextScreen(Alert.class);
                break;
            case R.id.header_right_img_lay:
                nextScreen(Alert.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        previousScreen(Dashboard.class);
    }

}
