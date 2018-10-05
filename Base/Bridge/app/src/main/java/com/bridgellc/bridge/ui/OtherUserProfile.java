package com.bridgellc.bridge.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.apiinterface.APIRequestHandler;
import com.bridgellc.bridge.entity.HomeSingleItemEntity;
import com.bridgellc.bridge.entity.ProfileEntity;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.model.ProfileResponse;
import com.bridgellc.bridge.utils.AppConstants;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogMangerOkCallback;

import java.util.ArrayList;
import java.util.Locale;


public class OtherUserProfile extends BaseFragmentActivity implements View.OnClickListener,
        DialogMangerOkCallback {

    private ViewPager mViewpager;
    private ImageView mLeftArrow, mRightArrow;
    private RatingBar mUserRatingbar;
    private TextView mProfileNameTxt, mUniversityNameTxt, mItemTxt, mVerifiedTxt;
    public static String mOtherUSerID;
    private LinearLayout mCertiPartnerLay;
    public ArrayList<Fragment> mFragmentsList;
    private ProfileEntity mProfileDataRes;
    private ArrayList<HomeSingleItemEntity> mUserItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_user_profile);

        mFragmentsList = new ArrayList<>();
        mUserItemList = new ArrayList<>();
        mProfileDataRes = new ProfileEntity();

        initComponents();
    }

    private void initComponents() {

        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);

        //Glob Var Decl
        mHeaderTxt = (TextView) findViewById(R.id.header_txt);
        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);

        mViewpager = (ViewPager) findViewById(R.id.pager);
        mLeftArrow = (ImageView) findViewById(R.id.left_arrow);
        mRightArrow = (ImageView) findViewById(R.id.right_arrow);

        mProfileNameTxt = (TextView) findViewById(R.id.profile_name_txt);
        mUniversityNameTxt = (TextView) findViewById(R.id.university_name_txt);
        mUserRatingbar = (RatingBar) findViewById(R.id.user_ratingbar);
        mItemTxt = (TextView) findViewById(R.id.item_txt);
        mVerifiedTxt = (TextView) findViewById(R.id.verified_txt);
        mCertiPartnerLay = (LinearLayout) findViewById(R.id.certi_partner_lay);

        //Local Var Decl
        Button seeAllReviews = (Button) findViewById(R.id.footer_btn);

        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);

        mHeaderTxt.setText(getString(R.string.otherprofile).toUpperCase(Locale.ENGLISH));


        seeAllReviews.setText(getString(R.string.seeallreviews));

        mViewpager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {

                        mRightArrow.setVisibility(View.VISIBLE);
                        mLeftArrow.setVisibility(View.VISIBLE);
                        if (mViewpager.getCurrentItem() == 0) {
                            mLeftArrow.setVisibility(View.GONE);
                        }
                        if (mViewpager.getCurrentItem() == (mFragmentsList.size() - 1)) {
                            mRightArrow.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });

//        mHeadeRightBtnLay.setVisibility(mOtherUSerID.equalsIgnoreCase(GlobalMethods.getUserID(this)) ? View.INVISIBLE : View.VISIBLE);
        APIRequestHandler.getInstance().getOtherProfileResponse(mOtherUSerID, this);
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof ProfileResponse) {
            ProfileResponse otherProfres = (ProfileResponse) responseObj;

            if (otherProfres.getResponse_code().equalsIgnoreCase(AppConstants.SUCCESS_CODE)) {

                mProfileDataRes = otherProfres.getResult();
                mUserItemList = mProfileDataRes.getUser_listing();
                if (mProfileDataRes != null && mUserItemList != null) {
                    populateData();
                    mViewpager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                }
            } else {
                DialogManager.showBasicAlertDialog(this,
                        otherProfres.getMessage(), this);
            }
        }
    }


    public void populateData() {


        mFragmentsList = new ArrayList<>();

        mProfileNameTxt.setText(mProfileDataRes.getFirst_name());
        mUniversityNameTxt.setText(mProfileDataRes.getUniversity_name());
        if (mProfileDataRes.getUser_rating() != null && !mProfileDataRes.getUser_rating()
                .equalsIgnoreCase("")) {
            mUserRatingbar.setRating(Float.parseFloat(mProfileDataRes.getUser_rating()));
        } else {
            mUserRatingbar.setRating(0.0f);
        }
        mItemTxt.setText(itemCount(mProfileDataRes.getTotal_listings()));


        if (mProfileDataRes.getSeller_partner() != null && (mProfileDataRes.getSeller_partner()
                .equalsIgnoreCase(getString(R.string.one)) || mProfileDataRes.getSeller_partner().equalsIgnoreCase
                (getString(R.string.two)))) {

            mVerifiedTxt.setText(getString(R.string.cert_partner));
            mCertiPartnerLay.setVisibility(View.VISIBLE);
        } else if (mProfileDataRes.getUser_verified() != null && mProfileDataRes.getUser_verified()
                .equalsIgnoreCase(getString(R.string.one))) {

            mVerifiedTxt.setText(getString(R.string.cert_student));
            mCertiPartnerLay.setVisibility(View.VISIBLE);
        } else {
            mCertiPartnerLay.setVisibility(View.GONE);
        }


        int fragmentSize = mUserItemList.size() / 6;


        if (mUserItemList.size() % 6 != 0) {
            ++fragmentSize;
        }


        for (int i = 0; i < fragmentSize; i++) {
            OtherUserProfileItemsFragment otherUserProfileItemsFragment = new
                    OtherUserProfileItemsFragment();
            ArrayList<HomeSingleItemEntity> currentList = new ArrayList<>();

            for (int j = i * 6; j < (i + 1) * 6; j++) {
                if (mUserItemList.size() > j) {
                    currentList.add(mUserItemList.get(j));
                }
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable("HomeDataList", currentList);
            otherUserProfileItemsFragment.setArguments(bundle);
            mFragmentsList.add(otherUserProfileItemsFragment);

            mRightArrow.setVisibility(View.VISIBLE);
            mLeftArrow.setVisibility(View.GONE);
            if (mFragmentsList.size() == 1 || mFragmentsList.size() == 0) {
                mRightArrow.setVisibility(View.GONE);
            }


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_left_btn_lay:
                onBackPressed();
                break;
            case R.id.left_arrow:
                mViewpager.setCurrentItem(mViewpager.getCurrentItem() - 1);
                break;
            case R.id.right_arrow:
                mViewpager.setCurrentItem(mViewpager.getCurrentItem() + 1);
                break;
            case R.id.footer_btn:
                if (mProfileDataRes != null && mProfileDataRes.getUser_id() != null) {
                    ReviewScreen.mOtherUserId = mProfileDataRes.getUser_id();
                    nextScreen(ReviewScreen.class, false);
                }
                break;

        }
    }

    @Override
    public void onOkClick() {

    }

    private String itemCount(String mItem) {
        String returnItem = mItem + " " + getString(R.string.items);

        if (mItem.equalsIgnoreCase(getString(R.string.zero)) || mItem.equalsIgnoreCase(getString(R.string.one)))
            returnItem = mItem + " " + getString(R.string.ite);
        return returnItem;
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

    @Override
    public void onBackPressed() {
        finishScreen();
    }
}
