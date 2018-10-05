package com.calix.calixgigamanage.ui.parentalcontrol;

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
import com.calix.calixgigamanage.fragments.ParentalControlFilterFragment;
import com.calix.calixgigamanage.main.BaseActivity;
import com.calix.calixgigamanage.utils.AppConstants;
import com.calix.calixgigamanage.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParentalControlFilter extends BaseActivity {

    @BindView(R.id.pc_filter_par_lay)
    RelativeLayout mPCFileterLay;

    @BindView(R.id.header_txt)
    TextView mHeaderTxt;

    @BindView(R.id.pc_filter_header_bg_lay)
    RelativeLayout mPCFilterHeaderBgLay;

    @BindView(R.id.pc_filter_pager)
    ViewPager mPCFilterViewPager;

    @BindView(R.id.pc_filter_tab_lay)
    TabLayout mPCFilterTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_parental_control_filter);
        initView();
    }


    private void initView() {


            /*For error track purpose - log with class name*/
        AppConstants.TAG = this.getClass().getSimpleName();

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mPCFileterLay);

        setHeaderView();


        setupViewPager(mPCFilterViewPager);
        mPCFilterTabLayout.setupWithViewPager(mPCFilterViewPager);


    }


    private void setHeaderView() {

        /*Header*/
        mHeaderTxt.setVisibility(View.VISIBLE);
        mHeaderTxt.setText(getString(R.string.filter_for_samantha));

         /*Set header adjustment - status bar we applied transparent color so header tack full view*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPCFilterHeaderBgLay.post(new Runnable() {
                public void run() {
                    int heightInt = getResources().getDimensionPixelSize(R.dimen.size45);
                    mPCFilterHeaderBgLay.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightInt + NumberUtil.getInstance().getStatusBarHeight(ParentalControlFilter.this)));
                    mPCFilterHeaderBgLay.setPadding(0, NumberUtil.getInstance().getStatusBarHeight(ParentalControlFilter.this), 0, 0);
                }

            });
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFragment(new ParentalControlFilterFragment());
        adapter.addFragment(new ParentalControlFilterFragment());
        adapter.addFragment(new ParentalControlFilterFragment());
        adapter.addFragment(new ParentalControlFilterFragment());
        adapter.addFragment(new ParentalControlFilterFragment());

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
            String title = "";
            switch (position){
                case 0:
                    title = getString(R.string.adult_cap);
                    break;
                case 1:
                    title = getString(R.string.teen_cap);
                    break;
                case 2:
                    title = getString(R.string.kid_cap);
                    break;
                case 3:
                    title = getString(R.string.pre_k_cap);
                    break;
                case 4:
                    title = getString(R.string.none_cap);
                    break;
            }
            return title;
        }


        private void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

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

    @Override
    public void onBackPressed() {
        backScreen();
    }
}

