package com.bridgellc.bridge.ui.upload;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bridgellc.bridge.R;
import com.bridgellc.bridge.main.BaseFragmentActivity;
import com.bridgellc.bridge.ui.home.HomeScreenActivity;
import com.bridgellc.bridge.utils.DialogManager;
import com.bridgellc.bridge.utils.DialogManagerTwoBtnCallback;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by USER on 3/14/2016.
 */
public class UploadScreen extends BaseFragmentActivity {

    public ArrayList<Fragment> mFragmentsList = new ArrayList<Fragment>();
    private ViewPager mUploadScreenPager;
    public static UploadEntityClass mEntity;

    private TextView titleTv;
    private ImageView mHeaderLeftImage, mHeaderRightImage;

    private ImageView mArrowImage;

    public UploadPhotosFragment mUploadPhotosFragment;

    public UploadPublishFragment mUploadPublishFragment;
    private Fragment mFragment = null;
    public UploadDropboxPreviewFragment mUploadDropboxPreviewFragment;
    public UploadDropboxOrginalFragment mUploadDropboxOrginalFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_screen);
        initComponents();

    }

    private void initComponents() {
        mViewGroup = (ViewGroup) findViewById(R.id.parent_lay);
        setupUI(mViewGroup);
        setFont(mViewGroup, mLightFont);
        titleTv = (TextView) findViewById(R.id.header_txt);
        titleTv.setText(getString(R.string.upload_txt).toUpperCase(Locale.ENGLISH));
        mHeaderLeftImage = (ImageView) findViewById(R.id.header_left_img);
        mHeaderRightImage = (ImageView) findViewById(R.id.header_right_img);
        mHeaderLeftImage.setImageResource(R.drawable.back_img);
        mHeaderRightImage.setImageResource(R.drawable.refresh_icon);

        mHeadeLeftBtnLay = (RelativeLayout) findViewById(R.id.header_left_btn_lay);
        mHeadeRightBtnLay = (RelativeLayout) findViewById(R.id.header_right_btn_lay);


        mHeadeLeftBtnLay.setVisibility(View.VISIBLE);
        mHeadeRightBtnLay.setVisibility(View.VISIBLE);

        mUploadScreenPager = (ViewPager) findViewById(R.id.uploadPager);

        mHeadeLeftBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrevFragment();
            }
        });
        mHeadeRightBtnLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogManager.showBaseTwoBtnDialog(UploadScreen.this, getString(R.string.app_name), getString(R.string.are_want_start), getString(R.string.yes), getString(R.string.no), new DialogManagerTwoBtnCallback() {
                    @Override
                    public void onBtnOkClick(String mOkStr) {

                        mEntity = new UploadEntityClass();
                        addFragment(new UploadSelectSellingRequesting(), 0);
                    }

                    @Override
                    public void onBtnCancelClick(String mCancelStr) {

                    }
                });
            }
        });


        mEntity = new UploadEntityClass();
        addFragment(new UploadSelectSellingRequesting(), 0);

        mUploadScreenPager
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Fragment mFragment = mFragmentsList.get(position);

                        if (mFragment instanceof UploadDropboxPreviewFragment) {
                            titleTv.setText(getString(R.string.upload_preview).toUpperCase(Locale.ENGLISH));
                        } else if (mFragment instanceof UploadDropboxOrginalFragment) {
                            titleTv.setText(getString(R.string.upload_orginal).toUpperCase(Locale.ENGLISH));
                        } else {
                            titleTv.setText(getString(R.string.upload_txt).toUpperCase(Locale.ENGLISH));
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });


    }

    public void showNextFragment() {
        int position = mUploadScreenPager.getCurrentItem();
        mUploadScreenPager.setCurrentItem((position + 1));
    }

    public void showPrevFragment() {
        if (slidingDrawer.isOpened()) {
            slidingDrawer.animateClose();
        }
        int position = mUploadScreenPager.getCurrentItem();
        if (position > 0) {
            mUploadScreenPager.setCurrentItem((position - 1));
        } else {
            finishScreen();
        }

    }

//    @Override
//    protected void onResumeFragments() {
//        super.onResumeFragments();
//        if (mFragment instanceof UploadPublishFragment) {
//            UploadPublishFragment.resumCall(this);
//        }
//    }

    @Override
    public void addFragment(Fragment fragment, int position) {

        mFragment = fragment;
        if (mFragmentsList.size() > position) {
            ArrayList<Fragment> mNewList = new ArrayList<Fragment>();
            for (int i = 0; i < position; i++) {
                mNewList.add(mFragmentsList.get(i));
            }
            mFragmentsList = mNewList;
        }
        if (fragment instanceof UploadPhotosFragment) {
            mUploadPhotosFragment = (UploadPhotosFragment) fragment;
        }

        if (fragment instanceof UploadPublishFragment) {
            mUploadPublishFragment = (UploadPublishFragment) fragment;
        }
        if (fragment instanceof UploadDropboxPreviewFragment) {
            mUploadDropboxPreviewFragment = (UploadDropboxPreviewFragment) fragment;
        }
//        if (fragment instanceof UploadDropboxPreviewFragment) {
//            titleTv.setText(getString(R.string.upload_preview));
//        } else if (fragment instanceof UploadDropboxOrginalFragment) {
//            titleTv.setText(getString(R.string.upload_orginal));
//        } else {
//            titleTv.setText(getString(R.string.upload_txt));
//        }

        mFragmentsList.add(fragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mUploadScreenPager.setAdapter(myPagerAdapter);
        mUploadScreenPager.setOffscreenPageLimit(1);
        mUploadScreenPager.setCurrentItem(mFragmentsList.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == UploadDropboxPreviewFragment.DBX_CHOOSER_REQUEST) {
                mUploadDropboxPreviewFragment.onActivityResult(requestCode, resultCode, data);
            } else if (requestCode == UploadDropboxOrginalFragment.DX_CHOOSER_REQUEST) {
                mUploadDropboxOrginalFragment.onActivityResult(requestCode, resultCode, data);
            } else {
                mUploadPhotosFragment.onActivityResult(requestCode, resultCode, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            previousScreen(HomeScreenActivity.class, true);
        }
    }

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        mUploadPublishFragment.onRequestSuccess(responseObj);
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

        showPrevFragment();
    }
}
