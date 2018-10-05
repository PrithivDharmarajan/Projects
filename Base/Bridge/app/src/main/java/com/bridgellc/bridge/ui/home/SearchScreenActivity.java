package com.bridgellc.bridge.ui.home;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.HomeResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by USER on 3/14/2016.
 */
public class SearchScreenActivity extends BaseFragmentActivity {

    public ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
    private ViewPager mUploadScreenPager;

//    private ImageView mHeaderLeftImage,mHeaderRightImage,mArrowImage;

    private ImageView indicator1, indicator2, indicator3;

    private int limit = 500, offset = 0;

    public static String filterOption = "", filterType = "";

    private HomeResponse homeResponse;
    GradientDrawable bgShapeLightGray, bgShapeBlue;

    private Button mCancelButton;

    private LinearLayout viewPagerIndicatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);
        initComponents();

    }


    private void initComponents() {
        mUploadScreenPager = (ViewPager) findViewById(R.id.uploadPager);

        indicator1 = (ImageView) findViewById(R.id.one);
        indicator2 = (ImageView) findViewById(R.id.two);
        indicator3 = (ImageView) findViewById(R.id.three);
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);


        bgShapeLightGray = (GradientDrawable) getResources().getDrawable(R.drawable.round);
        bgShapeBlue = (GradientDrawable) getResources().getDrawable(R.drawable.round);


        bgShapeLightGray.setColor(getResources().getColor(R.color.gray_home));
        bgShapeBlue.setColor(getResources().getColor(R.color.blue_home));


        viewPagerIndicatorLayout = (LinearLayout) findViewById(R.id.view_pager_indicator);

        viewPagerIndicatorLayout.setVisibility(View.GONE);


        mCancelButton = (Button) findViewById(R.id.footer_btn);
        mCancelButton.setText(getString(R.string.cancel));

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final EditText search = (EditText) findViewById(R.id.search_text);

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && search.getText().toString().trim().length() > 0) {
                    APIRequestHandler.getInstance().getSearchResponse("" + limit, "" + offset, search.getText().toString().trim(), SearchScreenActivity.this);
                    hideSoftKeyboard(SearchScreenActivity.this);
                    return true;
                }
                return false;
            }
        });
        ImageView img = (ImageView) findViewById(R.id.search_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (search.getText().toString().length() > 0) {
                    APIRequestHandler.getInstance().getSearchResponse("" + limit, "" + offset, search.getText().toString().trim(), SearchScreenActivity.this);
                }
            }
        });


        mUploadScreenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indicator1.setBackground(position == 0 ? bgShapeBlue
                        : bgShapeLightGray);
                indicator2.setBackground(position == 1 ? bgShapeBlue
                        : bgShapeLightGray);
                indicator3.setBackground(position == 2 ? bgShapeBlue
                        : bgShapeLightGray);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        previousScreen(HomeScreenActivity.class, true);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof HomeResponse) {
            homeResponse = (HomeResponse) responseObj;

            if (homeResponse.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {
                populateData();
            } else {
                DialogManager.showBasicAlertDialog(SearchScreenActivity.this, homeResponse.getMessage(), new DialogMangerOkCallback() {
                    @Override
                    public void onOkClick() {

                    }
                });
            }
        }

    }

    public void populateData() {
//        viewPagerIndicatorLayout.setVisibility(View.VISIBLE);
        mFragmentsList = new ArrayList<Fragment>();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        Collections.sort(homeResponse.getResult().getGoods(), new Comparator<HomeSingleItemEntity>() {
            @Override
            public int compare(HomeSingleItemEntity ent2, HomeSingleItemEntity ent1) {
                Date d = new Date();
                Date d1 = new Date();
                try {
                    d = simpleDateFormat.parse(ent2.getCreated_datetime());
                    d1 = simpleDateFormat.parse(ent1.getCreated_datetime());
                } catch (Exception e) {

                }

                return d.after(d1) ? -1 : 1;
            }
        });


        ArrayList<HomeSingleItemEntity> goodsList = new ArrayList<HomeSingleItemEntity>();


        for (HomeSingleItemEntity homeEntity :
                homeResponse.getResult().getGoods()) {

//            boolean isAdd = true;
//
//            if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase("ALL")) {
//                if (!(homeEntity.getCategory_name().equalsIgnoreCase(filterOption))) {
//                    isAdd = false;
//                }
//            }
//            if (isAdd) {
//                if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
//                    if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
//                        isAdd = false;
//                    }
//                } else {
//                    isAdd = true;
//
//                }
//            }
//
//            if (isAdd) {
            goodsList.add(homeEntity);
//            }
        }


        ArrayList<HomeSingleItemEntity> list = new ArrayList<HomeSingleItemEntity>();

        for (HomeSingleItemEntity homeEntity :
                homeResponse.getResult().getGoods()) {

            boolean isAdd = true;

//            if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase("ALL")) {
//                if (!(homeEntity.getCategory_name().equalsIgnoreCase(filterOption))) {
//                    isAdd = false;
//                }
//            }
//            if (isAdd) {
//                if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
//                    if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
//                        isAdd = false;
//                    }
//                } else {
//                    isAdd = true;
//
//                }
//            }
//
//            if (isAdd) {
            list.add(homeEntity);
//            }
        }

        for (HomeSingleItemEntity homeEntity :
                homeResponse.getResult().getServices()) {

            boolean isAdd = true;

//            if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase("ALL")) {
//                if (!(homeEntity.getCategory_name().equalsIgnoreCase(filterOption))) {
//                    isAdd = false;
//                }
//            }
//            if (isAdd) {
//                if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
//                    if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
//                        isAdd = false;
//                    }
//                } else {
//                    isAdd = true;
//
//                }
//            }
//
//            if (isAdd) {
            list.add(homeEntity);
//            }
        }

        Collections.sort(list, new Comparator<HomeSingleItemEntity>() {
            @Override
            public int compare(HomeSingleItemEntity ent2, HomeSingleItemEntity ent1) {
                Date d = new Date();
                Date d1 = new Date();
                try {
                    d = simpleDateFormat.parse(ent2.getCreated_datetime());
                    d1 = simpleDateFormat.parse(ent1.getCreated_datetime());
                } catch (Exception e) {

                }

                return d.after(d1) ? -1 : 1;
            }
        });


        Collections.sort(homeResponse.getResult().getServices(), new Comparator<HomeSingleItemEntity>() {
            @Override
            public int compare(HomeSingleItemEntity ent2, HomeSingleItemEntity ent1) {
                Date d = new Date();
                Date d1 = new Date();
                try {
                    d = simpleDateFormat.parse(ent2.getCreated_datetime());
                    d1 = simpleDateFormat.parse(ent1.getCreated_datetime());
                } catch (Exception e) {

                }

                return d.after(d1) ? -1 : 1;
            }
        });


        ArrayList<HomeSingleItemEntity> servicesList = new ArrayList<HomeSingleItemEntity>();

        for (HomeSingleItemEntity homeEntity :
                homeResponse.getResult().getServices()) {

//            boolean isAdd = true;
//
//            if (filterOption.length() > 0 && !filterOption.equalsIgnoreCase("ALL")) {
//                if (!(homeEntity.getItem_category().equalsIgnoreCase(filterOption))) {
//                    isAdd = false;
//                }
//            }
//            if (isAdd) {
//                if (filterType.length() > 0 && !filterType.equalsIgnoreCase("3")) {
//                    if (!(homeEntity.getItem_mode().equalsIgnoreCase(filterType))) {
//                        isAdd = false;
//                    }
//                } else {
//                    isAdd = true;
//
//                }
//            }
//
//            if (isAdd) {
            servicesList.add(homeEntity);
//            }
        }


        HomeGoodsServicesFragment goodsFragment = new HomeGoodsServicesFragment();
        Bundle goodsBundle = new Bundle();

        goodsBundle.putSerializable("HomeDataList", goodsList);

        goodsFragment.setArguments(goodsBundle);


        HomeGoodsServicesFragment homeFragment = new HomeGoodsServicesFragment();
        Bundle homeBundle = new Bundle();


        homeBundle.putSerializable("HomeDataList", list);

        homeFragment.setArguments(homeBundle);


        HomeGoodsServicesFragment servicesFragment = new HomeGoodsServicesFragment();
        Bundle serviceBundle = new Bundle();

        serviceBundle.putSerializable("HomeDataList", servicesList);

        servicesFragment.setArguments(serviceBundle);

//        mFragmentsList.add(goodsFragment);
        mFragmentsList.add(homeFragment);
//        mFragmentsList.add(servicesFragment);


        mUploadScreenPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mUploadScreenPager.setCurrentItem(0);
//        indicator1.setImageResource(R.drawable.grey_dot);
//        indicator2.setImageResource(R.drawable.black_dot);
//        indicator3.setImageResource(R.drawable.grey_dot);

        if (list.size() == 0) {
            DialogManager.showBasicAlertDialog(this, getString(R.string.no_result), new DialogMangerOkCallback() {
                @Override
                public void onOkClick() {

                }
            });
        }


//        filterOption = "";
//        filterType = "";
    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {


        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }


    }




}
