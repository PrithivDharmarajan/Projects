package com.fautus.fautusapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fautus.fautusapp.R;
import com.fautus.fautusapp.main.BaseActivity;
import com.fautus.fautusapp.utils.AppConstants;
import com.fautus.fautusapp.utils.ParseAPIConstants;
import com.fautus.fautusapp.utils.PreferenceUtil;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class implements UI and functions for consumer and photographer tutorials
 */

public class TutorialScreen extends BaseActivity {


    /*Variable initialization using bind view*/

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

    @BindView(R.id.cancel_txt)
    TextView mCancelTxt;

    @BindView(R.id.skip_txt)
    TextView mSkipTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_tutorial_screen);
        initView();
    }

    private void initView() {
         /*ButterKnife for variable initialization*/
        ButterKnife.bind(this);

       /*Keypad to be hidden when a touch made outside the edit text*/
        setupUI(mTutorialViewGroup);

        /*welcome screen - Added layout to array list*/
        ArrayList<Integer> tutorialXmlIntArrList = new ArrayList<>();

       /* If the value of AppConstants.WELCOME_SCREEN_TYPE is one, Then the screen can react in a Consumer welcome screen . orelse, it reacts in a photographer Welcome Screen*/
        /*If the current screen could be a photographer's screen, then the cancel text view will be visible, otherwise it will be invisible*/
        mCancelTxt.setVisibility(AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) ? View.VISIBLE : View.GONE);
        /*If the current screen could be a Consumer's screen, then the Skip text view will be visible, otherwise it will be invisible*/
        mSkipTxt.setVisibility(AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) ? View.GONE : View.VISIBLE);

        if (AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE)) {
            tutorialXmlIntArrList.add(R.layout.photographer_tutorial_first_view);
            tutorialXmlIntArrList.add(R.layout.photographer_tutorial_second_view);
            tutorialXmlIntArrList.add(R.layout.photographer_tutorial_third_view);

        } else {
            tutorialXmlIntArrList.add(R.layout.consumer_tutorial_first_view);
            tutorialXmlIntArrList.add(R.layout.consumer_tutorial_second_view);
            tutorialXmlIntArrList.add(R.layout.consumer_tutorial_third_view);
        }

        /*Initialize Welcome adapter*/
        mTutorialImgPager.setAdapter(new WelcomeScreenPager(this, tutorialXmlIntArrList));
        mTutorialImgPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*Change indicator images*/
                mIndicatorOneImg.setImageResource(position == 0 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_hash_bg);
                mIndicatorTwoImg.setImageResource(position == 1 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_hash_bg);
                mIndicatorThreeImg.setImageResource(position == 2 ? R.drawable.circle_sky_blue_bg : R.drawable.circle_hash_bg);

                /*Position 2 - Cancel & Skip View will be hidden */
                mCancelTxt.setVisibility(AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) && position != 2 ? View.VISIBLE : View.GONE);
                mSkipTxt.setVisibility(AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) || position == 2 ? View.GONE : View.VISIBLE);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /*View OnClick*/
    @OnClick({R.id.cancel_txt, R.id.skip_txt, R.id.indicator_one_img, R.id.indicator_two_img, R.id.indicator_three_img})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_txt:
                /*direct to signUp screen*/
                AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
                if (AppConstants.PHOTOGRAPHER_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                    finishScreen();
                } else {
                    previousScreen(SignUpScreen.class, true);
                }
                break;
            case R.id.skip_txt:
                /*direct to signUp screen*/
                AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
                nextScreen(SignUpScreen.class, true);
                break;

            case R.id.indicator_one_img:
                mTutorialImgPager.setCurrentItem(0);
                break;

            case R.id.indicator_two_img:
                mTutorialImgPager.setCurrentItem(1);
                break;

            case R.id.indicator_three_img:
                mTutorialImgPager.setCurrentItem(2);
                break;
        }

    }


    private void nextScreen() {
          /*Check welcome screen type*/
        String welcomeScreenSeenStr = AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE) ? AppConstants.PHOTOGRAPHER_WELCOME_SCREEN_SEEN : AppConstants.CONSUMER_WELCOME_SCREEN_SEEN;

        /*Set flag if the screen has been viewed*/
        PreferenceUtil.storeBoolPreferenceValue(this, welcomeScreenSeenStr, true);

        /*Direct to next screen */
        nextScreen(SignUpScreen.class, true);
    }


    //Welcome screen pager
    private class WelcomeScreenPager extends PagerAdapter {
        private Context mContext;
        private ArrayList<Integer> mTutorialXmlArrList;

        private WelcomeScreenPager(Context context, ArrayList<Integer> userList) {
            /*Default construction*/
            mContext = context;
            mTutorialXmlArrList = userList;
        }

        @Override
        public int getCount() {
            return mTutorialXmlArrList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            ViewGroup rootViewGrp = (ViewGroup) LayoutInflater.from(mContext).inflate(mTutorialXmlArrList.get(position),
                    container, false);
            LinearLayout contLay;


            if (mTutorialXmlArrList.get(position).equals(R.layout.consumer_tutorial_third_view)) {
                contLay = rootViewGrp.findViewById(R.id.cont_lay);
                contLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ok got it click*/
                        AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
                        nextScreen();
                    }
                });

            } else if (mTutorialXmlArrList.get(position).equals(R.layout.photographer_tutorial_third_view)) {
                contLay = rootViewGrp.findViewById(R.id.cont_lay);
                ImageView youtubeVideoImg = rootViewGrp.findViewById(R.id.youtube_video_img);
                ImageView youtubePlayImg = rootViewGrp.findViewById(R.id.youtube_play_img);
                try {
                    Glide.with(mContext)
                            .load(String.format(AppConstants.YOUTUBE_VIDEO_IMAGE_URL, AppConstants.YOUTUBE_VIDEO_LD))
                            .into(youtubeVideoImg);
                } catch (Exception ex) {
                    Log.e(mContext.getClass().getSimpleName(), ex.getMessage());
                }
                youtubePlayImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Youtube video player click - direct to youtube player Screen*/
                        nextScreen(YoutubeScreen.class, false);

                    }
                });
                contLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*ok got it click*/
                        AppConstants.WELCOME_SCREEN_TYPE = AppConstants.SUCCESS_CODE;
                        if (AppConstants.PHOTOGRAPHER_FROM_MENU.equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                           /*Set flag for local purpose*/
                            AppConstants.PROFILE_FROM_MENU = AppConstants.FAILURE_CODE;
                            PreferenceUtil.storeBoolPreferenceValue(TutorialScreen.this, AppConstants.SETTINGS_STRIP_ON, true);
                            PreferenceUtil.storeBoolPreferenceValue(TutorialScreen.this, AppConstants.USER_IS_CONSUMER, false);

                            ParseUser user = ParseUser.getCurrentUser();
                            if (user != null) {
                                user.put(ParseAPIConstants.isPhotographer, true);
                                user.saveInBackground();
                            }
                           /*Direct to next screen*/
                            nextScreen(PhotographerProfileScreen.class, true);

                        } else {
                            nextScreen();
                        }
                    }
                });
            }

            container.addView(rootViewGrp);
            return rootViewGrp;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Parent layout is LinearLayout
            container.removeView((LinearLayout) object);
        }

    }

    @Override
    public void onBackPressed() {
        /* If the value of AppConstants.WELCOME_SCREEN_TYPE is one, redirect to signUp screen, orelse exit from the app*/
        if (AppConstants.WELCOME_SCREEN_TYPE.equals(AppConstants.SUCCESS_CODE)) {
            AppConstants.WELCOME_SCREEN_TYPE = AppConstants.FAILURE_CODE;
            previousScreen(SignUpScreen.class, true);
        } else {
            finish();
        }
    }
}
