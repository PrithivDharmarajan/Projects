package com.bridgellc.bridge.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.HomeResponse;
import com.bridgellc.bridge.ui.upload.UploadScreen;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.CustomSwipeToRefresh;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;
import com.bridgellc.bridge.utils.GlobalMethods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class HomeScreenActivity extends BaseFragmentActivity implements SwipeRefreshLayout
        .OnRefreshListener {

    public ArrayList<Fragment> mFragmentsList;
    public static ViewPager mUploadScreenPager;
    private ViewPager mTutorialPager;
    private TextView mHeaderSubTxt, mSkipTxt;
    private ImageView mHeaderRightImage;
    public static CustomSwipeToRefresh swipeRefreshLayout;
    private ImageView indicator1, indicator2, indicator3;
    GradientDrawable bgShapeLightGray, bgShapeBlue;
    ArrayList<Integer> mTutorialImgList = new ArrayList<Integer>();
    private Button mFooterOneBtn;
    private int limit = 500, offset = 0;
    private RelativeLayout mTutorialLay, mTutBtnLay;
    private int curPos = 1;
    public static String filterOption = "", filterType = "";
    ArrayList<HomeSingleItemEntity> mGoodsList, mServicesList, mList;
    private HomeResponse mHomeItemResponse;
    public static boolean isFilterShown = false;

    Dialog mAlertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        mFragmentsList = new ArrayList<>();
        mHomeItemResponse = new HomeResponse();
        mGoodsList = new ArrayList<>();
        mServicesList = new ArrayList<>();
        mList = new ArrayList<>();

        initComponents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        homeApiCall();
    }


    private void homeApiCall() {
        curPos = 1;

        if (GlobalMethods.getStringValue(HomeScreenActivity.this, AppConstants.UNIVERSITY_MODE).equalsIgnoreCase("")) {
            GlobalMethods.storeValuetoPreference(this,
                    GlobalMethods.STRING_PREFERENCE,
                    AppConstants.UNIVERSITY_MODE, AppConstants.FAILURE_CODE);
        }
        if ((Boolean) GlobalMethods.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN)) {

            APIRequestHandler.getInstance().getHomeResponse("" + limit, "" + offset, this);
        }
    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);


        setHeader();


        swipeRefreshLayout = (CustomSwipeToRefresh) findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        mUploadScreenPager = (ViewPager) findViewById(R.id.uploadPager);
        mTutorialPager = (ViewPager) findViewById(R.id.tutorial_pager);


        indicator1 = (ImageView) findViewById(R.id.one);
        indicator2 = (ImageView) findViewById(R.id.two);
        indicator3 = (ImageView) findViewById(R.id.three);


        mTutorialImgList.add(R.drawable.tutorial_1);
        mTutorialImgList.add(R.drawable.tutorial_2);
        mTutorialImgList.add(R.drawable.tutorial_3);
        mTutorialImgList.add(R.drawable.tutorial_4);
        mTutorialImgList.add(R.drawable.tutorial_5);
        mTutorialImgList.add(R.drawable.tutorial_6);
        mTutorialImgList.add(R.drawable.tutorial_7);
        mTutorialImgList.add(R.drawable.tutorial_8);
        mTutorialImgList.add(R.drawable.tutorial_9);
        mTutorialImgList.add(Color.TRANSPARENT);

        mTutorialPager.setAdapter(new TutorialPagerAdapter(mTutorialImgList));
        mTutorialPager.setCurrentItem(0);

        bgShapeLightGray = (GradientDrawable) getResources().getDrawable(R.drawable.round);
        bgShapeBlue = (GradientDrawable) getResources().getDrawable(R.drawable.round);


        bgShapeLightGray.setColor(getResources().getColor(R.color.gray_home));
        bgShapeBlue.setColor(getResources().getColor(R.color.blue_home));


        mTutorialPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position == mTutorialImgList.size() - 1) {

                    HomeScreenActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    GlobalMethods.storeValuetoPreference(HomeScreenActivity.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                    mTutorialLay.setVisibility(View.GONE);
                    APIRequestHandler.getInstance().getHomeResponse("" + limit, "" + offset,
                            HomeScreenActivity.this);
                }

                if (position == 5 || position == 6) {
                    String tutBtnTxt = "";
                    mTutBtnLay.setVisibility(View.VISIBLE);
                    if (position == 5) {
                        tutBtnTxt = getString(R.string.new_post);
                    } else {
                        tutBtnTxt = getString(R.string.menu).toLowerCase(Locale.ENGLISH);
                    }

                    tutBtnTxt = tutBtnTxt.replace(tutBtnTxt.substring(0, 1), tutBtnTxt.substring(0, 1).toUpperCase(Locale.ENGLISH));
                    mFooterOneBtn.setText(tutBtnTxt);
                } else {
                    mTutBtnLay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
        mUploadScreenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                change_Page(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void change_Page(int pos) {
        curPos = pos;

        if (pos == 0) {
            mHeaderTxt.setText(getString(R.string.goods).toUpperCase(Locale.ENGLISH));
        } else if (pos == 1) {
            mHeaderTxt.setText(getString(R.string.home_c).toUpperCase(Locale.ENGLISH));
        } else {
            mHeaderTxt.setText(getString(R.string.services).toUpperCase(Locale.ENGLISH));
        }

        indicator1.setBackground(pos == 0 ? bgShapeBlue
                : bgShapeLightGray);
        indicator2.setBackground(pos == 1 ? bgShapeBlue
                : bgShapeLightGray);
        indicator3.setBackground(pos == 2 ? bgShapeBlue
                : bgShapeLightGray);

    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof HomeResponse) {
            mHomeItemResponse = (HomeResponse) responseObj;

            if (mHomeItemResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.PAYMENT_MODE, mHomeItemResponse.getResult().getPaymnet_mode());
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.BANK_DETAILS, mHomeItemResponse.getResult().getPaymnet_details().getBank_details());
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.CARD_DETAILS, mHomeItemResponse.getResult().getPaymnet_details().getCard_details());

                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.PAYMENT_DETAILS, mHomeItemResponse.getResult().getPayment_details());
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this,
                        GlobalMethods.STRING_PREFERENCE,
                        AppConstants.PARTNER, mHomeItemResponse.getResult().getPartner());

                populateData();
            } else {
                DialogManager.showBasicAlertDialog(HomeScreenActivity.this, mHomeItemResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }

        }
    }

    public void populateData() {

        if (mHomeItemResponse != null && mHomeItemResponse.getResult() != null) {

            if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase(getString(R.string.all))) {
                mHeaderSubTxt.setVisibility(View.VISIBLE);
                mHeaderSubTxt.setText("(" + filterOption + ")");
            } else {
                mHeaderSubTxt.setVisibility(View.GONE);
            }

            mFragmentsList = new ArrayList<>();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

            if (mHomeItemResponse.getResult().getGoods() != null && mHomeItemResponse.getResult().getGoods()
                    .size() > 0) {
                Collections.sort(mHomeItemResponse.getResult().getGoods(), new Comparator<HomeSingleItemEntity>() {
                    @Override
                    public int compare(HomeSingleItemEntity goodsResOne, HomeSingleItemEntity
                            goodsResTwo) {
                        Date d = new Date();
                        Date d1 = new Date();
                        try {
                            d = simpleDateFormat.parse(goodsResOne.getCreated_datetime());
                            d1 = simpleDateFormat.parse(goodsResTwo.getCreated_datetime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return d.after(d1) ? -1 : 1;
                    }
                });

                mGoodsList = new ArrayList<>();
                mList = new ArrayList<>();

                for (HomeSingleItemEntity homeEntity :
                        mHomeItemResponse.getResult().getGoods()) {

                    boolean isAdd = true;

                    if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase(getString(R
                            .string.all)
                    )) {
                        if (!(homeEntity.getCategory_name().equalsIgnoreCase(filterOption))) {
                            isAdd = false;
                        }
                    }
                    if (isAdd) {
                        if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
                            if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
                                isAdd = false;
                            }
                        } else {
                            isAdd = true;

                        }
                    }

                    if (isAdd) {
                        mGoodsList.add(homeEntity);
                        mList.add(homeEntity);
                    }
                }

            }
            if (mHomeItemResponse.getResult().getServices() != null && mHomeItemResponse.getResult().getServices
                    ().size() > 0) {
                Collections.sort(mHomeItemResponse.getResult().getServices(), new Comparator<HomeSingleItemEntity>() {
                    @Override
                    public int compare(HomeSingleItemEntity ent2, HomeSingleItemEntity ent1) {
                        Date d = new Date();
                        Date d1 = new Date();
                        try {
                            d = simpleDateFormat.parse(ent2.getCreated_datetime());
                            d1 = simpleDateFormat.parse(ent1.getCreated_datetime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return d.after(d1) ? -1 : 1;
                    }
                });


                mServicesList = new ArrayList<>();
                for (HomeSingleItemEntity homeEntity :
                        mHomeItemResponse.getResult().getServices()) {

                    boolean isAdd = true;

                    if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase("ALL")) {
                        if (!(homeEntity.getCategory_name().equalsIgnoreCase(filterOption))) {
                            isAdd = false;
                        }
                    }
                    if (isAdd) {
                        if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
                            if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
                                isAdd = false;
                            }
                        } else {
                            isAdd = true;

                        }
                    }

                    if (isAdd) {
                        mList.add(homeEntity);
                        mServicesList.add(homeEntity);
                    }
                }
            }
            Collections.sort(mList, new Comparator<HomeSingleItemEntity>() {
                @Override
                public int compare(HomeSingleItemEntity ent2, HomeSingleItemEntity ent1) {
                    Date d = new Date();
                    Date d1 = new Date();
                    try {
                        d = simpleDateFormat.parse(ent2.getCreated_datetime());
                        d1 = simpleDateFormat.parse(ent1.getCreated_datetime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return d.after(d1) ? -1 : 1;
                }
            });


            HomeGoodsServicesFragment goodsFragment = new HomeGoodsServicesFragment();
            Bundle goodsBundle = new Bundle();

            goodsBundle.putSerializable("HomeDataList", mGoodsList);

            goodsFragment.setArguments(goodsBundle);


            HomeGoodsServicesFragment homeFragment = new HomeGoodsServicesFragment();
            Bundle homeBundle = new Bundle();

            homeBundle.putSerializable("HomeDataList", mList);
            homeFragment.setArguments(homeBundle);


            HomeGoodsServicesFragment servicesFragment = new HomeGoodsServicesFragment();
            Bundle serviceBundle = new Bundle();

            serviceBundle.putSerializable("HomeDataList", mServicesList);

            servicesFragment.setArguments(serviceBundle);

            mFragmentsList.add(goodsFragment);
            mFragmentsList.add(homeFragment);
            mFragmentsList.add(servicesFragment);
            mUploadScreenPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            mUploadScreenPager.setCurrentItem(curPos);
            change_Page(curPos);

            if (mAlertDialog != null && mAlertDialog.isShowing()) {
                mAlertDialog.dismiss();
            }
            if (mList != null && mList.size() < 1) {
                mAlertDialog = DialogManager.showBasicAlertDialog(this,
                        getString(R.string.no_result), new DialogMangerOkCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }


        }

    }

    private void setHeader() {
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeaderSubTxt = (TextView) findViewById(R.id.header_filesub_txt);


        mHeaderTxt.setText(getString(R.string.home_c));
        ImageView mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.home_filter_icon);
        mHeaderRightImage.setImageResource(R.drawable.home_upload_icon);

        mTutorialLay = (RelativeLayout) findViewById(R.id.tutorial_pager_lay);

        mTutBtnLay = (RelativeLayout) findViewById(R.id.tut_btn_lay);
        mFooterOneBtn = (Button) findViewById(R.id.footer_btn);
        mSkipTxt = (TextView) findViewById(R.id.skip_txt);

        mSkipTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeScreenActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                mTutorialLay.setVisibility(View.GONE);
                APIRequestHandler.getInstance().getHomeResponse("" + limit, "" + offset,
                        HomeScreenActivity.this);
            }
        });
        mFooterOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFooterOneBtn.getText().toString().equalsIgnoreCase(getString(R.string.menu))) {
                    if (slidingDrawer != null) {
                        slidingDrawer.animateOpen();
                    }
                } else {
                    nextScreen(UploadScreen.class, false);
                }
                HomeScreenActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                GlobalMethods.storeValuetoPreference(HomeScreenActivity.this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN, true);
                mTutorialLay.setVisibility(View.GONE);
                APIRequestHandler.getInstance().getHomeResponse("" + limit, "" + offset,
                        HomeScreenActivity.this);
            }
        });

        if ((Boolean) GlobalMethods.getValueFromPreference(this, GlobalMethods.BOOLEAN_PREFERENCE, AppConstants.TUTORIAL_SEEN)) {
            mTutorialLay.setVisibility(View.GONE);
        } else {
            //Full View (set Flag)
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mTutorialLay.setVisibility(View.VISIBLE);
        }

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);


        mHeadeRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (slidingDrawer.isOpened()) {
                    slidingDrawer.animateClose();
                }
                HomeScreenFilterDialog.closeFilterDialog();
                nextScreen(UploadScreen.class, false);
            }
        });

        mHeadeLeftBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (slidingDrawer.isOpened()) {
                    slidingDrawer.animateClose();
                }
                HomeScreenFilterDialog.showFilterDialog(HomeScreenActivity.this);

            }
        });

    }

    @Override
    public void onRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);

            APIRequestHandler.getInstance().getHomeResponse("" + limit, "" + offset, this);

        }
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {


        MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }


    }

    class TutorialPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        ArrayList<Integer> mImgList;

        private TutorialPagerAdapter(ArrayList<Integer> mList) {
            this.mImgList = mList;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mTutorialImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public Object instantiateItem(View view, final int position) {
            final ViewGroup nullParent = null;
            final View pagerview = inflater.inflate(
                    R.layout.swipe_tutorial_view, nullParent,false);


            ImageView tutorialImg = (ImageView) pagerview.findViewById(R.id.tutorial_img);

            tutorialImg.setBackgroundResource(mTutorialImgList.get(position));
            ((ViewPager) view).addView(pagerview, 0);


            return pagerview;
        }


    }

    @Override
    public void onBackPressed() {
        DialogManager.showBaseTwoBtnDialog(this, getString(R.string.app_name), getString(R.string.app_exit), getString(R.string.yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
            @Override
            public void onBtnOkClick(String mOkStr) {
                if (mHandlerBaseFrgAct != null) {
                    mHandlerBaseFrgAct.removeCallbacks(mRunBaseFrgAct);
                }
                if (notifyHandler != null) {
                    notifyHandler.removeCallbacks(notifyCheckingService);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }

            @Override
            public void onBtnCancelClick(String mCancelStr) {

            }
        });

    }

}
