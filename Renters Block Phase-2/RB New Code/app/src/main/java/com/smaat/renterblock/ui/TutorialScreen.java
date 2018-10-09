package com.smaat.renterblock.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smaat.renterblock.R;
import com.smaat.renterblock.main.BaseActivity;
import com.smaat.renterblock.utils.AppConstants;
import com.smaat.renterblock.utils.PreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/22/2017.
 */

public class TutorialScreen extends BaseActivity {


    @BindView(R.id.tutorial_parent_lay)
    ViewGroup mTutorialViewGroup;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.get_started_btn)
    Button mGetStartedBtn;

    @BindView(R.id.slide_pointer_one_img)
    ImageView mSlidePointerOneImg;

    @BindView(R.id.slide_pointer_two_img)
    ImageView mSlidePointerTwoImg;

    @BindView(R.id.slide_pointer_three_img)
    ImageView mSlidePointerThreeImg;

    @BindView(R.id.slide_pointer_four_img)
    ImageView mSlidePointerFourImg;

    @BindView(R.id.slide_pointer_five_img)
    ImageView mSlidePointerFiveImg;

    ArrayList<Integer> mImagesArray;
    ArrayList<String> mTutorialHeaderTxt;
    ArrayList<String> mTutorialBodyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_screen);

       /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

        /*Keypad to be hidden when a Click/touch made outside the edit text*/
        setupUI(mTutorialViewGroup);

        initView();
    }

    private void initView() {

        mImagesArray = new ArrayList<>();
        mImagesArray.add(R.drawable.tut_screen_1);
        mImagesArray.add(R.drawable.tut_screen_2);
        mImagesArray.add(R.drawable.tut_screen_3);
        mImagesArray.add(R.drawable.tut_screen_4);
        mImagesArray.add(R.drawable.tut_screen_5);

        mTutorialHeaderTxt = new ArrayList<>();
        mTutorialHeaderTxt.add(getString(R.string.tut_header1));
        mTutorialHeaderTxt.add(getString(R.string.tut_header2));
        mTutorialHeaderTxt.add(getString(R.string.tut_header3));
        mTutorialHeaderTxt.add(getString(R.string.tut_header4));
        mTutorialHeaderTxt.add(getString(R.string.tut_header5));

        mTutorialBodyTxt = new ArrayList<>();
        mTutorialBodyTxt.add(getString(R.string.tut_txt1));
        mTutorialBodyTxt.add(getString(R.string.tut_txt2));
        mTutorialBodyTxt.add(getString(R.string.tut_txt3));
        mTutorialBodyTxt.add(getString(R.string.tut_txt4));
        mTutorialBodyTxt.add(getString(R.string.tut_txt5));

        CustomPagerAdapter mPagerAdapter = new CustomPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        setListener();
        mPagerAdapter.notifyDataSetChanged();

    }

    /*View OnClick*/
    @OnClick({R.id.get_started_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_started_btn:
                PreferenceUtil.storeBoolPreferenceValue(this, AppConstants.TUTORIAL_SEEN, true);
                nextScreen(LoginScreen.class, true);
                break;
        }
    }

    private void setListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSlidePointerOneImg.setImageResource(position == 0 ? R.drawable.circle_bg_indicator_on : R.drawable.circle_bg_indicator_off);
                mSlidePointerTwoImg.setImageResource(position == 1 ? R.drawable.circle_bg_indicator_on : R.drawable.circle_bg_indicator_off);
                mSlidePointerThreeImg.setImageResource(position == 2 ? R.drawable.circle_bg_indicator_on : R.drawable.circle_bg_indicator_off);
                mSlidePointerFourImg.setImageResource(position == 3 ? R.drawable.circle_bg_indicator_on : R.drawable.circle_bg_indicator_off);
                mSlidePointerFiveImg.setImageResource(position == 4 ? R.drawable.circle_bg_indicator_on : R.drawable.circle_bg_indicator_off);


                /*Set get started btn visible*/
                mGetStartedBtn.setVisibility(position == 4 ? View.VISIBLE : View.GONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private class CustomPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;
        ImageView mPagerImage;
        TextView mHeaderTxt, mMsgTxt;

        private CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mImagesArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.swipe_tutorial_view, container, false);
            mPagerImage = view.findViewById(R.id.swipe_img);
            mHeaderTxt = view.findViewById(R.id.swipe_top_txt);
            mMsgTxt = view.findViewById(R.id.swipe_msg_txt);


            mPagerImage.setImageResource(mImagesArray.get(position));
            mHeaderTxt.setText(mTutorialHeaderTxt.get(position));
            mMsgTxt.setText(mTutorialBodyTxt.get(position));
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}
