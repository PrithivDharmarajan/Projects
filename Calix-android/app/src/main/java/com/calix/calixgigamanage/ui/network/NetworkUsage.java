package com.calix.calixgigamanage.ui.network;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calix.calixgigamanage.R;
import com.calix.calixgigamanage.fragments.DownloadFragment;
import com.calix.calixgigamanage.fragments.UploadFragment;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

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

    @BindView(R.id.pc_header_bg_lay)
    RelativeLayout mPCHeaderBgLay;

    @BindView(R.id.pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_lay)
    TabLayout mTabLayout;

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

        setHeaderView();


        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);


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
    @OnClick({R.id.header_left_img_lay, R.id.change_network_priority_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_img_lay:
                onBackPressed();
                break;
            case R.id.change_network_priority_btn:
                nextScreen(NetworkPriority.class);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backScreen();
    }
}
