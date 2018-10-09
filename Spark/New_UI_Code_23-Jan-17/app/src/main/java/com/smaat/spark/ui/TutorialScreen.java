package com.smaat.spark.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smaat.spark.R;
import com.smaat.spark.main.BaseActivity;
import com.smaat.spark.utils.AppConstants;
import com.smaat.spark.utils.GlobalMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;


public class TutorialScreen extends BaseActivity {

    @BindView(R.id.parent_lay)
    ViewGroup mTutorialViewGroup;

    @BindView(R.id.tutorial_img_pager)
    ViewPager mTutorialImgPager;

    @BindView(R.id.indicator_one_img)
    ImageView mIndicatorOneImg;

    @BindView(R.id.indicator_two_img)
    ImageView mIndicatorTwoImg;

    @BindView(R.id.indicator_three_img)
    ImageView mIndicatorThreeImg;

    @BindView(R.id.indicator_four_img)
    ImageView mIndicatorFourImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_tutorial_screen);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        setupUI(mTutorialViewGroup);

        //Added drawable local images to array list
        ArrayList<Integer> tutorialImgIntArrList = new ArrayList<>();
        tutorialImgIntArrList.add(R.drawable.tutorial_one);
        tutorialImgIntArrList.add(R.drawable.tutorial_two);
        tutorialImgIntArrList.add(R.drawable.tutorial_three);
        tutorialImgIntArrList.add(R.drawable.tutorial_four);

        //Init tutorial adapter
        mTutorialImgPager.setAdapter(new TutorialImagesPager(this, tutorialImgIntArrList));
        mTutorialImgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Change indicator images
                mIndicatorOneImg.setImageResource(position == 0 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorTwoImg.setImageResource(position == 1 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorThreeImg.setImageResource(position == 2 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
                mIndicatorFourImg.setImageResource(position == 3 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_gray_bg);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.skip_txt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_txt:
                GlobalMethods.storeValueToPreference(this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                nextScreen(LoginScreen.class, true);
                break;
        }

    }

    //Tutorial pager
    private class TutorialImagesPager extends PagerAdapter {
        private Context mContext;
        private ArrayList<Integer> mImagesArrList;

        private TutorialImagesPager(Context context, ArrayList<Integer> userList) {
            //Default construction
            mContext = context;
            mImagesArrList = userList;
        }

        @Override
        public int getCount() {
            return mImagesArrList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ViewGroup rootViewGrp = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.adap_tutorial_view,
                    container, false);

            //Viewpager image
            final GifImageView profileImg = (GifImageView) rootViewGrp.findViewById(R.id.tutorial_img);
            profileImg.setImageResource(mImagesArrList.get(position));
            container.addView(rootViewGrp);

            return rootViewGrp;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Parent layout is relative
            container.removeView((RelativeLayout) object);
        }

    }
}
